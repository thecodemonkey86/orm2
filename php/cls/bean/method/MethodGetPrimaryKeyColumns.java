package php.cls.bean.method;

import php.Types;
import php.cls.Method;
import php.cls.expression.ArrayInitExpression;
import php.cls.expression.PhpStringLiteral;
import model.Column;
import model.PrimaryKey;

public class MethodGetPrimaryKeyColumns extends Method {
	PrimaryKey pk;
	
	public MethodGetPrimaryKeyColumns(PrimaryKey pk) {
		super(Public, Types.array(Types.String), "getPrimaryKeyColumns");
		this.pk = pk;
		setStatic(true);
	}

	@Override
	public void addImplementation() {
		ArrayInitExpression init = new ArrayInitExpression();
		for(Column col : pk.getColumns()) {
			init.addElement(new PhpStringLiteral(col.getEscapedName()));
		}
		
		_return(_declare(returnType, "pkCols", init));
	}
	
	
}
