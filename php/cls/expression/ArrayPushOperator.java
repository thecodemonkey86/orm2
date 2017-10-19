package php.cls.expression;

import php.Types;
import php.lib.LibOperator;

public class ArrayPushOperator extends LibOperator{

	public static final ArrayPushOperator INSTANCE = new ArrayPushOperator();
	
	private ArrayPushOperator() {
		super("[] =", Types.Void, false);
	}



}
