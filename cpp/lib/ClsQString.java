package cpp.lib;

import cpp.core.Cls;
import cpp.core.QStringPlusEqOperator;

public class ClsQString extends Cls {

	public static final String arg = "arg";
	public static final String isNull = "isNull";
	public static final String isEmpty = "isEmpty";
	
	public ClsQString() {
		super("QString");
		addOperator(new QStringPlusEqOperator());
		addMethod(new LibMethod(this, arg));
		addMethod(new LibMethod(this, "reserve"));
		addMethod(new LibMethod(this, "mid"));
		addMethod(new LibMethod(this, isEmpty));
		addMethod(new LibMethod(this, isNull));
	}

}
