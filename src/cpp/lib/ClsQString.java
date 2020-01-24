package cpp.lib;

import cpp.core.Cls;
import cpp.core.QStringPlusEqOperator;

public class ClsQString extends Cls {

	public static final String arg = "arg";
	public static final String isNull = "isNull";
	public static final String isEmpty = "isEmpty";
	public static final String left = "left";
	public static final String trimmed = "trimmed";
	public static final String number = "number";
	
	public ClsQString() {
		super("QString");
		addOperator(new QStringPlusEqOperator());
		addMethod(new LibMethod(this, arg));
		addMethod(new LibMethod(this, "reserve"));
		addMethod(new LibMethod(this, "mid"));
		addMethod(new LibMethod(this, isEmpty));
		addMethod(new LibMethod(this, isNull));
		addMethod(new LibMethod(this, left));
		addMethod(new LibMethod(this, trimmed));
		addMethod(new LibMethod(this, number,true));
	}

}
