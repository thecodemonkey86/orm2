package php.cls.bean.method;

import model.Column;
import php.PhpFunctions;
import php.Types;
import php.cls.Method;
import php.cls.Param;
import php.cls.expression.Expressions;
import php.cls.expression.PhpStringLiteral;
import php.cls.instruction.IfBlock;
import php.lib.PhpStringType;

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
