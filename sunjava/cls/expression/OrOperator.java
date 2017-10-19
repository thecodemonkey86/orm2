package sunjava.cls.expression;

import sunjava.Types;
import sunjava.lib.LibOperator;

public class OrOperator extends LibOperator{

	public static final OrOperator INSTANCE = new OrOperator();
	
	private OrOperator() {
		super("||", Types.Bool, false);
	}



}
