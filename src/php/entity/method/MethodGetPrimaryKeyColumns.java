package php.entity.method;

import database.column.Column;
import database.relation.PrimaryKey;
import php.core.Types;
import php.core.expression.ArrayInitExpression;
import php.core.expression.PhpStringLiteral;
import php.core.method.Method;

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
