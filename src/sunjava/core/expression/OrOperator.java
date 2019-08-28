package sunjava.core.expression;

import sunjava.core.Types;
import sunjava.lib.LibOperator;

public class OrOperator extends LibOperator{

	public static final OrOperator INSTANCE = new OrOperator();
	
	private OrOperator() {
		super("||", Types.Bool, false);
	}



}
