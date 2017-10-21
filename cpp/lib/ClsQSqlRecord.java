package cpp.lib;

import cpp.Types;
import cpp.core.Cls;

public class ClsQSqlRecord extends Cls {

	public ClsQSqlRecord() {
		super("QSqlRecord");
		addMethod(new LibMethod(Types.QVariant, "value") );
	}

}
