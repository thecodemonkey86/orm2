package sunjava.cls.expression;

import sunjava.Types;
import sunjava.cls.Operator;
import sunjava.cls.Param;

public abstract class EqualOperator extends Operator {

	public EqualOperator(Param p) {
		super("==", Types.Bool, false);
		if (p != null) {
			addParam(p);
		}
		
	}

}
