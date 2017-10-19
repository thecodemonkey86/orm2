package cpp.lib;

import cpp.Types;
import cpp.cls.Cls;
import cpp.cls.RawPtr;
import cpp.cls.UniquePtr;

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
