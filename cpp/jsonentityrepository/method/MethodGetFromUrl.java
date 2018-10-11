package cpp.jsonentityrepository.method;

import cpp.NetworkTypes;
import cpp.Types;
import cpp.core.Attr;
import cpp.core.LambdaExpression;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.expression.Var;
import cpp.jsonentity.JsonEntity;
import cpp.jsonentityrepository.JsonEntityRepository;
import cpp.lib.ClsQNetworkAccessManager;
import cpp.lib.ClsQNetworkReply;
import cpp.lib.QObjectConnect;

public class MethodGetFromUrl extends Method {

	Param pUrl; 
	
	public MethodGetFromUrl(JsonEntity entity) {
		super(Public, entity.toSharedPtr(), "get"+entity.getName()+"FromUrl");
		setStatic(true);
		pUrl = addParam(new Param(Types.QString.toConstRef(), "url"));
	}

	@Override
	public void addImplementation() {
		Attr aNetwork = parent.getStaticAttribute(JsonEntityRepository.network);
		Var req = _declareInitConstructor(NetworkTypes.QNetworkRequest, "req", pUrl);
		Var reply = _declare(NetworkTypes.QNetworkReply.toRawPointer(), "reply", aNetwork.callMethod(ClsQNetworkAccessManager.get, req));
		
		LambdaExpression lambdaExpression = new LambdaExpression();
		lambdaExpression.addInstr(reply.callMethodInstruction(ClsQNetworkReply.deleteLater));
		addInstr(new QObjectConnect(reply,"&QNetworkReply::finished",aNetwork,
				lambdaExpression.setCapture(reply)));
	    		
	    		
	    		
	    
	}

}
