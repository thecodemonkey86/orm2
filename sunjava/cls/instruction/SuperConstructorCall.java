package sunjava.cls.instruction;

import codegen.CodeUtil;
import generate.CodeUtil2;
import sunjava.cls.expression.Expression;

public class SuperConstructorCall extends ScClosedInstruction{

	public SuperConstructorCall(Expression... args) {
		super("super"+CodeUtil.parentheses(CodeUtil2.commaSep((Object[] )args)));
	}
	
}
