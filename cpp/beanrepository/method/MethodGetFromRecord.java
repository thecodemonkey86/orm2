package cpp.beanrepository.method;

import java.util.List;

import cpp.Types;
import cpp.CoreTypes;
import cpp.bean.BeanCls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.Expression;
import cpp.core.expression.IntExpression;
import cpp.core.expression.QStringPlusOperatorExpression;
import cpp.core.expression.Var;
import cpp.core.instruction.IfBlock;
import cpp.lib.ClsQString;
import cpp.lib.ClsQVariant;
import cpp.lib.EnableSharedFromThis;
import database.FirebirdDatabase;
import database.column.Column;
import cpp.core.expression.ParenthesesExpression;

public class MethodGetFromRecord extends Method {
	protected List<Column> columns;
	protected BeanCls bean;
	
	public static final String getMethodName(BeanCls cls) {
		return "get"+cls.getName()+ "FromRecord";
	}
	
	public static final String getMethodNameStatic(BeanCls cls) {
		return "get"+cls.getName()+ "FromRecordStatic";
	}
	
	public MethodGetFromRecord(BeanCls cls, boolean staticVersion) {
		super(Public, cls.toSharedPtr(), staticVersion? getMethodNameStatic(cls) : getMethodName(cls) );
		if(staticVersion) {
			setStatic(true);
			addParam(new Param(Types.BeanRepository.toRawPointer(), "repository"));
		}
		addParam(new Param(Types.QSqlRecord.toConstRef(), "record"));
		addParam(new Param(Types.QString.toConstRef(), "alias"));
		this.columns = cls.getTbl().getColumns(true);
		this.bean = cls;
//		setConstQualifier(true);
	}

	@Override
	public boolean includeIfEmpty() {
		return true;
	}
	
	@Override
	public void addImplementation() {
		//Var bean = _declareMakeShared(parent, "bean");
		Var vBean = null;
		
		if(isStatic()) {
			Param pRepository = getParam("repository");
			vBean = _declareMakeShared(bean, "bean", pRepository);
		} else {
			vBean = _declareMakeShared(bean, "bean", _this().callMethod(EnableSharedFromThis.SHARED_FROM_THIS));
		}
		for(Column col:columns) {
			try{
				
				if (!col.hasOneRelation()) {
					
					Expression exprArrayIndex = new QStringPlusOperatorExpression(getParam("alias"), QString.fromStringConstant("__"+ col.getName()));
					if(BeanCls.getDatabase() instanceof FirebirdDatabase) {
						exprArrayIndex = new ParenthesesExpression(exprArrayIndex).callMethod(ClsQString.left, new IntExpression(31));
					}
					
					if (col.isNullable()) {
						Var val = _declare(CoreTypes.QVariant, "_val"+col.getUc1stCamelCaseName(),getParam("record").callMethod("value", exprArrayIndex));
						IfBlock ifIsNull = _if(val.callMethod(ClsQVariant.isNull));
						ifIsNull.thenBlock().addInstr(vBean.callMethodInstruction("set"+col.getUc1stCamelCaseName()+"NullInternal"));
						ifIsNull.elseBlock().addInstr(vBean.callMethodInstruction("set"+col.getUc1stCamelCaseName()+"Internal",getParam("record").callMethod("value", new QStringPlusOperatorExpression(getParam("alias"), QString.fromStringConstant("__"+ col.getName()))).callMethod(BeanCls.getDatabaseMapper().getQVariantConvertMethod(col))));
					} else {
						addInstr(vBean.callMethodInstruction("set"+col.getUc1stCamelCaseName()+"Internal",getParam("record").callMethod("value", exprArrayIndex).callMethod(BeanCls.getDatabaseMapper().getQVariantConvertMethod(col))));
					}
				}
//					_callMethodInstr(bean, "set"+col.getUc1stCamelCaseName(), getParam("record").callMethod("value", new QStringPlusOperatorExpression(getParam("alias"), QString.fromStringConstant("__"+ col.getName()))).callMethod(BeanCls.getDatabaseMapper().getQVariantConvertMethod(col)));
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(parent);
			}
		}
		addInstr(vBean.callMethodInstruction("setInsertNew",BoolExpression.FALSE));
		_return(vBean);
	}

}
