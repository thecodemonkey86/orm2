package cpp.jsonentityrepository.method;

import java.util.List;

import cpp.JsonTypes;
import cpp.NetworkTypes;
import cpp.QtCoreTypes;
import cpp.Types;
import cpp.core.Attr;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.Expressions;
import cpp.core.expression.Var;
import cpp.core.instruction.ForeachLoop;
import cpp.core.instruction.IfBlock;
import cpp.entity.Nullable;
import cpp.jsonentity.JsonEntities;
import cpp.jsonentity.JsonEntity;
import cpp.jsonentity.method.MethodColumnAttrSetterInternal;
import cpp.jsonentityrepository.JsonEntityRepository;
import cpp.lib.ClsQEventLoop;
import cpp.lib.ClsQJsonDocument;
import cpp.lib.ClsQJsonObject;
import cpp.lib.ClsQJsonValue;
import cpp.lib.ClsQNetworkAccessManager;
import cpp.lib.ClsQNetworkReply;
import cpp.lib.ClsQNetworkRequest;
import cpp.lib.ClsQUrl;
import cpp.lib.ClsQUrlQuery;
import cpp.lib.QObjectConnect;
import cpp.orm.JsonOrmUtil;
import cpp.orm.OrmUtil;
import database.column.Column;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import database.relation.PrimaryKey;
import sunjava.bean.method.MethodAddRelatedBeanInternal;

public class MethodEntityLoad extends Method {

	protected List<OneRelation> oneRelations;
	protected List<OneToManyRelation> oneToManyRelations;
	protected List<ManyRelation> manyRelations;
	protected PrimaryKey primaryKey;
	protected JsonEntity entity;
	protected Param pBean;
	
	public static String getMethodName() {
		return "load";
	}
	
	public MethodEntityLoad(JsonEntity entity) {
		super(Public, Types.Void, getMethodName());
		
		this.oneRelations = entity.getOneRelations();
		this.oneToManyRelations = entity.getOneToManyRelations();
		this.manyRelations = entity.getManyRelations();
		this.primaryKey = entity.getTbl().getPrimaryKey();
		this.entity = entity;
		pBean = addParam(entity.toRawPointer(), "entity");
		
	}

	@Override
	public boolean includeIfEmpty() {
		return true;
	}
	
