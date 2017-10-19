package cpp.cls.expression;

import cpp.Types;
import cpp.cls.Operator;
import cpp.cls.Param;

public abstract class EqualOperator extends Operator {

	public EqualOperator(Param p) {
		super("==", Types.Bool, false);
		if (p == null) {
			System.out.println();
		}
		addParam(p);
		setConstQualifier(true);
	}

}
