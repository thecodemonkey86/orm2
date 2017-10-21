package php.core.expression;

import php.core.Operator;
import php.core.Param;
import php.core.Types;

public abstract class EqualOperator extends Operator {

	public EqualOperator(Param p) {
		super("===", Types.Bool, false);
		if (p != null) {
			addParam(p);
		}
		
	}

}
