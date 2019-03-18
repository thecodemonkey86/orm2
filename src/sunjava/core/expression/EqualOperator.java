package sunjava.core.expression;

import sunjava.core.Operator;
import sunjava.core.Param;
import sunjava.core.Types;

public abstract class EqualOperator extends Operator {

	public EqualOperator(Param p) {
		super("==", Types.Bool, false);
		if (p != null) {
			addParam(p);
		}
		
	}

}
