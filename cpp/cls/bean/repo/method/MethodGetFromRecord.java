package cpp.cls.bean.repo.method;

import java.util.List;

import model.Column;
import cpp.Types;
import cpp.cls.Method;
import cpp.cls.Param;
import cpp.cls.QString;
import cpp.cls.bean.BeanCls;
import cpp.cls.expression.BoolExpression;
import cpp.cls.expression.QStringPlusOperatorExpression;
import cpp.cls.expression.Var;
import cpp.cls.instruction.IfBlock;
import cpp.lib.ClsQVariant;
import cpp.lib.EnableSharedFromThis;

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
					if (col.isNullable()) {
						Var val = _declare(Types.QVariant, "_val"+col.getUc1stCamelCaseName(),getParam("record").callMethod("value", new QStringPlusOperatorExpression(getParam("alias"), QString.fromStringConstant("__"+ col.getName()))));
						IfBlock ifIsNull = _if(val.callMethod(ClsQVariant.isNull));
						ifIsNull.thenBlock().addInstr(vBean.callMethodInstruction("set"+col.getUc1stCamelCaseName()+"NullInternal"));
						ifIsNull.elseBlock().addInstr(vBean.callMethodInstruction("set"+col.getUc1stCamelCaseName()+"Internal",getParam("record").callMethod("value", new QStringPlusOperatorExpression(getParam("alias"), QString.fromStringConstant("__"+ col.getName()))).callMethod(BeanCls.getDatabaseMapper().getQVariantConvertMethod(col.getDbType()))));
					} else {
						addInstr(vBean.callMethodInstruction("set"+col.getUc1stCamelCaseName()+"Internal",getParam("record").callMethod("value", new QStringPlusOperatorExpression(getParam("alias"), QString.fromStringConstant("__"+ col.getName()))).callMethod(BeanCls.getDatabaseMapper().getQVariantConvertMethod(col.getDbType()))));
					}
				}
//					_callMethodInstr(bean, "set"+col.getUc1stCamelCaseName(), getParam("record").callMethod("value", new QStringPlusOperatorExpression(getParam("alias"), QString.fromStringConstant("__"+ col.getName()))).callMethod(BeanCls.getDatabaseMapper().getQVariantConvertMethod(col.getDbType())));
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(parent);
			}
		}
		addInstr(vBean.callMethodInstruction("setInsertNew",BoolExpression.FALSE));
		_return(vBean);
	}

}
