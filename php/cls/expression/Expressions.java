package php.cls.expression;

import java.util.Arrays;
import java.util.List;

import php.cls.Method;
import php.cls.Type;


public class Expressions {
	
	public static final Expression Null=new Expression() {
		
		@Override
		public Type getType() {
			return null;
		}
		
		@Override
		public Expression callMethod(Method m,Expression...args) {
			throw new UnsupportedOperationException("accessing null");
		}
		
		@Override
		public String toString() {
			return "null";
		}
	};

	@Deprecated
	public static Expression concat(Expression separator, List<Expression> expressions) {
		return new ConcatExpression(separator, expressions);
	}
	
	public static Expression not(Expression e) {
		if(e instanceof BinaryOperatorExpression) {
			BinaryOperatorExpression binop = (BinaryOperatorExpression) e;
			if(binop.getOp().getSymbol().equals("==")) {
				return new BinaryOperatorExpression(binop.getExpression(), Operators.NEQ, binop.getArg());
			}
			return new NotExpression(e);
		} else {
			return new NotExpression(e);
		}
	}

	public static Expression and(Expression ... expressions) {
		if (expressions.length==0) {
			throw new IllegalArgumentException();
		} else if (expressions.length==1) {
			return expressions[0];
		}
		return and(Arrays.asList(expressions));
	}
	public static Expression or(Expression ... expressions) {
		if (expressions.length==0) {
			throw new IllegalArgumentException();
		} else if (expressions.length==1) {
			return expressions[0];
		}
		return or(Arrays.asList(expressions));
	}
	
	public static Expression and(List<Expression> expressions) {
		if (expressions.size()==0) {
			throw new IllegalArgumentException();
		} else if (expressions.size()==1) {
			return expressions.get(0);
		}
		Expression res = new BinaryOperatorExpression(expressions.get(0), Operators.AND, expressions.get(1));
		for(int i=2;i<expressions.size();i++) {
			res = new BinaryOperatorExpression(res, Operators.AND, expressions.get(i));
		}
		return res;

	}
	public static Expression or(List<Expression> expressions) {
		if (expressions.size()==0) {
			throw new IllegalArgumentException();
		} else if (expressions.size()==1) {
			return expressions.get(0);
		}
		Expression res = new BinaryOperatorExpression(expressions.get(0), Operators.OR, expressions.get(1));
		for(int i=2;i<expressions.size();i++) {
			res = new BinaryOperatorExpression(res, Operators.OR, expressions.get(i));
		}
		return res;
		
	}
}
