package cpp.lib;

import cpp.CoreTypes;
import cpp.core.Cls;
import cpp.core.RawPtr;
import cpp.core.UniquePtr;

public class ClsQSqlQuery extends Cls{

	public static String clear = "clear";
	
	public ClsQSqlQuery() {
		super("QSqlQuery");
		addMethod(new LibMethod(new ClsQSqlRecord(), "record") );
		addMethod(new LibMethod(CoreTypes.Bool, "next") );
		addMethod(new LibMethod(CoreTypes.Void, clear) );
	}
	
	public static RawPtr rawPtr() {
		return new RawPtr(new ClsQSqlQuery());
	}

	public static UniquePtr uniquePtr() {
		return new UniquePtr(new ClsQSqlQuery());
	}


}
