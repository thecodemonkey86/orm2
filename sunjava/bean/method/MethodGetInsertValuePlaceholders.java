package sunjava.bean.method;

import generate.CodeUtil2;
import sunjava.core.JavaString;
import sunjava.core.Method;
import sunjava.core.Types;

public class MethodGetInsertValuePlaceholders extends Method {

	int columnCount;
	
	public MethodGetInsertValuePlaceholders(int columnCount) {
		super(Method.Protected,Types.String, "getInsertValuePlaceholders");
		this.columnCount = columnCount;
	}

	@Override
	public void addImplementation() {
		_return(JavaString.stringConstant(CodeUtil2.strMultiply("?", ",", columnCount)));
		
	}

}
