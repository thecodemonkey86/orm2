package cpp.lib;

import cpp.Types;
import cpp.cls.Cls;

public class ClsQVariant extends Cls{
	
	public static final String isNull = "isNull";

	public ClsQVariant() {
		super("QVariant");
		addMethod(new LibMethod(Types.QString, "toString"));
		addMethod(new LibMethod(Types.QDate, "toDate"));
		addMethod(new LibMethod(Types.QDate, "toTime"));
		addMethod(new LibMethod(Types.QDateTime, "toDateTime"));
		addMethod(new LibMethod(Types.Double, "toDouble"));
		addMethod(new LibMethod(Types.QByteArray, "toByteArray"));
		addMethod(new LibMethod(Types.Bool, "toBool"));
		addMethod(new LibMethod(Types.Int, "toInt"));
		addMethod(new LibMethod(Types.Short, "toShort"));
		addMethod(new LibMethod(Types.Bool, isNull));
		addMethod(new LibMethod(Types.LongLong, "toLongLong"));
	}

}
