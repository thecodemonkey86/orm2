package php.core.instruction;

import codegen.CodeUtil;
import util.CodeUtil2;

public class DefaultCaseBlock extends InstructionBlock {
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		CodeUtil2.writeLine(sb, CodeUtil.sp("default:"));
		for(Instruction i:instructions) {
			if(InstructionBlock.enableStacktrace)
				CodeUtil2.writeLine(sb,new Comment(CodeUtil2.traceComment(i.getStackTrace())));
			sb.append(i);
			sb.append(CodeUtil2.NEWLINE);
		}
		return sb.toString();
	}
}
