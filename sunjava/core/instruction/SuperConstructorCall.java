package sunjava.core.instruction;

import codegen.CodeUtil;
import sunjava.core.expression.Expression;
import util.CodeUtil2;

public class SuperConstructorCall extends ScClosedInstruction{

	public SuperConstructorCall(Expression... args) {
		super("super"+CodeUtil.parentheses(CodeUtil2.commaSep((Object[] )args)));
	}
	
}
