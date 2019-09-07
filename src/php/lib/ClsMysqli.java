package php.lib;

import php.core.Attr;
import php.core.PhpCls;
import php.core.Types;

public class ClsMysqli extends PhpCls {

	public static final String insert_id = "insert_id";
	public static final String begin_transaction = "begin_transaction";
	public static final String commit = "commit";
	public static final String rollback = "rollback";
	
	public ClsMysqli() {
		super("mysqli", "\\");
		addAttr(new Attr(Types.Mixed, insert_id));
		addMethod(new LibMethod(Types.Int, insert_id));
		addMethod(new LibMethod(Types.Bool, begin_transaction));
		addMethod(new LibMethod(Types.Bool, commit));
		addMethod(new LibMethod(Types.Bool, rollback));
	}

}
