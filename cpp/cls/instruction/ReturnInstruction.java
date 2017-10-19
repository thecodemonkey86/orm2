package cpp.cls.instruction;

import generate.CodeUtil2;
import cpp.cls.expression.Expression;

public class ReturnInstruction extends Instruction{
	
	Expression returnExpression;
	
	public ReturnInstruction(Expression returnExpression) {
		this.returnExpression = returnExpression;
	}
	
	@Override
	public String toString() {
		Expression e=null;
//		if (!(returnExpression.getType() instanceof SharedPtr) || !((SharedPtr)returnExpression.getType() ).isWeak()) {
			e = returnExpression ;
//		} else {
//			e = new WeakPtrLock(returnExpression);
//		}
		return CodeUtil2.sp("return", e.getReadAccessString())+';';
	}
}
