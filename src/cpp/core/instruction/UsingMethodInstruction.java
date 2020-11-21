package cpp.core.instruction;

import cpp.core.Method;
import util.CodeUtil2;

public class UsingMethodInstruction extends SemicolonTerminatedInstruction{

	public UsingMethodInstruction(Method m) {
		super(CodeUtil2.sp("public:","using",m.getParent().toDeclarationString()+"::"+m.getName()));
	}

}
