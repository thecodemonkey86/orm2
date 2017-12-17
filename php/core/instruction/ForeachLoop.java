package php.core.instruction;

import codegen.CodeUtil;
import php.core.expression.Expression;
import php.core.expression.Var;
import util.CodeUtil2;

public class ForeachLoop extends InstructionBlock{
	
	protected Var var;
	protected Expression collection;
	
	public ForeachLoop(Var var, Expression collection) {
		this.var = var;
		this.collection = collection;
	}
	
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder(CodeUtil2.sp("foreach",CodeUtil2.parentheses(CodeUtil.sp(collection.toString(),"as",var.getUsageString()))));
		sb.append("{\n");
		for(Instruction i: instructions) {
			if(enableStacktrace)
				CodeUtil.writeLine(sb,new Comment(CodeUtil2.traceComment(i.getStackTrace())));
			sb.append(i.toString()).append('\n');
		}
		return sb.append('}').toString();
	}

	public Var getVar() {
		return var;
	}
	
}
