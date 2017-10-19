package php.cls.expression;

import php.Types;
import php.lib.LibOperator;

public class OrOperator extends LibOperator{

	public static final OrOperator INSTANCE = new OrOperator();
	
	private OrOperator() {
		super("||", Types.Bool, false);
	}



}
