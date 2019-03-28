package cpp.core.expression;

import cpp.Types;
import cpp.lib.LibOperator;

public class OrOperator extends LibOperator{

	public static final OrOperator INSTANCE = new OrOperator();
	
	private OrOperator() {
		super("||", Types.Bool, false);
	}



}
