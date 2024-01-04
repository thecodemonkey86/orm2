package php.lib;

import php.core.CoreTypes;
import php.core.Type;
import php.core.Types;
import php.core.expression.Expression;

public class ClsDateTime extends PhpLibCls{

	public static final String format = "format";
	public static final String getTimestamp = "getTimestamp";
	public static final Expression ISO8601 = new Expression() {
		
		@Override
		public String toString() {
			return "DateTimeInterface::ISO8601_EXPANDED";
		}
		
		@Override
		public Type getType() {
			return CoreTypes.String;
		}
	};
	
	public ClsDateTime() {
		super("DateTime");
		addMethod(new LibMethod(Types.String, format));
		addMethod(new LibMethod(Types.Int, getTimestamp));
	}

}
