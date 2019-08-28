package cpp.core.expression;

import codegen.CodeUtil;
import cpp.Types;
import cpp.core.Type;
import util.CodeUtil2;

public class QChar extends Expression{
	protected char c;
	
	
	public QChar(char c) {
		this.c = c;
	}
	public static QChar fromChar(char c) {
		return new QChar(c);
	}
	@Override
	public String toString() {
		return getType().getName()+CodeUtil.parentheses(CodeUtil2.singleQuote(c));
	}
	
	@Override
	public Type getType() {
		return Types.QChar;
	}
}
