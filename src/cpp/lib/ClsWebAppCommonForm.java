package cpp.lib;

import cpp.Types;
import cpp.core.Cls;

public class ClsWebAppCommonForm extends Cls{

	public ClsWebAppCommonForm() {
		super("Form");
		addMethod(new LibMethod(Types.QString, "stringValue"));
		addMethod(new LibMethod(Types.Int, "intValue"));
		addMethod(new LibMethod(Types.Int64, "int64Value"));
		addMethod(new LibMethod(Types.QDate, "dateValue"));
		addMethod(new LibMethod(Types.Double, "doubleValue"));
		addMethod(new LibMethod(Types.Bool, "boolValue"));
		addMethod(new LibMethod(Types.QDate, "dateValue"));
		addMethod(new LibMethod(Types.QDateTime, "dateTimeValue"));
	}

}
