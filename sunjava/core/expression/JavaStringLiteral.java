package sunjava.core.expression;

import codegen.CodeUtil;
import sunjava.core.Type;
import sunjava.core.Types;
import util.StringUtil;

public class JavaStringLiteral extends Expression {

	protected String literal;
	
	public JavaStringLiteral(String literal) {
		super();
		this.literal = literal;
	}

	@Override
	public Type getType() {
		return Types.String;
	}

	@Override
	public String toString() {
		return CodeUtil.quote(StringUtil.replaceAll(literal,"\"", "\\\""));
	}

	public String getOriginalString() {
		return literal;
	}

}
