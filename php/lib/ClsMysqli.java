package php.lib;

import php.core.Attr;
import php.core.PhpCls;
import php.core.Types;

public class ClsMysqli extends PhpCls {

	public static final String insert_id = "insert_id";
	
	public ClsMysqli() {
		super("mysqli", "\\");
		addAttr(new Attr(Types.Mixed, insert_id));
	}

}
