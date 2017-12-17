package cpp.core.instruction;

import codegen.CodeUtil;
import cpp.core.expression.Expression;
import cpp.core.expression.Var;
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
		StringBuilder sb=new StringBuilder(CodeUtil2.sp("for",CodeUtil2.parentheses(CodeUtil.sp(var.toDeclarationString(),":",collection.toString()))));
		sb.append("{\n");
		for(Instruction i: instructions) {
			CodeUtil.writeLine(sb,new Comment(CodeUtil2.traceComment(i.getStackTrace())));
			sb.append(i.toString()).append('\n');
		}
		return sb.append('}').toString();
	}

	public Var getVar() {
		return var;
	}
}
