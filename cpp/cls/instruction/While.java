package cpp.cls.instruction;

import codegen.CodeUtil;
import cpp.cls.expression.Expression;
import cpp.cls.expression.Var;

public class While extends InstructionBlock{
	Expression condition;
	
	public static While create() {
		return new While();
	}
	
	public While() {
	}
	
	public While setCondition(Expression c) {
		this.condition=c;
		return this;
	}

	public While addInstructions(Instruction... instructions) {
		for(Instruction i:instructions)
			this.instructions.add(i);
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder("while ");
		sb.append(CodeUtil.parentheses(condition));
		sb.append("{\n");
		for(Instruction i:instructions) {
			sb.append(i);
			sb.append('\n');
		}
		sb.append("}");
		return sb.toString();
	}
	
	public Expression getCondition() {
		return condition;
	}

	public Instruction getLastInstruction() {
		return instructions.get(instructions.size()-1);
	}

	public Var findVar(String name) {
		for(Instruction i:instructions) {
			if (i instanceof DeclareInstruction) {
				if (((DeclareInstruction) i).getVar().getName().equals(name))
					return ((DeclareInstruction) i).getVar();
			}
		}
		throw new RuntimeException("no such var");
	}
}
