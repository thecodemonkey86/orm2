package php.entity.method;

import java.util.List;

import database.column.Column;
import php.core.Param;
import php.core.Types;
import php.core.method.Method;
import php.entity.EntityCls;

public class MethodGetByRecordStatic extends Method {
	protected List<Column> columns;
	
	public MethodGetByRecordStatic(List<Column> columns,EntityCls cls) {
		super(Public, cls, "getByRecord");
		setStatic(true);
		addParam(new Param(Types.mysqli, "sqlCon"));
//		addParam(new Param(Types.QSqlRecord, "record"));
		addParam(new Param(Types.String, "alias"));
		this.columns = columns;
	}

	@Override
	public boolean includeIfEmpty() {
		return true;
	}
	
	@Override
	public void addImplementation() {
	/*	Var entity = _declare(parent, "entity", getParam("sqlCon"));
//		Var entity = _declareNewRaw(returnType, "entity", parent.getAttrByName("sqlCon"));
		for(Column col:columns) {
			try{
//				if (!col.hasOneRelation()) {
					addInstr(entity.assignAttr(col.getCamelCaseName(),getParam("record").callMethod("value", new PhpStringPlusOperatorExpression(getParam("alias"), PhpString.fromStringConstant("__"+ col.getName()))).callMethod(BeanCls.getTypeMapper().getConvertMethod(col.getDbType()))));
//				}
//					_callMethodInstr(entity, "set"+col.getUc1stCamelCaseName(), getParam("record").callMethod("value", new PhpStringPlusOperatorExpression(getParam("alias"), PhpString.fromStringConstant("__"+ col.getName()))).callMethod(BeanCls.getTypeMapper().getConvertMethod(col.getDbType())));
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(parent);
			}
		}
		addInstr(entity.assignAttr("insert",BoolExpression.FALSE));
		_return(entity);*/
	}

}
