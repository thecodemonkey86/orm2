package sunjava.cls.instruction;

import generate.CodeUtil2;
import sunjava.cls.AbstractJavaCls;
import sunjava.cls.expression.Expression;

public class AssignInstruction extends Instruction {
	
	protected Expression assign;
	protected Expression value;
	
	public AssignInstruction(Expression assign, Expression value) {
		this.assign = assign;
		this.value = value;
	}
	
	@Override
	public String toString() {
		return CodeUtil2.sp(assign.getWriteAccessString(),'=',value)+';';
	}
	
	@Override
	public void collectImports(AbstractJavaCls cls) {
		assign.collectImports(cls);
		value.collectImports(cls);
	}
	
	
}
