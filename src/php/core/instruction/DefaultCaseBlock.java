package php.core.instruction;

import codegen.CodeUtil;
import util.CodeUtil2;

public class DefaultCaseBlock extends InstructionBlock {
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		CodeUtil.writeLine(sb, CodeUtil.sp("default:"));
		for(Instruction i:instructions) {
			if(InstructionBlock.enableStacktrace)
				CodeUtil.writeLine(sb,new Comment(CodeUtil2.traceComment(i.getStackTrace())));
			sb.append(i);
			sb.append('\n');
		}
		return sb.toString();
	}
}
