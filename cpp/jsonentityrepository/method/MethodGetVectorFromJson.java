package cpp.jsonentityrepository.method;

import cpp.JsonTypes;
import cpp.Types;
import cpp.bean.Nullable;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.expression.Var;
import cpp.core.instruction.ForeachLoop;
import cpp.core.instruction.IfBlock;
import cpp.jsonentity.JsonEntities;
import cpp.jsonentity.JsonEntity;
import cpp.jsonentity.method.MethodColumnAttrSetterInternal;
import cpp.lib.ClsQJsonDocument;
import cpp.lib.ClsQJsonObject;
import cpp.lib.ClsQJsonValue;
import cpp.lib.ClsQVector;
import cpp.orm.JsonOrmUtil;
import cpp.orm.OrmUtil;
import database.column.Column;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import sunjava.bean.method.MethodAddRelatedBeanInternal;
import util.CodeUtil2;

public class MethodGetVectorFromJson extends Method{

	Param pJson;
	JsonEntity entity;
	
	public MethodGetVectorFromJson(JsonEntity entity) {
		super(Public, Types.qvector(entity.toSharedPtr()), getMethodName(entity));
		pJson = addParam(Types.QByteArray.toConstRef(), "jsondata");
		this.entity = entity;
		setStatic(true);
	}

