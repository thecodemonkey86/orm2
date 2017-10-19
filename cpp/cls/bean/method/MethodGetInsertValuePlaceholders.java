package cpp.cls.bean.method;

import generate.CodeUtil2;
import cpp.Types;
import cpp.cls.Method;
import cpp.cls.QString;

public class MethodGetInsertValuePlaceholders extends Method {

	int columnCount;
	
	public MethodGetInsertValuePlaceholders(int columnCount) {
		super(Method.Public,Types.QString, "getInsertValuePlaceholders");
		this.columnCount = columnCount;
	}

	@Override
	public void addImplementation() {
		_return(QString.fromStringConstant(CodeUtil2.strMultiply("?", ",", columnCount)));
		
	}

}
