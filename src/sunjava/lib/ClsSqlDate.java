package sunjava.lib;

import sunjava.core.JavaCls;
import sunjava.core.Types;

public class ClsSqlDate extends JavaCls {

	public static final String METHOD_NAME_TO_LOCAL_DATE = "toLocalDate";
	
	public ClsSqlDate() {
		super("Date", "java.sql");
		addMethod(new LibMethod(Types.LocalDate, METHOD_NAME_TO_LOCAL_DATE));
	}

}
