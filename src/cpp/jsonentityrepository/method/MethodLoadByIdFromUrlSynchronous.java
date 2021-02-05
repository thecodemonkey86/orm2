//package cpp.jsonentityrepository.method;
//
//import cpp.JsonTypes;
//import cpp.NetworkTypes;
//import cpp.QtCoreTypes;
//import cpp.core.Attr;
//import cpp.core.Method;
//import cpp.core.Param;
//import cpp.core.QString;
//import cpp.core.expression.BoolExpression;
//import cpp.core.expression.Expression;
//import cpp.core.expression.Var;
//import cpp.jsonentity.JsonEntity;
//import cpp.jsonentityrepository.ClsJsonEntityRepository;
//import cpp.lib.ClsQEventLoop;
//import cpp.lib.ClsQNetworkAccessManager;
//import cpp.lib.ClsQNetworkReply;
//import cpp.lib.ClsQNetworkRequest;
//import cpp.lib.ClsQUrl;
//import cpp.lib.ClsQUrlQuery;
//import cpp.lib.QObjectConnect;
//import cpp.orm.JsonOrmUtil;
//import database.column.Column;
//
//@Deprecated
//public class MethodLoadByIdFromUrlSynchronous extends Method {
//
//	JsonEntity entity;
//	
//	public MethodLoadByIdFromUrlSynchronous(JsonEntity entity) {
//		super(Public, entity.toSharedPtr(), "load"+entity.getName()+"ById");
//		this.entity = entity;
//		for(Column pkCol : entity.getTbl().getPrimaryKey()) {
//			addParam(entity.getAttrByName(pkCol.getCamelCaseName()).getType(), pkCol.getCamelCaseName());
//		}
//	}
//
//	@Override
//	public void addImplementation() {
//		/*QUrlQuery query;
//
//query.addQueryItem("username", "test");
//query.addQueryItem("password", "test");
//
//url.setQuery(query.query());*/
//		Attr aNetwork = parent.getStaticAttribute(ClsJsonEntityRepository.network);
//		Var urlquery = _declare(NetworkTypes.QUrlQuery, "urlquery");
//		Var url = _declare(NetworkTypes.QUrl, "url",_this().accessAttr("url"));
//		addInstr( urlquery.callMethodInstruction(ClsQUrlQuery.addQueryItem, QString.fromStringConstant("queryType"),QString.fromStringConstant("byId")));
//		addInstr( urlquery.callMethodInstruction(ClsQUrlQuery.addQueryItem, QString.fromStringConstant("entityType"), QString.fromStringConstant(entity.getName())));
//		
//		for(Column pkCol : entity.getTbl().getPrimaryKey()) {
//			Param param = getParam(pkCol.getCamelCaseName());
//			Expression e = JsonOrmUtil.convertToString(param);
//			
//			
//			
//			addInstr( urlquery.callMethodInstruction(ClsQUrlQuery.addQueryItem, QString.fromStringConstant(pkCol.getName()),e));
//		}
//		addInstr(url.callMethodInstruction(ClsQUrl.setQuery,urlquery));
//		Var req = _declareInitConstructor(NetworkTypes.QNetworkRequest, "req", url);
//		addInstr(req.callMethodInstruction(ClsQNetworkRequest.setAttribute, NetworkTypes.QNetworkRequest.FollowRedirectsAttribute, BoolExpression.TRUE));
//		Var reply = _declare(NetworkTypes.QNetworkReply.toRawPointer(), "reply", aNetwork.callMethod(ClsQNetworkAccessManager.get, req));
//		
//		Var eventLoop = _declare(QtCoreTypes.QEventLoop, "eventloop");
//		addInstr(new QObjectConnect(reply,"&QNetworkReply::finished",eventLoop.pointer(), ClsQEventLoop.quit));
//		addInstr(eventLoop.callMethodInstruction(ClsQEventLoop.exec));
//		_return(JsonTypes.JsonEntityRepository.callStaticMethod(MethodGetOneFromJson.getMethodName(entity), reply.callMethod(ClsQNetworkReply.readAll)));
//			
//	}
//
//}
