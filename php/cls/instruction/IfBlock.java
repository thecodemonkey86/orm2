package php.cls.instruction;

import generate.CodeUtil2;

import java.util.ArrayList;

import codegen.CodeUtil;
import php.cls.AbstractPhpCls;
import php.cls.expression.BinaryOperatorExpression;
import php.cls.expression.Expression;
import php.cls.expression.Operators;

public class IfBlock extends Instruction{
	Expression condition;
	InstructionBlock ifInstr;
	ArrayList<Expression> elseifCondition;
	ArrayList<InstructionBlock> elseifInstr;
	InstructionBlock elseInstr;
	
	public IfBlock() {
		this.ifInstr = new InstructionBlock();
	}
	
	
	public static IfBlock create() {
		return new IfBlock();
	}
	
	public IfBlock setCondition(Expression c) {
		this.condition=c;
		return this;
	}
	public IfBlock setIfInstr(Instruction... instr) {
		for(Instruction i:instr) {
			this.ifInstr.addInstr(i);	
		}
		return this;
	}
	public IfBlock addElseIf(Expression condition, Instruction... instr) {
		if (elseifInstr == null) {
			elseifCondition = new ArrayList<>();
			elseifInstr = new ArrayList<>();
		}
		this.elseifCondition.add(condition);
		InstructionBlock ib = new InstructionBlock();
		for(Instruction i:instr) {
			ib.addInstr(i);
		}
		
		this.elseifInstr.add(ib);
		return this;
	}
	public IfBlock setElseInstr(Instruction... instr) {
		if (elseInstr == null) {
			elseInstr = new InstructionBlock();
		}
		for(Instruction i:instr) {
			this.elseInstr.addInstr(i);	
		}
		return this;
	}
	
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder("if ");
		sb.append(CodeUtil.parentheses(condition.getUsageString()));
		sb.append("{\n");
		for(Instruction i:ifInstr) {
			if(InstructionBlock.enableStacktrace)
				CodeUtil.writeLine(sb,new Comment(CodeUtil2.traceComment(i.getStackTrace())));
			sb.append(i);
			sb.append('\n');
		}
		sb.append("}");
		if (elseifCondition!=null) {
			for (int i = 0; i < elseifCondition.size(); i++) {
				sb.append("else if");
				sb.append(CodeUtil.parentheses(elseifCondition.get(i).getUsageString()));
				InstructionBlock elifInstr = elseifInstr.get(i);
				sb.append("{\n");
				for(Instruction instr:elifInstr) {
					if(InstructionBlock.enableStacktrace)
					CodeUtil.writeLine(sb,new Comment(CodeUtil2.traceComment(instr.getStackTrace())));
					sb.append(instr);
					sb.append("\n");
				}
				sb.append("}");
			} 
			
		}
		if (elseInstr != null) {
			sb.append(" else {\n");
			for(Instruction instr:elseInstr) {
				if(InstructionBlock.enableStacktrace)
					CodeUtil.writeLine(sb,new Comment(CodeUtil2.traceComment(instr.getStackTrace())));
				sb.append(instr);
				sb.append("\n");
			}
			sb.append("}\n");
			
		} else {
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public IfBlock addIfInstr(Instruction i) {
		ifInstr.addInstr(i);
		return this;
	}

	public IfBlock addElseInstr(Instruction i) {
		if (elseInstr == null) {
			elseInstr = new InstructionBlock();
		}
		elseInstr.addInstr(i);
		return this;
	}

	public Expression getCondition() {
		return condition;
	}

	public boolean hasInstructions() {
		return !ifInstr.isEmpty();
	}
	
	public InstructionBlock thenBlock() {
		return ifInstr;
	}
	
	public InstructionBlock elseBlock() {
		if (elseInstr == null) {
			elseInstr = new InstructionBlock();
		}
		return elseInstr;
	}
	
	public ArrayList<InstructionBlock> getElseifInstr() {
		return elseifInstr;
	}
	
	public IfBlock _and(Expression e) {
		this.condition = new BinaryOperatorExpression(condition, Operators.AND, e);
		return this;
	}

	public void _break() {
		this.ifInstr.addInstr(new BreakInstruction());
	}
	
}
