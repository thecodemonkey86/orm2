package sunjava.core.instruction;

import codegen.CodeUtil;
import sunjava.core.AbstractJavaCls;
import sunjava.core.expression.Expression;
import util.CodeUtil2;

public class DoWhile extends InstructionBlock {
	Expression condition;
	
	public static DoWhile create() {
		return new DoWhile();
	}
	
	public DoWhile() {
	}
	
	public DoWhile setCondition(Expression c) {
		if (c==null) {
			throw new NullPointerException();
		}
		this.condition=c;
		return this;
	}

	public DoWhile addInstructions(Instruction... instructions) {
		for(Instruction i:instructions)
			this.instructions.add(i);
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder("do {\n");
		
		for(Instruction i:instructions) {
			CodeUtil.writeLine(sb,new Comment(CodeUtil2.traceComment(i.getStackTrace())));
			sb.append(i.toString())
				.append('\n');
		}
		sb.append("} while")
			.append(CodeUtil.parentheses(condition))
			.append(';');
		return sb.toString();
	}

	public boolean hasInstructions() {
		return instructions.size()>0;
	}
	
	@Override
	public void collectImports(AbstractJavaCls cls) {
		condition.collectImports(cls);
		super.collectImports(cls);
	} 
}
