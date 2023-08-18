package cpp.lib;

import cpp.Types;
import cpp.core.Cls;

public class ClsQByteArray extends Cls{

	public static String isEmpty="isEmpty";
	
	public ClsQByteArray() {
		super("QByteArray");
		addMethod(new LibMethod(Types.Bool, isEmpty));
	}

}
