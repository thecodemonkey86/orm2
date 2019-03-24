package cpp.core.expression;

import cpp.Types;
import cpp.core.Operator;
import cpp.core.Param;

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