	@Override
	public void addImplementation() {
		Attr aNetwork = parent.getStaticAttribute(JsonEntityRepository.network);
		Var urlquery = _declare(NetworkTypes.QUrlQuery, "urlquery");
		Var url = _declare(NetworkTypes.QUrl, "url",_this().accessAttr("url"));
		addInstr( urlquery.callMethodInstruction(ClsQUrlQuery.addQueryItem, QString.fromStringConstant("queryType"),QString.fromStringConstant("byId")));
		addInstr( urlquery.callMethodInstruction(ClsQUrlQuery.addQueryItem, QString.fromStringConstant("entityType"), QString.fromStringConstant(entity.getName())));
		
		for(Column pkCol : entity.getTbl().getPrimaryKey()) {
			
			addInstr( urlquery.callMethodInstruction(ClsQUrlQuery.addQueryItem, QString.fromStringConstant(pkCol.getName()),JsonOrmUtil.convertToString(pBean.callAttrGetter(pkCol.getCamelCaseName()))));
		}
		addInstr(url.callMethodInstruction(ClsQUrl.setQuery,urlquery));
		Var req = _declareInitConstructor(NetworkTypes.QNetworkRequest, "req", url);
		addInstr(req.callMethodInstruction(ClsQNetworkRequest.setAttribute, NetworkTypes.QNetworkRequest.FollowRedirectsAttribute, BoolExpression.TRUE));
		Var reply = _declare(NetworkTypes.QNetworkReply.toRawPointer(), "reply", aNetwork.callMethod(ClsQNetworkAccessManager.get, req));
		
		Var eventLoop = _declare(QtCoreTypes.QEventLoop, "eventloop");
		addInstr(new QObjectConnect(reply,"&QNetworkReply::finished",eventLoop.pointer(), ClsQEventLoop.quit));
		addInstr(eventLoop.callMethodInstruction(ClsQEventLoop.exec));
		Var pJsonObject = _declare(JsonTypes.QJsonObject, "jsonobject", JsonTypes.QJsonDocument.callStaticMethod(ClsQJsonDocument.fromJson, reply.callMethod(ClsQNetworkReply.readAll)).callMethod(ClsQJsonDocument.object));
		
		for (Column col : entity.getTbl().getAllColumns()) {
			if (col.isNullable()) {
				IfBlock ifValueIsNull = _ifNot(
						pJsonObject.callMethod(ClsQJsonObject.value, QString.fromStringConstant(col.getName()))
								.callMethod(ClsQJsonValue.isNull));
				// ifValueIsNull.thenBlock().addInstr(
				// b1.callMethodInstruction(MethodColumnAttrSetNull.getMethodName(col)));
				ifValueIsNull.thenBlock().addInstr(pBean.callMethodInstruction(MethodColumnAttrSetterInternal.getMethodName(col),
						JsonOrmUtil.jsonConvertMethod(
								pJsonObject.callMethod(ClsQJsonObject.value,
										QString.fromStringConstant(col.getName())),
								((Nullable) (((Cls) pBean.getType()).getAttrByName(col.getCamelCaseName())).getType())
										.getElementType())));
			} else {

				addInstr(pBean.callSetterMethodInstruction(col.getCamelCaseName(),
						JsonOrmUtil.jsonConvertMethod(
								pJsonObject.callMethod(ClsQJsonObject.value,
										QString.fromStringConstant(col.getName())),
								((Cls) pBean.getType()).getAttrByName(col.getCamelCaseName()).getType())));
			}
		}
		for(OneRelation r : entity.getOneRelations() ) {
			IfBlock ifValueIsNull = _if(Expressions.and(
					
					pJsonObject.callMethod(ClsQJsonObject.contains, QString.fromStringConstant(OrmUtil.getOneRelationDestAttrName(r))),
					Expressions.not(pJsonObject.callMethod(ClsQJsonObject.value, QString.fromStringConstant(OrmUtil.getOneRelationDestAttrName(r))).callMethod(ClsQJsonValue.isNull))));
			JsonEntity e = JsonEntities.get(r.getDestTable());
			Var relationBeanData =ifValueIsNull.thenBlock()._declare(e.toSharedPtr(),r.getAlias(),parent.callStaticMethod(MethodGetOneFromJson.getMethodName(e), pJsonObject.callMethod(ClsQJsonObject.value,QString.fromStringConstant(OrmUtil.getOneRelationDestAttrName(r))).callMethod(ClsQJsonValue.toObject)));
			ifValueIsNull.thenBlock().addInstr(pBean.callSetterMethodInstruction(OrmUtil.getOneRelationDestAttrName(r), relationBeanData));
		}
		for(OneToManyRelation r : entity.getOneToManyRelations() ) {
			IfBlock ifValueIsNull = _if(Expressions.and(
					
					pJsonObject.callMethod(ClsQJsonObject.contains, QString.fromStringConstant(OrmUtil.getOneToManyRelationDestAttrNameSingular(r))),
					Expressions.not(pJsonObject.callMethod(ClsQJsonObject.value, QString.fromStringConstant(OrmUtil.getOneToManyRelationDestAttrNameSingular(r))).callMethod(ClsQJsonValue.isNull))));
			JsonEntity e = JsonEntities.get(r.getDestTable());
			Var relationBeanDataArray  =ifValueIsNull.thenBlock()._declare(JsonTypes.QJsonArray,"_jsonDataArray" +r.getAlias(), pJsonObject.callMethod(ClsQJsonObject.value,QString.fromStringConstant(OrmUtil.getOneToManyRelationDestAttrNameSingular(r))).callMethod(ClsQJsonValue.toArray));
			ForeachLoop foreachRelationBean = ifValueIsNull.thenBlock()._foreach(new Var(JsonTypes.QJsonValue.toConstRef(),"_jsonData" + r.getAlias()), relationBeanDataArray);
			foreachRelationBean.addInstr(pBean.callMethodInstruction(MethodAddRelatedBeanInternal.getMethodName(r), parent.callStaticMethod(MethodGetOneFromJson.getMethodName(e),foreachRelationBean.getVar().callMethod(ClsQJsonValue.toObject))));
		}
		for(ManyRelation r : entity.getManyRelations() ) {
			IfBlock ifValueIsNull = _if(Expressions.and(
					
					pJsonObject.callMethod(ClsQJsonObject.contains, QString.fromStringConstant(OrmUtil.getManyRelationDestAttrNameSingular(r))),
					Expressions.not(pJsonObject.callMethod(ClsQJsonObject.value, QString.fromStringConstant(OrmUtil.getManyRelationDestAttrNameSingular(r))).callMethod(ClsQJsonValue.isNull))));
			JsonEntity e = JsonEntities.get(r.getDestTable());
			Var relationBeanDataArray  =ifValueIsNull.thenBlock()._declare(JsonTypes.QJsonArray,"_jsonDataArray" +r.getAlias(), pJsonObject.callMethod(ClsQJsonObject.value,QString.fromStringConstant(OrmUtil.getManyRelationDestAttrNameSingular(r))).callMethod(ClsQJsonValue.toArray));
			ForeachLoop foreachRelationBean = ifValueIsNull.thenBlock()._foreach(new Var(JsonTypes.QJsonValue.toConstRef(),"_jsonData" + r.getAlias()), relationBeanDataArray);
			foreachRelationBean.addInstr(pBean.callMethodInstruction(MethodAddRelatedBeanInternal.getMethodName(r), parent.callStaticMethod(MethodGetOneFromJson.getMethodName(e),foreachRelationBean.getVar().callMethod(ClsQJsonValue.toObject))));
		}
		
	}

}
