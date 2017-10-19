package sunjava.cls.expression;

import codegen.CodeUtil;
import sunjava.Types;
import sunjava.cls.Type;
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
