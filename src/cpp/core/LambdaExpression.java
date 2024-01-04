package cpp.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import codegen.CodeUtil;
import cpp.core.expression.Expression;
import cpp.core.expression.Var;
import cpp.core.instruction.Comment;
import cpp.core.instruction.IfBlock;
import cpp.core.instruction.Instruction;
import cpp.core.instruction.InstructionBlock;
import util.CodeUtil2;

public class LambdaExpression extends Expression{
	List<Expression> capture;
	List<Expression> arguments;
	InstructionBlock instructions;
	
	public LambdaExpression() {
		capture = new ArrayList<>();
		arguments = new ArrayList<>();
		instructions = new InstructionBlock();
	}
	
	public LambdaExpression setCapture(Expression... capture) {
		this.capture.addAll(Arrays.asList(capture));
		return this;
	}
	
	public LambdaExpression setArguments(Expression... arguments) {
		this.arguments.addAll(Arrays.asList(arguments));
		return this;
	}
	
	public LambdaExpression setCapture(List<Expression> capture) {
		this.capture = capture;
		return this;
	}
	
	public LambdaExpression setArguments(List<Expression> arguments) {
		this.arguments = arguments;
		return this;
	}
	
	@Override
	public String toString() {
		ArrayList<String> capture = new ArrayList<>(this.capture.size());
		for (Expression expression : this.capture) {
			capture.add(expression.getReadAccessString());
		}
		String[] arguments = new String[this.arguments.size()];
		for(int i=0;i<arguments.length;i++) {
			arguments[i] = CodeUtil2.sp(this.arguments.get(i).getType().toDeclarationString(),this.arguments.get(i).getReadAccessString());
		}
		
		StringBuilder sb = new StringBuilder( CodeUtil.sp(CodeUtil.brackets(CodeUtil.commaSep(capture)) ,CodeUtil.parentheses(CodeUtil.commaSep(arguments)),"{\n"));
		
		for(Instruction i : instructions) {
			if(Instruction.isStackTraceEnabled())
				CodeUtil.writeLine(sb,new Comment(CodeUtil2.traceComment(i.getStackTrace())));
			CodeUtil.writeLine(sb,i);
		}
		sb.append("}");
		return sb.toString();
	}
	
	public void addInstr(Instruction i) {
		instructions.addInstr(i);
	}

	@Override
	public Type getType() {
		return null;
	}

	public void _assign(Expression expr, Expression value) {
		instructions._assign(expr, value);
		
	}
	
	public IfBlock _if(Expression cond)	{
		return instructions._if(cond);
	}
	
	public Var _declare(Type type, String varName) {
		return instructions._declare(type, varName);
	}
		
	public Var _declare(Type type, String varName, Expression init ) {
		return instructions._declare(type, varName,init);
	}
	public Var _declareInitConstructor(Type type, String varName, Expression init ) {
		return instructions._declareInitConstructor(type, varName, init);
	}

	public Expression getArgument(int i) {
		return arguments.get(i);
	}

	public IfBlock _ifNot(Expression cond) {
		return instructions._ifNot(cond);
	}
}
