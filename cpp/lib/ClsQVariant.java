package cpp.lib;

import cpp.Types;
import cpp.core.Cls;
import cpp.core.TplSymbol;
import cpp.core.Type;
import cpp.core.method.TplMethod;

public class ClsQVariant extends Cls{
	
	public static final String isNull = "isNull";
	public static final String value = "value";

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
		addMethodTemplate(new LibMethodTemplate(new TplSymbol("T"), value) {

			@Override
			public TplMethod getConcreteMethod(Type... types) {
				return new LibTplMethod(LibTplMethod.Public, types[0], value,types);
			}
			
		});
	}

}
