package php.core.expression;

import php.core.Types;
import php.lib.LibOperator;

public class AndOperator extends LibOperator{

	public static final AndOperator INSTANCE = new AndOperator();
	
	private AndOperator() {
		super("&&", Types.Bool, false);
	}



}
