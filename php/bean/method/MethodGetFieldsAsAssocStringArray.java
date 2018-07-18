package php.bean.method;

import database.column.Column;
import php.bean.BeanCls;
import php.core.Types;
import php.core.expression.AssocArrayInitExpression;
import php.core.expression.Expression;
import php.core.method.Method;
import util.Pair;

public class MethodGetFieldsAsAssocStringArray extends Method {

	public MethodGetFieldsAsAssocStringArray() {
		super(Public, Types.array(Types.String), "getFieldsAsAssocStringArray");
	}

	@Override
	public void addImplementation() {
		BeanCls bean = (BeanCls) parent;
		
		AssocArrayInitExpression expr = new AssocArrayInitExpression();
		for(Column col : bean.getTbl().getAllColumns()) {
			if(!col.hasRelation())
				expr.addElement(new Pair<String, Expression>(col.getName(), BeanCls.getTypeMapper().getConvertFieldToStringExpression(_this().callMethod("get"+col.getUc1stCamelCaseName()), col)));
		}
		_return(expr);

	}

}
