package sunjava.core.expression;

import generate.CodeUtil2;
import sunjava.core.Type;
import sunjava.core.Types;

public class CharExpression extends Expression{
	protected char c;
	
	
	public CharExpression(char c) {
		this.c = c;
	}
	public static CharExpression fromChar(char c) {
		return new CharExpression(c);
	}
	@Override
	public String toString() {
		return CodeUtil2.singleQuote(c);
	}
	
	@Override
	public Type getType() {
		return Types.Char;
	}
	
}
