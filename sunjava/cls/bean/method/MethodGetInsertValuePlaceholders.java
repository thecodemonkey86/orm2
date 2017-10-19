package sunjava.cls.bean.method;

import generate.CodeUtil2;
import sunjava.Types;
import sunjava.cls.JavaString;
import sunjava.cls.Method;

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
