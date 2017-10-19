package sunjava.cls.instruction;

import generate.CodeUtil2;
import sunjava.cls.AbstractJavaCls;
import sunjava.cls.expression.Expression;
import sunjava.cls.expression.Var;

public class DeclareInstruction extends Instruction {
	
	protected Var var;
	protected Expression init;
	
	public DeclareInstruction(Var var, Expression init) {
		this.var = var;
		this.init = init;
	}
	
	@Override
	public String toString() {
		//return CodeUtil2.sp(var.getType().toUsageString(),var.getName(),init!=null?CodeUtil2.parentheses(init):"")+';';
		return CodeUtil2.sp(var.getType().toDeclarationString(),var.getName(),init!=null?" = " + init:"")+';';
	}
	
	public Var getVar() {
		return var;
	}
	
	@Override
	public void collectImports(AbstractJavaCls cls) {
		var.getType().collectImports(cls);
		if(init!=null)
			init.collectImports(cls);
	}
}
