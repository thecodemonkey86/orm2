package cpp.entityrepository.method;

import java.util.List;

import cpp.Types;
import cpp.CoreTypes;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.Expression;
import cpp.core.expression.IntExpression;
import cpp.core.expression.QStringPlusOperatorExpression;
import cpp.core.expression.Var;
import cpp.core.instruction.IfBlock;
import cpp.entity.EntityCls;
import cpp.lib.ClsQString;
import cpp.lib.ClsQVariant;
import database.FirebirdDatabase;
import database.column.Column;
import cpp.core.expression.ParenthesesExpression;

public class MethodGetFromRecord extends Method {
	protected List<Column> columns;
	protected EntityCls bean;
	
	public static final String getMethodName(EntityCls cls) {
		return "get"+cls.getName()+ "FromRecord";
	}
	
	public static final String getMethodNameStatic(EntityCls cls) {
		return "get"+cls.getName()+ "FromRecordStatic";
	}
	
	public MethodGetFromRecord(EntityCls cls, boolean staticVersion) {
		super(Public, cls.toSharedPtr(), staticVersion? getMethodNameStatic(cls) : getMethodName(cls) );
		if(staticVersion) {
			setStatic(true);
			addParam(new Param(Types.EntityRepository.toRawPointer(), "repository"));
		}
		addParam(new Param(Types.QSqlRecord.toConstRef(), "record"));
		addParam(new Param(Types.QString.toConstRef(), "alias"));
		this.columns = cls.getTbl().getColumns(true);
		this.bean = cls;
		setStatic(true);
	}

	@Override
	public boolean includeIfEmpty() {
		return true;
	}
	
	@Override
	public void addImplementation() {
		//Var bean = _declareMakeShared(parent, "entity");
		Var vBean = _declareMakeShared(bean, "entity");
		for(Column col:columns) {
			try{
				
				if (!col.hasOneRelation() && !col.isFileImportEnabled()) {
					
					Expression exprArrayIndex = new QStringPlusOperatorExpression(getParam("alias"), QString.fromStringConstant("__"+ col.getName()));
					if(EntityCls.getDatabase() instanceof FirebirdDatabase) {
						exprArrayIndex = new ParenthesesExpression(exprArrayIndex).callMethod(ClsQString.left, new IntExpression(31));
					}
					
					if (col.isNullable()) {
						Var val = _declare(CoreTypes.QVariant, "_val"+col.getUc1stCamelCaseName(),getParam("record").callMethod("value", exprArrayIndex));
						IfBlock ifIsNull = _if(val.callMethod(ClsQVariant.isNull));
						Expression eValue = val.callMethod(EntityCls.getDatabaseMapper().getQVariantConvertMethod(col));
						ifIsNull.thenBlock().addInstr(vBean.callMethodInstruction("set"+col.getUc1stCamelCaseName()+"NullInternal"));
						ifIsNull.elseBlock().addInstr(vBean.callMethodInstruction("set"+col.getUc1stCamelCaseName()+"Internal",EntityCls.getDatabase() instanceof FirebirdDatabase && eValue.getType().equals(CoreTypes.QString) ? eValue.callMethod(ClsQString.trimmed) : eValue));
					} else {
						Expression eValue = getParam("record").callMethod("value", exprArrayIndex).callMethod(EntityCls.getDatabaseMapper().getQVariantConvertMethod(col));
						addInstr(vBean.callMethodInstruction("set"+col.getUc1stCamelCaseName()+"Internal",EntityCls.getDatabase() instanceof FirebirdDatabase && eValue.getType().equals(CoreTypes.QString) ? eValue.callMethod(ClsQString.trimmed) : eValue));
					}
				}
//					_callMethodInstr(bean, "set"+col.getUc1stCamelCaseName(), getParam("record").callMethod("value", new QStringPlusOperatorExpression(getParam("alias"), QString.fromStringConstant("__"+ col.getName()))).callMethod(BeanCls.getDatabaseMapper().getQVariantConvertMethod(col)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		addInstr(vBean.callMethodInstruction("setInsertNew",BoolExpression.FALSE));
		_return(vBean);
	}

}
