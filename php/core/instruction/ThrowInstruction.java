package php.core.instruction;

import codegen.CodeUtil;
import php.core.expression.Expression;
import php.core.expression.NewOperator;
import php.lib.ClsException;

public class ThrowInstruction extends ScClosedInstruction{

	public ThrowInstruction(Expression expression) {
		super(CodeUtil.sp("throw",expression));
	}
	
	public ThrowInstruction(ClsException exc, Expression ... args) {
		super(CodeUtil.sp("throw", new NewOperator(exc,args)));
	}

}
