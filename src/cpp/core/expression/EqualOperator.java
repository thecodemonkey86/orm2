package cpp.core.expression;

import cpp.CoreTypes;
import cpp.core.Operator;
import cpp.core.Param;

public abstract class EqualOperator extends Operator {

	public EqualOperator(Param p) {
		super("==", CoreTypes.Bool, false);
		addParam(p);
		setConstQualifier(true);
	}

}
