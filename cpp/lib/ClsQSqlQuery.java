package cpp.lib;

import cpp.Types;
import cpp.core.Cls;
import cpp.core.RawPtr;
import cpp.core.UniquePtr;

public class ClsQSqlQuery extends Cls{

	public ClsQSqlQuery() {
		super("QSqlQuery");
		addMethod(new LibMethod(new ClsQSqlRecord(), "record") );
		addMethod(new LibMethod(Types.Bool, "next") );
	}
	
	public static RawPtr rawPtr() {
		return new RawPtr(new ClsQSqlQuery());
	}

	public static UniquePtr uniquePtr() {
		return new UniquePtr(new ClsQSqlQuery());
	}

}
