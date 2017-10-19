package php.cls.instruction;

import java.util.ArrayList;
import java.util.List;

import codegen.CodeUtil;
import php.cls.AbstractPhpCls;
import php.cls.expression.Var;
import php.lib.ClsException;

public class TryCatchBlock extends Instruction{
	protected InstructionBlock tryBlock;
	protected List<CatchBlock> catchBlocks;
	protected InstructionBlock finallyBlock;
		
	public TryCatchBlock() {
		tryBlock = new InstructionBlock();
		catchBlocks = new ArrayList<>();
	}
	
	public InstructionBlock getTryBlock() {
		return tryBlock;
	}
	
	public TryCatchBlock addTryInstructions(Instruction ...instructions) {
		for(Instruction i : instructions)
			tryBlock.addInstr(i);	
		return this;
	}
	public TryCatchBlock addFinallyInstructions(Instruction ...instructions) {
		if(finallyBlock == null) {
			finallyBlock = new InstructionBlock();
		}
		for(Instruction i : instructions)
			finallyBlock.addInstr(i);	
		return this;
	}
	public TryCatchBlock addCatch(Var excvar, Instruction ...instructions) {
		
		CatchBlock catchBlock = new CatchBlock(excvar);
		this.catchBlocks.add(catchBlock);
		
		for(Instruction i : instructions) {
			catchBlock.addInstr(i);
		}
		return this;
	}
	public TryCatchBlock addCatch(ClsException exc, String varname, Instruction ...instructions) {
		
		CatchBlock catchBlock = new CatchBlock(new Var(exc, varname));
		this.catchBlocks.add(catchBlock);
		
		for(Instruction i : instructions) {
			catchBlock.addInstr(i);
		}
		return this;
	}
	
	public CatchBlock getLastCatchBlock() {
		return catchBlocks.get(catchBlocks.size()-1);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		CodeUtil.writeLine(sb, "try {");
		for(Instruction i : tryBlock) {
			CodeUtil.writeLine(sb, i);
		}
		sb.append("}");
		for(CatchBlock c : catchBlocks) {
			CodeUtil.writeLine(sb, CodeUtil.sp("catch", CodeUtil.parentheses(c.getVar().toDeclarationString()),"{" ));
			
			for(Instruction i : c) {
				CodeUtil.writeLine(sb, i);
			}
			sb.append("}");
			
		}
		if (finallyBlock != null && !finallyBlock.isEmpty()) {
			sb.append("finally {\n");
			for(Instruction i : finallyBlock) {
				CodeUtil.writeLine(sb, i);
			}
			CodeUtil.writeLine(sb, "}");
		} else {
			sb.append("\n");
		}
		
		return sb.toString();
	}
	
}
