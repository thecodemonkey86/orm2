package cpp.lib;

import cpp.CoreTypes;
import cpp.core.Cls;
import cpp.core.TplSymbol;
import cpp.core.Type;
import cpp.core.method.TplMethod;

public class ClsQVariant extends Cls{
	
	public static final String isNull = "isNull";
	public static final String value = "value";
//	public static final String toShort = "toShort";
	public static final String fromValue = "fromValue";

	public ClsQVariant() {
		super("QVariant");
		addMethod(new LibMethod(CoreTypes.QString, "toString"));
		addMethod(new LibMethod(CoreTypes.QStringList, "toStringList"));
		addMethod(new LibMethod(CoreTypes.QDate, "toDate"));
		addMethod(new LibMethod(CoreTypes.QDate, "toTime"));
		addMethod(new LibMethod(CoreTypes.QDateTime, "toDateTime"));
		addMethod(new LibMethod(CoreTypes.Double, "toDouble"));
		addMethod(new LibMethod(CoreTypes.QByteArray, "toByteArray"));
		addMethod(new LibMethod(CoreTypes.Bool, "toBool"));
		addMethod(new LibMethod(CoreTypes.Int, "toInt"));
//		addMethod(new LibMethod(CoreTypes.Short, toShort));
		addMethod(new LibMethod(CoreTypes.Bool, isNull));
		addMethod(new LibMethod(this, fromValue,true));
		addMethod(new LibMethod(CoreTypes.LongLong, "toLongLong"));
		addMethodTemplate(new LibMethodTemplate(new TplSymbol("T"), value,false) {

			@Override
			public TplMethod getConcreteMethodImpl(Type... types) {
				return new LibTplMethod(this,LibTplMethod.Public, types[0], value,types);
			}
			
		});
	}

}
