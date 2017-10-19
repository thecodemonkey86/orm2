package php.cls.expression;

import php.Types;
import php.lib.LibOperator;

public class AndOperator extends LibOperator{

	public static final AndOperator INSTANCE = new AndOperator();
	
	private AndOperator() {
		super("&&", Types.Bool, false);
	}



}
