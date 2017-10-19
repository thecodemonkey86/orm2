package php.cls.expression;

import php.Types;
import php.lib.LibOperator;

public class PhpStringPlusEqOperator extends LibOperator {

	
	public PhpStringPlusEqOperator() {
		super(".=",Types.String,false);
	}


}
