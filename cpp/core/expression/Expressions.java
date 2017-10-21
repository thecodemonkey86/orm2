package cpp.core.expression;

import java.util.Arrays;
import java.util.List;

import cpp.Types;
import cpp.core.Method;
import cpp.core.Type;


public class Expressions {
	public static StringConstant constCharPtr(String str) {
		return new StringConstant(str);
	}
	
	public static final Expression Nullptr=new Expression() {
		
		@Override
		public Type getType() {
			return Types.NullptrType;
		}
		
		@Override
		public Expression callMethod(Method m,Expression...args) {
			throw new UnsupportedOperationException("accessing nullptr");
		}
		
		@Override
		public String toString() {
			return "nullptr";
		}
	};
	
	@Deprecated
	public static Expression concat(Expression separator, List<Expression> expressions) {
		return new Expression() {
			
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
		};
	}
	
	public static Expression not(Expression e) {
		return new Expression() {
			
			@Override
			public String toString() {
				return "!" + e.toString();
			}
			
			@Override
			public Type getType() {
				return Types.Bool;
			}
		};
	}

	public static Expression and(Expression ... expressions) {
		if (expressions.length==0) {
			throw new IllegalArgumentException();
		} else if (expressions.length==1) {
			return expressions[0];
		}
		return and(Arrays.asList(expressions));
	}
	
	public static Expression and(List<Expression> expressions) {
		if (expressions.size()==0) {
			throw new IllegalArgumentException();
		} else if (expressions.size()==1) {
			return expressions.get(0);
		}
		Expression res = new BinaryOperatorExpression(expressions.get(0), Operators.AND, expressions.get(1));
		for(int i=3;i<expressions.size();i++) {
			res = new BinaryOperatorExpression(res, Operators.AND, expressions.get(i));
		}
		return res;
		
//		return new Expression() {
//			
//			@Override
//			public String toString() {
//				if (expressions.size()>0) {
//					String s=expressions.get(0).toString();
//					for(int i=1;i<expressions.size();i++) {
//						s+=" && "+ expressions.get(i);
//					}
//					return s;
//				}
//				return "";
//			}
//			
//			@Override
//			public Type getType() {
//				return Types.Bool;
//			}
//		};
	}
}
