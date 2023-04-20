package cpp.jsonentityrepository.method;

import cpp.CoreTypes;
import cpp.JsonTypes;
import cpp.NetworkTypes;
import cpp.core.Attr;
import cpp.core.LambdaExpression;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.expression.PlusOperatorExpression;
import cpp.core.expression.QByteArrayLiteral;
import cpp.core.expression.StdFunctionInvocation;
import cpp.core.expression.Var;
import cpp.core.instruction.SemicolonTerminatedInstruction;
import cpp.entity.method.MethodAttrGetter;
import cpp.jsonentity.JsonEntity;
import cpp.jsonentity.method.MethodToJson;
import cpp.jsonentityrepository.ClsJsonEntityRepository;
import cpp.lib.ClsQJsonDocument;
import cpp.lib.ClsQNetworkAccessManager;
import cpp.lib.ClsQNetworkReply;
import cpp.lib.ClsQNetworkRequest;
import cpp.lib.ClsQUrl;
import cpp.lib.ClsQUrlQuery;
import cpp.lib.ClsStdFunction;
import cpp.lib.QObjectConnect;

public class MethodSave extends Method {

	Param pCallback,pEntity; 
	JsonEntity entity;
	
	public MethodSave(JsonEntity entity) {
		super(Public, CoreTypes.Void, "save");
		pEntity = addParam(entity.toConstRef(), "entity");
		pCallback = addParam(new Param(new ClsStdFunction(CoreTypes.Void, CoreTypes.Bool), "callback"));
		this.entity = entity;
		setStatic(true); 
	}

	@Override
	public void addImplementation() {
		Attr aNetwork = parent.getStaticAttribute(ClsJsonEntityRepository.network);
		Var vUrl = _declare(NetworkTypes.QUrl, "url",JsonTypes.JsonEntityRepository.callStaticMethod(MethodAttrGetter.getMethodName(JsonTypes.JsonEntityRepository.getAttrByName(ClsJsonEntityRepository.baseUrl))));
		Var vUrlQuery = _declare(NetworkTypes.QUrlQuery, "urlquery");
		_callMethodInstr(vUrlQuery, ClsQUrlQuery.addQueryItem, QString.fromStringConstant("entityType"),QString.fromStringConstant(entity.getName()));
		_callMethodInstr(vUrlQuery,ClsQUrlQuery.addQueryItem, QString.fromStringConstant("queryType"), QString.fromStringConstant("save"));
		addInstr(vUrl.callMethodInstruction(ClsQUrl.setQuery, vUrlQuery));
		Var vRec = _declareInitConstructor(NetworkTypes.QNetworkRequest, "req", vUrl);
		addInstr(vRec.callMethodInstruction(ClsQNetworkRequest.setAttribute, NetworkTypes.QNetworkRequest.redirectPolicyAttribute, NetworkTypes.QNetworkRequest.sameOriginRedirectPolicy));
		addInstr(vRec.callMethodInstruction(ClsQNetworkRequest.setHeader,NetworkTypes.QNetworkRequest.contentTypeHeader,QString.fromStringConstant("application/x-www-form-urlencoded")));
		Var reply = _declare(NetworkTypes.QNetworkReply.toRawPointer(), "reply", aNetwork.callMethod(ClsQNetworkAccessManager.post,vRec, new PlusOperatorExpression(new QByteArrayLiteral("data="),NetworkTypes.QUrl.callStaticMethod(ClsQUrl.toPercentEncoding,  pEntity.callMethod(MethodToJson.getMethodName()).callMethod(ClsQJsonDocument.toJson, JsonTypes.QJsonDocument.Compact)))));
		
		LambdaExpression lambdaExpression = new LambdaExpression();
		addInstr(new QObjectConnect(reply,"&QNetworkReply::finished",aNetwork,
				lambdaExpression.setCapture(reply, pCallback),true));
		lambdaExpression.addInstr(new SemicolonTerminatedInstruction("qDebug()<<reply->readAll()"));	
		lambdaExpression.addInstr(new StdFunctionInvocation(pCallback, reply.callMethod(ClsQNetworkReply.error)._equals(NetworkTypes.QNetworkReply.noError)));
		lambdaExpression.addInstr(reply.callMethodInstruction(ClsQNetworkReply.deleteLater));
	}

}
