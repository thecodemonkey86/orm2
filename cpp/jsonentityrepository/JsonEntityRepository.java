package cpp.jsonentityrepository;

import cpp.NetworkTypes;
import cpp.core.Attr;
import cpp.core.Cls;
import cpp.core.expression.Expressions;

public class JsonEntityRepository extends Cls{
	public static final String CLSNAME = "JsonEntityRepository";
	public static final String network = "network";
	
	
	public JsonEntityRepository() {
		super(CLSNAME);
		addAttr(new Attr(Attr.Private, NetworkTypes.QNetworkAccessManager.toRawPointer(), network, Expressions.Nullptr, true));
	}

}
