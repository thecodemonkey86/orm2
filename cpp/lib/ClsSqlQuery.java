package cpp.lib;

import cpp.cls.Cls;

public class ClsSqlQuery extends Cls{


	public ClsSqlQuery() {
		super("SqlQuery");
		addMethod(new LibMethod(this.toRawPointer(), "select"));
		addMethod(new LibMethod(this.toRawPointer(), "from"));
		addMethod(new LibMethod(this.toRawPointer(), "leftJoin"));
		addMethod(new LibMethod(this.toRawPointer(), "join"));
		addMethod(new LibMethod(new ClsQSqlQuery().toSharedPtr(), "execQuery"));
		addMethod(new LibMethod(this.toRawPointer(), "where"));
		addMethod(new LibMethod(this.toRawPointer(), "andWhereIn"));
		addMethod(new LibMethod(this.toRawPointer(), "whereIn"));
	}

}
