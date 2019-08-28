package cpp.jsonentityrepository.method;

import cpp.CoreTypes;
import cpp.JsonTypes;
import cpp.NetworkTypes;
import cpp.core.Attr;
import cpp.core.LambdaExpression;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QtSignal;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.Var;
import cpp.core.instruction.QtEmit;
import cpp.jsonentity.JsonEntity;
import cpp.jsonentityrepository.JsonEntityRepository;
import cpp.lib.ClsQNetworkAccessManager;
import cpp.lib.ClsQNetworkReply;
import cpp.lib.ClsQNetworkRequest;
import cpp.lib.QObjectConnect;

public class MethodLoadFromUrl extends Method {

	Param pUrl; 
	JsonEntity entity;
	
	public MethodLoadFromUrl(JsonEntity entity) {
		super(Public, CoreTypes.Void, "load"+entity.getName()+"FromUrl");
		pUrl = addParam(new Param(NetworkTypes.QUrl.toConstRef(), "url"));
		this.entity = entity;
	}

	@Override
	public void addImplementation() {
		Attr aNetwork = parent.getStaticAttribute(JsonEntityRepository.network);
		Var req = _declareInitConstructor(NetworkTypes.QNetworkRequest, "req", pUrl);
		addInstr(req.callMethodInstruction(ClsQNetworkRequest.setAttribute, NetworkTypes.QNetworkRequest.FollowRedirectsAttribute, BoolExpression.TRUE));
		Var reply = _declare(NetworkTypes.QNetworkReply.toRawPointer(), "reply", aNetwork.callMethod(ClsQNetworkAccessManager.get, req));
		
		LambdaExpression lambdaExpression = new LambdaExpression();
		addInstr(new QObjectConnect(reply,"&QNetworkReply::finished",aNetwork,
				lambdaExpression.setCapture(reply, _this())));
	    		
		lambdaExpression.addInstr(new QtEmit( (QtSignal)JsonTypes.JsonEntityRepository.getMethod(JsonEntityRepository.getSignalNameOnLoaded(entity)), JsonTypes.JsonEntityRepository.callStaticMethod(MethodGetVectorFromJson.getMethodName(entity),reply.callMethod(ClsQNetworkReply.readAll))));
		lambdaExpression.addInstr(reply.callMethodInstruction(ClsQNetworkReply.deleteLater));
	}

}
