package cpp.jsonentityrepository.method;

import cpp.CoreTypes;
import cpp.JsonTypes;
import cpp.NetworkTypes;
import cpp.core.Attr;
import cpp.core.LambdaExpression;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.StdFunctionInvocation;
import cpp.core.expression.Var;
import cpp.jsonentity.JsonEntity;
import cpp.jsonentityrepository.ClsJsonEntityRepository;
import cpp.lib.ClsQNetworkAccessManager;
import cpp.lib.ClsQNetworkReply;
import cpp.lib.ClsQNetworkRequest;
import cpp.lib.ClsStdFunction;
import cpp.lib.QObjectConnect;

public class MethodLoadOneFromUrl extends Method {

	Param pUrl,pCallback; 
	JsonEntity entity;
	
	public static String getMethodName(JsonEntity entity) {
		return "loadOne"+entity.getName()+"FromUrl";
	}
	
	public MethodLoadOneFromUrl(JsonEntity entity) {
		super(Public, CoreTypes.Void, getMethodName(entity));
		pUrl = addParam(new Param(NetworkTypes.QUrl.toConstRef(), "url"));
		pCallback = addParam(new Param(new ClsStdFunction(CoreTypes.Void, entity.toSharedPtr().toConstRef()), "callback"));
		this.entity = entity;
		setStatic(true); 
	}

	@Override
	public void addImplementation() {
		Attr aNetwork = parent.getStaticAttribute(ClsJsonEntityRepository.network);
		Var req = _declareInitConstructor(NetworkTypes.QNetworkRequest, "req", pUrl);
		addInstr(req.callMethodInstruction(ClsQNetworkRequest.setAttribute, NetworkTypes.QNetworkRequest.FollowRedirectsAttribute, BoolExpression.TRUE));
		Var reply = _declare(NetworkTypes.QNetworkReply.toRawPointer(), "reply", aNetwork.callMethod(ClsQNetworkAccessManager.get, req));
		
		LambdaExpression lambdaExpression = new LambdaExpression();
		addInstr(new QObjectConnect(reply,"&QNetworkReply::finished",aNetwork,
				lambdaExpression.setCapture(reply, pCallback),true));
	    		
		lambdaExpression.addInstr(new StdFunctionInvocation(pCallback, JsonTypes.JsonEntityRepository.callStaticMethod(MethodGetOneFromJson.getMethodName(entity),reply.callMethod(ClsQNetworkReply.readAll))));
		lambdaExpression.addInstr(reply.callMethodInstruction(ClsQNetworkReply.deleteLater));
	}

}