package php.bean.method;

import generate.CodeUtil2;
import php.core.Types;
import php.core.expression.PhpStringLiteral;
import php.core.method.Method;


public class MethodGetInsertValuePlaceholders extends Method {

	int columnCount;
	
	public MethodGetInsertValuePlaceholders(int columnCount) {
		super(Method.Protected,Types.String, "getInsertValuePlaceholders");
		this.columnCount = columnCount;
	}

	@Override
	public void addImplementation() {
		_return(new PhpStringLiteral(CodeUtil2.strMultiply("?", ",", columnCount)));
		
	}

}
