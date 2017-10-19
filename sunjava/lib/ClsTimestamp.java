package sunjava.lib;

import sunjava.Types;
import sunjava.cls.JavaCls;

public class ClsTimestamp extends JavaCls{

	public static final String METHOD_NAME_TO_INSTANT = "toInstant";

	public ClsTimestamp() {
		super("Timestamp", "java.sql");
		addMethod(new LibMethod(Types.Instant, METHOD_NAME_TO_INSTANT));
	}

}
