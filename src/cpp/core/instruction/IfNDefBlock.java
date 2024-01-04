package cpp.core.instruction;

import codegen.CodeUtil;
import util.CodeUtil2;

public class IfNDefBlock extends InstructionBlock {
	String macro;
	
	public IfNDefBlock(String macro) {
		this.macro=macro;
	}
	
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder("#ifndef ");
		sb.append(macro).append("\r\n");
		
		for(Instruction i:instructions) {
			if(Instruction.isStackTraceEnabled())
				CodeUtil.writeLine(sb,new Comment(CodeUtil2.traceComment(i.getStackTrace())));
			sb.append(i.toString())
				.append("\r\n");
		}
		sb.append("#endif\r\n");
		return sb.toString();
	}
}
