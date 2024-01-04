package cpp.core.expression;

import cpp.core.instruction.SemicolonTerminatedInstruction;
import util.CodeUtil2;

public class StdFunctionInvocation extends SemicolonTerminatedInstruction{
	public StdFunctionInvocation(Var f, Expression...args) {
		super(f.getName()+CodeUtil2.parentheses(CodeUtil2.commaSep((Object[])args)));
	}
}
