package sunjava.core.instruction;

import sunjava.core.JavaString;
import sunjava.core.expression.BoolExpression;
import sunjava.core.expression.Expression;

public class Instructions {
	public static ReturnInstruction _return(Expression ex){
		return new ReturnInstruction(ex);
	}
	
	public static ReturnInstruction _return(String strConstant){
		return new ReturnInstruction(JavaString.stringConstant(strConstant));
	}
	
	public static ReturnInstruction _return(boolean b){
		return new ReturnInstruction(new BoolExpression(b));
	}
	
	public static ScClosedInstruction sc(Expression ex) {
		return new ScClosedInstruction(ex.toString());
	}

	public static Instruction _continue() {
		return new ContinueInstruction();
	}
	
}
