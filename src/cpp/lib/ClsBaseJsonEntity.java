package cpp.lib;

import cpp.Namespaces;
import cpp.NetworkTypes;
import cpp.Types;
import cpp.core.Attr;
import cpp.core.Cls;
import cpp.core.expression.Expressions;

public class ClsBaseJsonEntity extends Cls {

	public static final String network = "network";
	public static final String insert = "insert";
	public static final String loaded = "loaded";
	public static final String setInsertNew = "setInsertNew";
	public static final String setLoaded = "setLoaded";
	public static final String isLoaded = "isLoaded";
	public static final String primaryKeyModified = "primaryKeyModified";
	
	public ClsBaseJsonEntity() {
		super("JsonBaseEntity");
		addAttr(new Attr(Attr.Protected, NetworkTypes.QNetworkAccessManager.toRawPointer(), network, Expressions.Nullptr, true));
		addAttr(new Attr(Types.Bool, insert));
		addAttr(new Attr(Types.Bool, loaded));
		addAttr(new Attr(Types.Bool, primaryKeyModified));
		addMethod(new LibMethod(Types.Void, setInsertNew));
		addMethod(new LibMethod(Types.Void, setLoaded));
		addMethod(new LibMethod(Types.Bool, isLoaded));
		useNamespace = Namespaces.ORM2;
	}

}
