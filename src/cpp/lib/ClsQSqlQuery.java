package cpp.lib;

import cpp.CoreTypes;
import cpp.core.Cls;
import cpp.core.RawPtr;
import cpp.core.UniquePtr;

public class ClsQSqlQuery extends Cls{

	public static final String record = "record";
	public static final String next = "next";
	
	public ClsQSqlQuery() {
		super("QSqlQuery");
		addMethod(new LibMethod(this, record) );
		addMethod(new LibMethod(CoreTypes.Bool, next) );
	}
	
	public static RawPtr rawPtr() {
		return new RawPtr(new ClsQSqlQuery());
	}

	public static UniquePtr uniquePtr() {
		return new UniquePtr(new ClsQSqlQuery());
	}


}
