package sunjava.bean.method;

import java.util.List;

import database.column.Column;
import sunjava.bean.BeanCls;
import sunjava.core.Method;
import sunjava.core.Param;
import sunjava.core.Types;

public class MethodGetByRecordStatic extends Method {
	protected List<Column> columns;
	
	public MethodGetByRecordStatic(List<Column> columns,BeanCls cls) {
		super(Public, cls, "getByRecord");
		setStatic(true);
		addParam(new Param(Types.Connection, "sqlCon"));
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
	/*	Var bean = _declare(parent, "entity", getParam("sqlCon"));
//		Var bean = _declareNewRaw(returnType, "entity", parent.getAttrByName("sqlCon"));
		for(Column col:columns) {
			try{
//				if (!col.hasOneRelation()) {
					addInstr(bean.assignAttr(col.getCamelCaseName(),getParam("record").callMethod("value", new JavaStringPlusOperatorExpression(getParam("alias"), JavaString.fromStringConstant("__"+ col.getName()))).callMethod(BeanCls.getTypeMapper().getConvertMethod(col.getDbType()))));
//				}
//					_callMethodInstr(bean, "set"+col.getUc1stCamelCaseName(), getParam("record").callMethod("value", new JavaStringPlusOperatorExpression(getParam("alias"), JavaString.fromStringConstant("__"+ col.getName()))).callMethod(BeanCls.getTypeMapper().getConvertMethod(col.getDbType())));
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(parent);
			}
		}
		addInstr(bean.assignAttr("insert",BoolExpression.FALSE));
		_return(bean);*/
	}

}
