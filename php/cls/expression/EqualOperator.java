package php.cls.expression;

import php.Types;
import php.cls.Operator;
import php.cls.Param;

public abstract class EqualOperator extends Operator {

	public EqualOperator(Param p) {
		super("===", Types.Bool, false);
		if (p != null) {
			addParam(p);
		}
		
	}

}
