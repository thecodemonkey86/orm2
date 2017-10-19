package php.lib;

import php.Types;
import php.cls.PhpCls;

public class ClsMysqliResult extends PhpCls {

	public static final String fetch_assoc = "fetch_assoc";
	
	public ClsMysqliResult() {
		super("mysqli_result", "\\");
		addMethod(new LibMethod(Types.array(Types.Mixed), fetch_assoc));
	}

}
