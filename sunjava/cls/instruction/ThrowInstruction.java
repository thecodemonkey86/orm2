package sunjava.cls.instruction;

import codegen.CodeUtil;
import sunjava.cls.expression.Expression;
import sunjava.cls.expression.NewOperator;
import sunjava.lib.ClsException;

public class ThrowInstruction extends ScClosedInstruction{

	public ThrowInstruction(Expression expression) {
		super(CodeUtil.sp("throw",expression));
	}
	
	public ThrowInstruction(ClsException exc, Expression ... args) {
		super(CodeUtil.sp("throw", new NewOperator(exc,args)));
	}

}
