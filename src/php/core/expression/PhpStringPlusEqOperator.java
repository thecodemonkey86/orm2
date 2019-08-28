package php.core.expression;

import php.core.Types;
import php.lib.LibOperator;

public class PhpStringPlusEqOperator extends LibOperator {

	
	public PhpStringPlusEqOperator() {
		super(".=",Types.String,false);
	}


}
