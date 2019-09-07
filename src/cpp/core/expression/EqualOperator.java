package cpp.core.expression;

import cpp.CoreTypes;
import cpp.core.Operator;
import cpp.core.Param;

public abstract class EqualOperator extends Operator {

	public EqualOperator(Param p) {
		super("==", CoreTypes.Bool, false);
		if (p == null) {
			System.out.println();
		}
		addParam(p);
		setConstQualifier(true);
	}

}
