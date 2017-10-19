package php.cls.bean.method;

import java.util.List;

import model.Column;
import php.Types;
import php.cls.Method;
import php.cls.Param;
import php.cls.bean.BeanCls;

public class MethodGetByRecordStatic extends Method {
	protected List<Column> columns;
	
	public MethodGetByRecordStatic(List<Column> columns,BeanCls cls) {
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
	/*	Var bean = _declare(parent, "bean", getParam("sqlCon"));
//		Var bean = _declareNewRaw(returnType, "bean", parent.getAttrByName("sqlCon"));
		for(Column col:columns) {
			try{
//				if (!col.hasOneRelation()) {
					addInstr(bean.assignAttr(col.getCamelCaseName(),getParam("record").callMethod("value", new PhpStringPlusOperatorExpression(getParam("alias"), PhpString.fromStringConstant("__"+ col.getName()))).callMethod(BeanCls.getTypeMapper().getConvertMethod(col.getDbType()))));
//				}
//					_callMethodInstr(bean, "set"+col.getUc1stCamelCaseName(), getParam("record").callMethod("value", new PhpStringPlusOperatorExpression(getParam("alias"), PhpString.fromStringConstant("__"+ col.getName()))).callMethod(BeanCls.getTypeMapper().getConvertMethod(col.getDbType())));
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(parent);
			}
		}
		addInstr(bean.assignAttr("insert",BoolExpression.FALSE));
		_return(bean);*/
	}

}
