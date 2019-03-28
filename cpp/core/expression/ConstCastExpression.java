package cpp.core.expression;

import cpp.core.ConstRef;
import cpp.core.Ref;
import cpp.core.Type;
import util.CodeUtil2;

public class ConstCastExpression extends Expression {
	Expression e;
	
	public ConstCastExpression(Expression e) {
		this.e=e;
	}
	
	@Override
	public Type getType() {
		ConstRef cr=(ConstRef) e.getType();
		Ref r=new Ref(cr.getBase());
		return r;
	}

	@Override
	public String toString() {
		return "const_cast"+CodeUtil2.abr(getType().toUsageString())+CodeUtil2.parentheses(e.toString());
	}

}
