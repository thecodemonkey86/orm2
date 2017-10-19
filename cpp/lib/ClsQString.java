package cpp.lib;

import cpp.cls.Cls;
import cpp.cls.QStringPlusEqOperator;

public class ClsQString extends Cls {

	public static final String arg = "arg";

	public ClsQString() {
		super("QString");
		addOperator(new QStringPlusEqOperator());
		addMethod(new LibMethod(this, arg));
		addMethod(new LibMethod(this, "reserve"));
		addMethod(new LibMethod(this, "mid"));
		addMethod(new LibMethod(this, "isEmpty"));
	}

}
