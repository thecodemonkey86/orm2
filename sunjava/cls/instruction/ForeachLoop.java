package sunjava.cls.instruction;

import codegen.CodeUtil;
import generate.CodeUtil2;
import sunjava.cls.AbstractJavaCls;
import sunjava.cls.expression.Expression;
import sunjava.cls.expression.Var;

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
	
	@Override
	public void collectImports(AbstractJavaCls cls) {
		collection.collectImports(cls);
		super.collectImports(cls);
	}
}
