package cpp.cls.expression;

import generate.CodeUtil2;
import cpp.cls.ConstRef;
import cpp.cls.Ref;
import cpp.cls.Type;

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
