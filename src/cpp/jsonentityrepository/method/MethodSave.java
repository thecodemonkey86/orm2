package cpp.jsonentityrepository.method;

import cpp.CoreTypes;
import cpp.JsonTypes;
import cpp.NetworkTypes;
import cpp.Types;
import cpp.core.Attr;
import cpp.core.LambdaExpression;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.QStringLiteral;
import cpp.core.expression.Operators;
import cpp.core.expression.PlusOperatorExpression;
import cpp.core.expression.QByteArrayLiteral;
import cpp.core.expression.StdFunctionInvocation;
import cpp.core.expression.Var;
import cpp.core.instruction.DeclareInstruction;
import cpp.core.instruction.IfBlock;
import cpp.core.instruction.SemicolonTerminatedInstruction;
import cpp.entity.method.MethodAttrGetter;
import cpp.jsonentity.JsonEntity;
import cpp.jsonentity.method.MethodColumnAttrSetterInternal;
import cpp.jsonentity.method.MethodToJson;
import cpp.jsonentityrepository.ClsJsonEntityRepository;
import cpp.lib.ClsBaseJsonEntity;
import cpp.lib.ClsQByteArray;
import cpp.lib.ClsQJsonDocument;
import cpp.lib.ClsQJsonObject;
import cpp.lib.ClsQJsonValue;
import cpp.lib.ClsQNetworkAccessManager;
import cpp.lib.ClsQNetworkReply;
import cpp.lib.ClsQNetworkRequest;
import cpp.lib.ClsQUrl;
import cpp.lib.ClsQUrlQuery;
import cpp.lib.ClsStdFunction;
import cpp.lib.QObjectConnect;
import cpp.orm.JsonOrmUtil;
import database.column.Column;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.Expressions;

public class MethodSave extends Method {

	Param pCallback,pEntity; 
	JsonEntity entity;
	
	public MethodSave(JsonEntity entity) {
		super(Public, CoreTypes.Void, "save");
		pEntity = addParam(entity.toSharedPtr(), "entity");
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
		Var vRaw = lambdaExpression._declare(Types.QByteArray, "rawResponse",reply.callMethod(ClsQNetworkReply.readAll));
		Var d=new Var(JsonTypes.QJsonDocument, "_d");
		lambdaExpression.addInstr(new DeclareInstruction(d, JsonTypes.QJsonDocument.callStaticMethod(ClsQJsonDocument.fromJson, vRaw)));
		addInstr(new QObjectConnect(reply,"&QNetworkReply::finished",aNetwork,
				lambdaExpression.setCapture(reply, pCallback, pEntity),true));
		if(entity.getTbl().isAutoIncrement()) {
			IfBlock ifInsert = lambdaExpression._if(pEntity.callMethod(ClsBaseJsonEntity.isInsertNew));
			
			
			Var o = new Var(JsonTypes.QJsonObject, "_o");
			ifInsert.thenBlock().addInstr(new DeclareInstruction(o, d.callMethod(ClsQJsonDocument.object)));
			Column col = entity.getTbl().getPrimaryKey().getColumn(0);
			ifInsert.thenBlock().addInstr(pEntity.callMethodInstruction(MethodColumnAttrSetterInternal.getMethodName(col),JsonOrmUtil.jsonConvertMethod(o.callMethod(ClsQJsonObject.value, QStringLiteral.fromStringConstant(col.getName())), JsonEntity.getDatabaseMapper().columnToType(col)))); 
		}
		
		IfBlock ifNotEmpty = lambdaExpression._if(Expressions.not(vRaw.callMethod(ClsQByteArray.isEmpty)));
		
		lambdaExpression.addInstr(new SemicolonTerminatedInstruction("qWarning()<<rawResponse"));
		ifNotEmpty.thenBlock().addInstr(new StdFunctionInvocation(pCallback, reply.callMethod(ClsQNetworkReply.error)._equals(NetworkTypes.QNetworkReply.noError).binOp(Operators.AND,Expressions.not(d.callMethod(ClsQJsonDocument.object).callMethod(ClsQJsonObject.value, QStringLiteral.fromStringConstant("error")).callMethod(ClsQJsonValue.toBool)))));
		ifNotEmpty.thenBlock().addInstr(reply.callMethodInstruction(ClsQNetworkReply.deleteLater));
		ifNotEmpty.elseBlock().addInstr(new StdFunctionInvocation(pCallback,BoolExpression.FALSE));
	}

}
