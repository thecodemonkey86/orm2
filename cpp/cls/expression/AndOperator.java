package cpp.cls.expression;

import cpp.Types;
import cpp.lib.LibOperator;

public class AndOperator extends LibOperator{

	public static final AndOperator INSTANCE = new AndOperator();
	
	private AndOperator() {
		super("&&", Types.Bool, false);
	}



}
