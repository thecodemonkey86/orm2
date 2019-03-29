package cpp.lib;

import codegen.CodeUtil;
import cpp.core.LambdaExpression;
import cpp.core.expression.Expression;
import cpp.core.instruction.Instruction;

public class QObjectConnect extends Instruction {

	Expression sender, receiver;
	Object signal;
	Object slot;
	
	public QObjectConnect(Expression sender,Object signal,  Expression receiver, Object slot) {
		super();
		this.sender = sender;
		this.receiver = receiver;
		this.signal = signal;
		this.slot = slot;
	}
	
	public QObjectConnect(Expression sender, String signal,Expression receiver,  LambdaExpression slot) {
		super();
		this.sender = sender;
		this.receiver = receiver;
		this.signal = signal;
		this.slot = slot;
	}
	

	@Override
	public String toString() {

		return CodeUtil.sp("connect",CodeUtil.parentheses(CodeUtil.commaSep(sender, signal,receiver,slot)))+";";

	}
}
