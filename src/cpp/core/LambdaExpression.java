package cpp.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import codegen.CodeUtil;
import cpp.core.expression.Expression;
import cpp.core.instruction.Comment;
import cpp.core.instruction.Instruction;
import cpp.core.instruction.InstructionBlock;
import util.CodeUtil2;

public class LambdaExpression extends InstructionBlock{
	List<Expression> capture;
	List<Expression> arguments;
	
	public LambdaExpression() {
		capture = new ArrayList<>();
		arguments = new ArrayList<>();
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
		StringBuilder sb = new StringBuilder( CodeUtil.sp(CodeUtil.brackets(CodeUtil.commaSep(capture)) ,CodeUtil.parentheses(CodeUtil.commaSep(arguments)),"{\n"));
		
		for(Instruction i : instructions) {
			if(Instruction.isStackTraceEnabled())
				CodeUtil.writeLine(sb,new Comment(CodeUtil2.traceComment(i.getStackTrace())));
			CodeUtil.writeLine(sb,i);
		}
		sb.append("}");
		return sb.toString();
	}
}
