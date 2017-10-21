package sunjava.core.instruction;

import codegen.CodeUtil;
import generate.CodeUtil2;
import sunjava.core.expression.Expression;

public class SuperConstructorCall extends ScClosedInstruction{

	public SuperConstructorCall(Expression... args) {
		super("super"+CodeUtil.parentheses(CodeUtil2.commaSep((Object[] )args)));
	}
	
}
