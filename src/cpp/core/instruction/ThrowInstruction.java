package cpp.core.instruction;

import codegen.CodeUtil;
import cpp.core.expression.CreateObjectExpression;
import cpp.core.expression.Expression;
import cpp.lib.ClsQtException;

public class ThrowInstruction extends SemicolonTerminatedInstruction{

	public ThrowInstruction(Expression expression) {
		super(CodeUtil.sp("throw",expression));
	}
	
	public ThrowInstruction(ClsQtException exc, Expression ... args) {
		super(CodeUtil.sp("throw", new CreateObjectExpression(exc,args)));
	}

}