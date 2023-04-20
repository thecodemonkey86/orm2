package php.core.expression;

import codegen.CodeUtil;
import php.core.Type;
import php.core.Types;
import util.CodeUtil2;
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
		String s =  StringUtil.replaceAll(StringUtil.replaceAll(literal,"\"", "\\\""),"\'", "\\\'");
		return doubleQuote ? CodeUtil.quote(s) : CodeUtil2.singleQuote(s);
	}

	public String getOriginalString() {
		return literal;
	}

}
