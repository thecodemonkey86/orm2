package cpp.lib;

import codegen.CodeUtil;
import cpp.core.LambdaExpression;
import cpp.core.expression.Expression;
import cpp.core.instruction.Instruction;

public class QObjectConnect extends Instruction {

	Expression sender, receiver;
	Object signal;
	Object slot;
	boolean isStatic;
	
	public QObjectConnect(Expression sender,Object signal,  Expression receiver, Object slot,boolean isStatic) {
		super();
		this.sender = sender;
		this.receiver = receiver;
		this.signal = signal;
		this.slot = slot;
		this.isStatic=isStatic;
	}
	
	public QObjectConnect(Expression sender, String signal,Expression receiver,  LambdaExpression slot,boolean isStatic) {
		super();
		this.sender = sender;
		this.receiver = receiver;
		this.signal = signal;
		this.slot = slot;
		this.isStatic=isStatic;
	}
	

	@Override
	public String toString() {

		return CodeUtil.sp(isStatic?"QObject::":null, "connect",CodeUtil.parentheses(CodeUtil.commaSep(sender, signal,receiver,slot)))+";";

	}
}