	@Override
	public void addImplementation() {
		
		
		Var result = _declare(getReturnType(), "_result");
		Var jsondoc = _declare(JsonTypes.QJsonDocument, "jsondoc",JsonTypes.QJsonDocument.callStaticMethod(ClsQJsonDocument.fromJson, pJson));
		Var jsonArray = _declare(JsonTypes.QJsonArray, "jsonarray", jsondoc.callMethod(ClsQJsonDocument.array));
		ForeachLoop foreachJsonValue = _foreach(new Var(JsonTypes.QJsonValue, "jsonvalue"), jsonArray);
		Var jsonobject = foreachJsonValue._declare(JsonTypes.QJsonObject, "jsonobject", foreachJsonValue.getVar().callMethod(ClsQJsonValue.toObject));
		Var b1 = foreachJsonValue._declareMakeShared(entity, "b1");
		for(Column col : entity.getTbl().getAllColumns()) {
			if(!col.isRelationDestColumn() || col.isPartOfPk()) {
				if( col.isNullable()) {
					IfBlock ifValueIsNull = foreachJsonValue._ifNot(jsonobject.callMethod(ClsQJsonObject.value, QString.fromStringConstant(col.getName())).callMethod(ClsQJsonValue.isNull));
					//ifValueIsNull.thenBlock().addInstr( b1.callMethodInstruction(MethodColumnAttrSetNull.getMethodName(col)));
					ifValueIsNull.thenBlock().addInstr( b1.callMethodInstruction(MethodColumnAttrSetterInternal.getMethodName(col), JsonOrmUtil.jsonConvertMethod(jsonobject.callMethod(ClsQJsonObject.value, QString.fromStringConstant(col.getName())), ((Nullable)( ((Cls)b1.getType()).getAttrByName(col.getCamelCaseName())).getType()).getElementType())));
				} else {
				
					foreachJsonValue.addInstr( b1.callSetterMethodInstruction(col.getCamelCaseName(), JsonOrmUtil.jsonConvertMethod(jsonobject.callMethod(ClsQJsonObject.value, QString.fromStringConstant(col.getName())), ((Cls)b1.getType()).getAttrByName(col.getCamelCaseName()).getType())));
				}
			}
			
		}
		for(OneRelation r : entity.getOneRelations() ) {
			IfBlock ifValueIsNull = foreachJsonValue._ifNot(jsonobject.callMethod(ClsQJsonObject.value, QString.fromStringConstant(r.getColumns(0).getValue1().getName())).callMethod(ClsQJsonValue.isNull));
			JsonEntity e = JsonEntities.get(r.getDestTable());
			Var relationBeanData =ifValueIsNull.thenBlock()._declare(e.toSharedPtr(),r.getAlias(),parent.callStaticMethod(MethodGetOneFromJson.getMethodName(e), jsonobject.callMethod(ClsQJsonObject.value,QString.fromStringConstant(OrmUtil.getOneRelationDestAttrName(r))).callMethod(ClsQJsonValue.toObject)));
			ifValueIsNull.thenBlock().addInstr(b1.callSetterMethodInstruction(OrmUtil.getOneRelationDestAttrName(r), relationBeanData));
		}
		for(OneToManyRelation r : entity.getOneToManyRelations() ) {
			IfBlock ifValueIsNull = foreachJsonValue._ifNot(jsonobject.callMethod(ClsQJsonObject.value, QString.fromStringConstant(OrmUtil.getOneToManyRelationDestAttrNameSingular(r))).callMethod(ClsQJsonValue.isNull));
			JsonEntity e = JsonEntities.get(r.getDestTable());
			Var relationBeanDataArray  =ifValueIsNull.thenBlock()._declare(JsonTypes.QJsonArray,"_jsonDataArray" +r.getAlias(), jsonobject.callMethod(ClsQJsonObject.value,QString.fromStringConstant(OrmUtil.getOneToManyRelationDestAttrNameSingular(r))).callMethod(ClsQJsonValue.toArray));
			ForeachLoop foreachRelationBean = ifValueIsNull.thenBlock()._foreach(new Var(JsonTypes.QJsonValue.toConstRef(),"_jsonData" + r.getAlias()), relationBeanDataArray);
			foreachRelationBean.addInstr(b1.callMethodInstruction(MethodAddRelatedBeanInternal.getMethodName(r), parent.callStaticMethod(MethodGetOneFromJson.getMethodName(e),foreachRelationBean.getVar().callMethod(ClsQJsonValue.toObject))));
		}
		for(ManyRelation r : entity.getManyRelations() ) {
			IfBlock ifValueIsNull = foreachJsonValue._ifNot(jsonobject.callMethod(ClsQJsonObject.value, QString.fromStringConstant(OrmUtil.getManyRelationDestAttrName(r))).callMethod(ClsQJsonValue.isNull));
			JsonEntity e = JsonEntities.get(r.getDestTable());
			Var relationBeanDataArray  =ifValueIsNull.thenBlock()._declare(JsonTypes.QJsonArray,"_jsonDataArray" +r.getAlias(), jsonobject.callMethod(ClsQJsonObject.value,QString.fromStringConstant(OrmUtil.getManyRelationDestAttrName(r))).callMethod(ClsQJsonValue.toArray));
			ForeachLoop foreachRelationBean = ifValueIsNull.thenBlock()._foreach(new Var(JsonTypes.QJsonValue.toConstRef(),"_jsonData" + r.getAlias()), relationBeanDataArray);
			foreachRelationBean.addInstr(b1.callMethodInstruction(MethodAddRelatedBeanInternal.getMethodName(r), parent.callStaticMethod(MethodGetOneFromJson.getMethodName(e),foreachRelationBean.getVar().callMethod(ClsQJsonValue.toObject))));
		}
		/*for(OneToManyRelation r : entity.getOneToManyRelations() ) {
			Var arrRelationBeans =foreachBean._declare(Types.array(Types.Mixed),"relationBeans", foreachBean.getVar().callMethod( OrmUtil.getOneToManyRelationDestAttrNameSingular(r)));
			ForeachLoop foreachRelationBean = foreachBean._foreach(new Var(Beans.get(r.getDestTable()), "relationBean"+r.getAlias() ), arrRelationBeans);
			
			Var relationBeanData= foreachRelationBean._declare(Types.array(Types.String), "relationBeanData_"+r.getAlias(), foreachRelationBean.getVar().callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME));
			beanData.arrayIndexSet(new PhpStringLiteral(OrmUtil.getOneToManyRelationDestAttrNameSingular(r)), relationBeanData);
		}
		for(ManyRelation r : entity	.getManyRelations() ) {
			Var arrRelationBeans =foreachBean._declare(Types.array(Types.Mixed),"relationBeans", foreachBean.getVar().callMethod( OrmUtil.getManyRelationDestAttrNameSingular(r)));
			ForeachLoop foreachRelationBean = foreachBean._foreach(new Var(Beans.get(r.getDestTable()), "relationBean"+r.getAlias() ), arrRelationBeans);
			
			Var relationBeanData= foreachRelationBean._declare(Types.array(Types.String), "relationBeanData_"+r.getAlias(), foreachRelationBean.getVar().callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME));
			beanData.arrayIndexSet(new PhpStringLiteral(OrmUtil.getManyRelationDestAttrNameSingular(r)), relationBeanData);
		}*/
		
		foreachJsonValue.addInstr(result.callMethodInstruction(ClsQVector.append, b1));
		_return(result);
	}

	public static String getMethodName(JsonEntity entity) {
		return "get"+CodeUtil2.plural( entity.getName())+"FromJson";
	}
}
