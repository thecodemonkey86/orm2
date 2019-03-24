package cpp.core.instruction;

import java.util.ArrayList;

import codegen.CodeUtil;
import cpp.core.QtSignal;
import cpp.core.expression.Expression;
import util.CodeUtil2;

public class QtEmit extends Instruction{
	QtSignal signal;
	Expression[] expressions;
	
	public QtEmit(QtSignal signal, Expression...expressions) {
		this.signal = signal; 
		this.expressions = expressions;
	}
	
	@Override
	public String toString() {
		ArrayList<String> params=new ArrayList<>();
		for(Expression e:expressions) {
			params.add(e.getReadAccessString());
		}
		
		return CodeUtil.sp("emit", signal.getName(), CodeUtil.parentheses(CodeUtil.commaSep(params)))+";";
	}
}
