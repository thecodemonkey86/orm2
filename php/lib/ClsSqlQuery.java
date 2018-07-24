package php.lib;

import php.core.PhpCls;

public class ClsSqlQuery extends PhpCls{

	public static final String query = "query";
	public static final String insertInto = "insertInto";
	public static final String insertMultiRow = "insertMultiRow";
	public static final String update = "update";
	public static final String deleteFrom = "deleteFrom";
	public static final String setValue = "setValue";
	public static final String execute = "execute";
	public static final String where = "where";
	public static final String beginTransaction = "beginTransaction";
	public static final String commitTransaction = "commitTransaction";
	public static final String rollbackTransaction = "rollbackTransaction";
	public static final String setAutoCommit = "setAutoCommit";
	public static final String onConflictDoNothing = "onConflictDoNothing";
	public static final String addInsertRow = "addInsertRow";
	public static final String addInsertRawExpression = "addInsertRawExpression";

	public ClsSqlQuery() {
		super("SqlQuery","Sql\\Query");
		
		addMethod(new LibMethod(this, "andWhereIn"));
		addMethod(new LibMethod(this, "whereIn"));
		addMethod(new LibMethod(this, insertInto));
		addMethod(new LibMethod(this, deleteFrom));
		addMethod(new LibMethod(this, setValue));
		addMethod(new LibMethod(this, execute));
		addMethod(new LibMethod(this, update));
		addMethod(new LibMethod(this, beginTransaction));
		addMethod(new LibMethod(this, commitTransaction));
		addMethod(new LibMethod(this, rollbackTransaction));
		addMethod(new LibMethod(this, setAutoCommit));
		addMethod(new LibMethod(this, insertMultiRow));
		addMethod(new LibMethod(this, addInsertRow));
		addMethod(new LibMethod(this, onConflictDoNothing));
		addMethod(new LibMethod(this, addInsertRawExpression));
		setAbstract(true);
	}

}
