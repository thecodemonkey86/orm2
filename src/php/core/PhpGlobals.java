package php.core;

import php.core.expression.Var;

public class PhpGlobals {
	public static final Var $_GET = new Var(Types.array(Types.Mixed), "_GET");
}
