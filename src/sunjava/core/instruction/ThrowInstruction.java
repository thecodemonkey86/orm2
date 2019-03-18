package sunjava.core.instruction;

import codegen.CodeUtil;
import sunjava.core.expression.Expression;
import sunjava.core.expression.NewOperator;
import sunjava.lib.ClsException;

public class ThrowInstruction extends ScClosedInstruction{

	public ThrowInstruction(Expression expression) {
		super(CodeUtil.sp("throw",expression));
	}
	
	public ThrowInstruction(ClsException exc, Expression ... args) {
		super(CodeUtil.sp("throw", new NewOperator(exc,args)));
	}

}
