package cpp.lib;

import cpp.CoreTypes;
import cpp.core.Cls;

public class ClsQSqlRecord extends Cls {

	public static final String value = "value"; 
	
	public ClsQSqlRecord() {
		super("QSqlRecord");
		addMethod(new LibMethod(CoreTypes.QVariant, value) );
	}

}
