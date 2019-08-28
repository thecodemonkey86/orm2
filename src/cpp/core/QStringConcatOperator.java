package cpp.core;

import cpp.CoreTypes;
import cpp.lib.LibOperator;

public class QStringConcatOperator extends LibOperator {

	public QStringConcatOperator() {
		super("+", CoreTypes.QString, false);
	}

}
