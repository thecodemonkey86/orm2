package cpp.jsonentityrepository.method;

import cpp.CoreTypes;
import cpp.JsonTypes;
import cpp.NetworkTypes;
import cpp.core.Attr;
import cpp.core.LambdaExpression;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.Type;
import cpp.core.expression.StdFunctionInvocation;
import cpp.core.expression.Var;
import cpp.entity.method.MethodAttrGetter;
import cpp.jsonentity.JsonEntity;
import cpp.jsonentityrepository.ClsJsonEntityRepository;
import cpp.lib.ClsQNetworkAccessManager;
import cpp.lib.ClsQNetworkReply;
import cpp.lib.ClsQNetworkRequest;
import cpp.lib.ClsQUrl;
import cpp.lib.ClsQUrlQuery;
import cpp.lib.ClsStdFunction;
import cpp.lib.QObjectConnect;
import cpp.util.JsonOrmUtil;
import database.column.Column;

public class MethodLoadById extends Method{

	Param pCallback;
	JsonEntity entity;
	public MethodLoadById(JsonEntity entity) {
		super(Public, CoreTypes.Void, getMethodName(entity));
		setStatic(true);
		for(Column col:entity.getTbl().getPrimaryKey()) {
			Type colType =  JsonEntity.getDatabaseMapper().columnToType(col);
			addParam(new Param(colType.isPrimitiveType() ? colType : colType.toConstRef(), col.getCamelCaseName()));
		}
		pCallback = addParam(new Param(new ClsStdFunction(CoreTypes.Void, entity.toSharedPtr().toConstRef()), "callback"));
		this.entity = entity;
	}
	
	public static String getMethodName(JsonEntity entity) {
		return "load" + entity.getName()+"ById";
	}

	@Override
	public void addImplementation() {
		Attr aNetwork = parent.getStaticAttribute(ClsJsonEntityRepository.network);
		Var vUrl = _declare(NetworkTypes.QUrl, "url",JsonTypes.JsonEntityRepository.callStaticMethod(MethodAttrGetter.getMethodName(JsonTypes.JsonEntityRepository.getAttrByName(ClsJsonEntityRepository.baseUrl))));
		Var vUrlQuery = _declare(NetworkTypes.QUrlQuery, "urlquery");
		_callMethodInstr(vUrlQuery, ClsQUrlQuery.addQueryItem, QString.fromStringConstant("entityType"),QString.fromStringConstant(entity.getName()));
		_callMethodInstr(vUrlQuery,ClsQUrlQuery.addQueryItem, QString.fromStringConstant("queryType"), QString.fromStringConstant("byId"));
		for(Column colPk : entity.getTbl().getPrimaryKey()) {
			 addInstr(vUrlQuery.callMethodInstruction(ClsQUrlQuery.addQueryItem,QString.fromStringConstant(colPk.getName()), JsonOrmUtil.convertToQString(getParam(colPk.getCamelCaseName()))));
		}	
		addInstr(vUrl.callMethodInstruction(ClsQUrl.setQuery, vUrlQuery));
		
		Var req = _declareInitConstructor(NetworkTypes.QNetworkRequest, "req", vUrl);
		addInstr(req.callMethodInstruction(ClsQNetworkRequest.setAttribute, NetworkTypes.QNetworkRequest.redirectPolicyAttribute, NetworkTypes.QNetworkRequest.sameOriginRedirectPolicy));
		Var reply = _declare(NetworkTypes.QNetworkReply.toRawPointer(), "reply", aNetwork.callMethod(ClsQNetworkAccessManager.get, req));
		
		LambdaExpression lambdaExpression = new LambdaExpression();
		addInstr(new QObjectConnect(reply,"&QNetworkReply::finished",aNetwork,
				lambdaExpression.setCapture(reply, pCallback),true));
	    		
		lambdaExpression.addInstr(new StdFunctionInvocation(pCallback, JsonTypes.JsonEntityRepository.callStaticMethod(MethodGetOneFromJson.getMethodName(entity),reply.callMethod(ClsQNetworkReply.readAll))));
		lambdaExpression.addInstr(reply.callMethodInstruction(ClsQNetworkReply.deleteLater));
		
	}

}
