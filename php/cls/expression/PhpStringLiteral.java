package php.cls.expression;

import codegen.CodeUtil;
import generate.CodeUtil2;
import php.Types;
import php.cls.Type;
import util.StringUtil;

public class PhpStringLiteral extends Expression {

	protected String literal;
	protected boolean doubleQuote;
	public PhpStringLiteral(String literal) {
		this(literal,false);
	}
	
	public PhpStringLiteral(String literal, boolean doubleQuote) {
		super();
		this.literal = literal;
	}

	public PhpStringLiteral(char c) {
		this.literal = Character.toString(c);
	}

	@Override
	public Type getType() {
		return Types.String;
	}

	@Override
	public String toString() {
		String s = StringUtil.replaceAll(literal,"\"", "\\\"");
		return doubleQuote ? CodeUtil.quote(s) : CodeUtil2.singleQuote(s);
	}

	public String getOriginalString() {
		return literal;
	}

}
