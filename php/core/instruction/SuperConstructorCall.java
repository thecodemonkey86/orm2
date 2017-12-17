package php.core.instruction;

import codegen.CodeUtil;
import php.core.expression.Expression;
import util.CodeUtil2;

public class SuperConstructorCall extends ScClosedInstruction{

	public SuperConstructorCall(Expression... args) {
		super("parent::__construct"+CodeUtil.parentheses(CodeUtil2.commaSep((Object[] )args)));
	}
	
}
