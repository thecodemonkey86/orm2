package cpp.core.expression;

import codegen.CodeUtil;
import cpp.Types;
import cpp.core.Type;

public class CStringLiteral extends Expression {
	private final String str;

	public CStringLiteral(String str) {
		this.str = str;
	}

	@Override
	public String toString() {
		return CodeUtil.quote(str);
	}

	@Override
	public Type getType() {
		return Types.ConstCharPtr;
	}
}