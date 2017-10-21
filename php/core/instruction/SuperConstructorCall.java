package php.core.instruction;

import codegen.CodeUtil;
import generate.CodeUtil2;
import php.core.expression.Expression;

public class SuperConstructorCall extends ScClosedInstruction{

	public SuperConstructorCall(Expression... args) {
		super("parent::__construct"+CodeUtil.parentheses(CodeUtil2.commaSep((Object[] )args)));
	}
	
}
