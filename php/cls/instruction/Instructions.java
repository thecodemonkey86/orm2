package php.cls.instruction;

import php.cls.expression.BoolExpression;
import php.cls.expression.Expression;
import php.cls.expression.PhpStringLiteral;


public class Instructions {
	public static ReturnInstruction _return(Expression ex){
		return new ReturnInstruction(ex);
	}
	
	public static ReturnInstruction _return(String strConstant){
		return new ReturnInstruction(new PhpStringLiteral(strConstant));
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
