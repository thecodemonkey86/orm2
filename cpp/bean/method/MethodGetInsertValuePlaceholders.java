package cpp.bean.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.QString;
import util.CodeUtil2;

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
