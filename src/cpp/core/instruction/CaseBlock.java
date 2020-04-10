package cpp.core.instruction;

import java.util.ArrayList;
import java.util.List;

import codegen.CodeUtil;
import cpp.core.expression.Expression;
import util.CodeUtil2;

public class CaseBlock extends InstructionBlock{
	List<Expression> conditions;

	public CaseBlock() {
		this.conditions = new ArrayList<>();
	}
	
	public CaseBlock(List<Expression> conditions) {
		this.conditions = conditions;
	}
	
	void addCondition(Expression e) {
		this.conditions.add(e);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Expression cond : conditions) {
			CodeUtil.writeLine(sb, CodeUtil.sp("case",cond+":"));
		}
		for(Instruction i:instructions) {
			if(InstructionBlock.isStackTraceEnabled())
				CodeUtil.writeLine(sb,new Comment(CodeUtil2.traceComment(i.getStackTrace())));
			sb.append(i);
			sb.append('\n');
		}
		return sb.toString();
	}
}
