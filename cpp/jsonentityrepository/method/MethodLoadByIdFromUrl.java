package cpp.jsonentityrepository.method;

import cpp.CoreTypes;
import cpp.JsonTypes;
import cpp.NetworkTypes;
import cpp.core.Attr;
import cpp.core.LambdaExpression;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.QtSignal;
import cpp.core.expression.Expression;
import cpp.core.expression.Var;
import cpp.core.instruction.QtEmit;
import cpp.jsonentity.JsonEntity;
import cpp.jsonentityrepository.JsonEntityRepository;
import cpp.lib.ClsQNetworkAccessManager;
import cpp.lib.ClsQNetworkReply;
import cpp.lib.ClsQString;
import cpp.lib.ClsQUrl;
import cpp.lib.ClsQUrlQuery;
import cpp.lib.QObjectConnect;
import database.column.Column;

public class MethodLoadByIdFromUrl extends Method {

	JsonEntity entity;
	
	public MethodLoadByIdFromUrl(JsonEntity entity) {
		super(Public, CoreTypes.Void, "load"+entity.getName()+"ById");
		this.entity = entity;
		for(Column pkCol : entity.getTbl().getPrimaryKey()) {
			addParam(entity.getAttrByName(pkCol.getCamelCaseName()).getType(), pkCol.getCamelCaseName());
		}
	}

	@Override
	public void addImplementation() {
		/*QUrlQuery query;

query.addQueryItem("username", "test");
query.addQueryItem("password", "test");

url.setQuery(query.query());*/
		Attr aNetwork = parent.getStaticAttribute(JsonEntityRepository.network);
		Var urlquery = _declare(NetworkTypes.QUrlQuery, "urlquery");
		Var url = _declare(NetworkTypes.QUrl, "url",_this().accessAttr("url"));
		addInstr( urlquery.callMethodInstruction(ClsQUrlQuery.addQueryItem, QString.fromStringConstant("entityType"), QString.fromStringConstant(entity.getName())));
		
		for(Column pkCol : entity.getTbl().getPrimaryKey()) {
			Param param = getParam(pkCol.getCamelCaseName());
			Expression e = param;
			
			if(param.getType().equals(CoreTypes.Int32)) {
				e = CoreTypes.QString.callStaticMethod(ClsQString.number, e);
			}
			
			addInstr( urlquery.callMethodInstruction(ClsQUrlQuery.addQueryItem, QString.fromStringConstant(pkCol.getName()),e));
		}
		addInstr(url.callMethodInstruction(ClsQUrl.setQuery,urlquery));
		Var req = _declareInitConstructor(NetworkTypes.QNetworkRequest, "req", url);
		Var reply = _declare(NetworkTypes.QNetworkReply.toRawPointer(), "reply", aNetwork.callMethod(ClsQNetworkAccessManager.get, req));
		
		
		LambdaExpression lambdaExpression = new LambdaExpression();
		addInstr(new QObjectConnect(reply,"&QNetworkReply::finished",aNetwork,
				lambdaExpression.setCapture(reply, _this())));
	    		
		lambdaExpression.addInstr(new QtEmit( (QtSignal)JsonTypes.JsonEntityRepository.getMethod("onLoaded"+entity.getName()), JsonTypes.JsonEntityRepository.callStaticMethod(MethodGetFromJson.getMethodName(entity),reply.callMethod(ClsQNetworkReply.readAll))));
		lambdaExpression.addInstr(reply.callMethodInstruction(ClsQNetworkReply.deleteLater));
	}

}
