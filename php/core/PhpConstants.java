package php.core;

import php.core.expression.Expression;

public class PhpConstants {
	public static Expression IBASE_TEXT = new Expression() {
		
		@Override
		public String toString() {
			return "IBASE_TEXT";
		}
		
		@Override
		public Type getType() {
			return Types.Int;
		}
	};
}
