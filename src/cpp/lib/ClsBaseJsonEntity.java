package cpp.lib;

import cpp.NetworkTypes;
import cpp.Types;
import cpp.core.Attr;
import cpp.core.Cls;
import cpp.core.expression.Expressions;

public class ClsBaseJsonEntity extends Cls {

	public static final String network = "network";
	public static final String insert = "insert";
	public static final String primaryKeyModified = "primaryKeyModified";
	
	public ClsBaseJsonEntity() {
		super("JsonBaseEntity");
		addAttr(new Attr(Attr.Protected, NetworkTypes.QNetworkAccessManager.toRawPointer(), network, Expressions.Nullptr, true));
		addAttr(new Attr(Types.Bool, insert));
		addAttr(new Attr(Types.Bool, primaryKeyModified));
	}

}
