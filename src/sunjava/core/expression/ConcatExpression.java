package sunjava.core.expression;

import java.util.List;

import sunjava.core.AbstractJavaCls;
import sunjava.core.Type;

public class ConcatExpression extends  Expression{
	protected Expression separator;
	protected List<Expression> expressions;
	
	
	
	public ConcatExpression(Expression separator, List<Expression> expressions) {
		super();
		this.separator = separator;
		this.expressions = expressions;
	}

	@Override
	public String toString() {
		if (expressions.size()>0) {
			String s=expressions.get(0).toString();
			for(int i=1;i<expressions.size();i++) {
				s+=" + " +separator.toString() +" + "+ expressions.get(i);
			}
			return s;
		}
		return "";
	}
	
	@Override
	public Type getType() {
		return expressions.get(0).getType();
	}
	
	@Override
	public void collectImports(AbstractJavaCls cls) {
		separator.collectImports(cls);
		for(Expression e : expressions) {
			e.collectImports(cls);
		}
	}

}
