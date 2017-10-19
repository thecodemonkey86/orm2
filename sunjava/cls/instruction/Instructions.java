package sunjava.cls.instruction;

import sunjava.cls.JavaString;
import sunjava.cls.expression.BoolExpression;
import sunjava.cls.expression.Expression;

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
