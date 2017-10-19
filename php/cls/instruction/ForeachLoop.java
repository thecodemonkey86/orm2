package php.cls.instruction;

import codegen.CodeUtil;
import generate.CodeUtil2;
import php.cls.AbstractPhpCls;
import php.cls.expression.Expression;
import php.cls.expression.Var;

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
