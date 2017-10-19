package sunjava.cls.expression;

import sunjava.Types;
import sunjava.lib.LibOperator;

public class AndOperator extends LibOperator{

	public static final AndOperator INSTANCE = new AndOperator();
	
	private AndOperator() {
		super("&&", Types.Bool, false);
	}



}
