package sunjava.entity.method;

import sunjava.core.JavaString;
import sunjava.core.Method;
import sunjava.core.Types;
import util.CodeUtil2;

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
