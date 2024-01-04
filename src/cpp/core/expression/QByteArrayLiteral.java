package cpp.core.expression;

import codegen.CodeUtil;
import cpp.CoreTypes;
import cpp.core.Type;

public class QByteArrayLiteral extends Expression{

	String str;
	
	public QByteArrayLiteral(String str) {
		this.str=str;
	}
	
	@Override
	public Type getType() {
		return CoreTypes.QByteArray;
	}

	@Override
	public String toString() {
		return "QByteArrayLiteral" + CodeUtil.parentheses(CodeUtil.quote(str));
	}

}
