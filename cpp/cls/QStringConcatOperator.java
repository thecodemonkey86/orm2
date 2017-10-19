package cpp.cls;

import cpp.Types;
import cpp.lib.LibOperator;

public class QStringConcatOperator extends LibOperator {

	public QStringConcatOperator() {
		super("+", Types.QString, false);
	}

}
