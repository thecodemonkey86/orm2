package php.entity.method;

import database.column.Column;
import php.core.Param;
import php.core.PhpFunctions;
import php.core.Types;
import php.core.expression.Expressions;
import php.core.expression.PhpStringLiteral;
import php.core.instruction.IfBlock;
import php.core.method.Method;

public class MethodGetFieldName extends Method {

	protected Column col;
	
	
	public MethodGetFieldName(Column col) {
		super(Public, Types.String, getMethodName(col));
		setStatic(true);
		this.col = col;
		addParam(new Param(Types.String, "prefix", Expressions.Null));	
	}

	@Override
	public void addImplementation() {
		IfBlock ifPrefixIsNotNull = _if(getParam("prefix").isNotNull());
		
		ifPrefixIsNotNull.thenBlock()
		.
		
			_return(PhpFunctions.sprintf.call(new PhpStringLiteral("%s_%s"), getParam("prefix"), new PhpStringLiteral(col.getName())));
		ifPrefixIsNotNull.elseBlock()
		.
			_return(new PhpStringLiteral(col.getName()));
		
		
		
	}
	
	public static String getMethodName(Column col) {
		return "fieldName" + col.getUc1stCamelCaseName();
	}

}
