package sunjava.lib;

import sunjava.core.JavaCls;
import sunjava.core.Types;

public class ClsTimestamp extends JavaCls{

	public static final String METHOD_NAME_TO_INSTANT = "toInstant";

	public ClsTimestamp() {
		super("Timestamp", "java.sql");
		addMethod(new LibMethod(Types.Instant, METHOD_NAME_TO_INSTANT));
	}

}
