package php.cls.bean.method;

import generate.CodeUtil2;
import php.Types;
import php.cls.Method;
import php.cls.expression.PhpStringLiteral;


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
