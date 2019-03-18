package sunjava.core.expression;

import sunjava.core.Types;
import sunjava.lib.LibOperator;

public class AndOperator extends LibOperator{

	public static final AndOperator INSTANCE = new AndOperator();
	
	private AndOperator() {
		super("&&", Types.Bool, false);
	}



}
