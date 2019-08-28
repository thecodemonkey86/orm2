package cpp.jsonentityrepository.method;

import cpp.JsonTypes;
import cpp.Types;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.SharedPtr;
import cpp.core.expression.Expressions;
import cpp.core.expression.Var;
import cpp.core.instruction.ForeachLoop;
import cpp.core.instruction.IfBlock;
import cpp.entity.Nullable;
import cpp.jsonentity.JsonEntities;
import cpp.jsonentity.JsonEntity;
import cpp.jsonentity.method.MethodColumnAttrSetterInternal;
import cpp.lib.ClsQJsonDocument;
import cpp.lib.ClsQJsonObject;
import cpp.lib.ClsQJsonValue;
import cpp.orm.JsonOrmUtil;
import cpp.orm.OrmUtil;
import database.column.Column;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import sunjava.bean.method.MethodAddRelatedBeanInternal;

public class MethodGetOneFromJson extends Method {

	Param pJson;
	Param pJsonObject;
	JsonEntity entity;
	boolean overloadedQByteArray;

	public MethodGetOneFromJson(JsonEntity entity, boolean overloadedQByteArray) {
		super(Public, entity.toSharedPtr(), getMethodName(entity));
		this.overloadedQByteArray = overloadedQByteArray;
		if (overloadedQByteArray) {
			pJson = addParam(Types.QByteArray.toConstRef(), "jsondata");
		} else {
			pJsonObject = addParam(JsonTypes.QJsonObject.toConstRef(), "jsonobject");
		}
		this.entity = entity;
		setStatic(true);
	}

	@Override
	public void addImplementation() {
		
		if (overloadedQByteArray) {
			_return(parent.callStaticMethod(getMethodName(entity),
					_declare(JsonTypes.QJsonObject, "jsonobject", JsonTypes.QJsonDocument.callStaticMethod(ClsQJsonDocument.fromJson, pJson).callMethod(ClsQJsonDocument.object))));
		} else {
			Var e1 = _declareMakeShared((Cls) ((SharedPtr) returnType).getElementType(), "e1");
			for (Column col : entity.getTbl().getAllColumns()) {
				if (col.isNullable()) {
					IfBlock ifValueIsNull = _ifNot(
							pJsonObject.callMethod(ClsQJsonObject.value, QString.fromStringConstant(col.getName()))
									.callMethod(ClsQJsonValue.isNull));
					// ifValueIsNull.thenBlock().addInstr(
					// e1.callMethodInstruction(MethodColumnAttrSetNull.getMethodName(col)));
					ifValueIsNull.thenBlock().addInstr(e1.callMethodInstruction(MethodColumnAttrSetterInternal.getMethodName(col),
							JsonOrmUtil.jsonConvertMethod(
									pJsonObject.callMethod(ClsQJsonObject.value,
											QString.fromStringConstant(col.getName())),
									((Nullable) (((Cls) e1.getType()).getAttrByName(col.getCamelCaseName())).getType())
											.getElementType())));
				} else {

					addInstr(e1.callSetterMethodInstruction(col.getCamelCaseName(),
							JsonOrmUtil.jsonConvertMethod(
									pJsonObject.callMethod(ClsQJsonObject.value,
											QString.fromStringConstant(col.getName())),
									((Cls) e1.getType()).getAttrByName(col.getCamelCaseName()).getType())));
				}
			}
			for(OneRelation r : entity.getOneRelations() ) {
				IfBlock ifValueIsNull = _if(Expressions.and(
						
						pJsonObject.callMethod(ClsQJsonObject.contains, QString.fromStringConstant(OrmUtil.getOneRelationDestAttrName(r))),
						Expressions.not(pJsonObject.callMethod(ClsQJsonObject.value, QString.fromStringConstant(OrmUtil.getOneRelationDestAttrName(r))).callMethod(ClsQJsonValue.isNull))));
				JsonEntity e = JsonEntities.get(r.getDestTable());
				Var relationBeanData =ifValueIsNull.thenBlock()._declare(e.toSharedPtr(),r.getAlias(),parent.callStaticMethod(MethodGetOneFromJson.getMethodName(e), pJsonObject.callMethod(ClsQJsonObject.value,QString.fromStringConstant(OrmUtil.getOneRelationDestAttrName(r))).callMethod(ClsQJsonValue.toObject)));
				ifValueIsNull.thenBlock().addInstr(e1.callSetterMethodInstruction(OrmUtil.getOneRelationDestAttrName(r), relationBeanData));
			}
			for(OneToManyRelation r : entity.getOneToManyRelations() ) {
				IfBlock ifValueIsNull = _if(Expressions.and(
						
						pJsonObject.callMethod(ClsQJsonObject.contains, QString.fromStringConstant(OrmUtil.getOneToManyRelationDestAttrNameSingular(r))),
						Expressions.not(pJsonObject.callMethod(ClsQJsonObject.value, QString.fromStringConstant(OrmUtil.getOneToManyRelationDestAttrNameSingular(r))).callMethod(ClsQJsonValue.isNull))));
				JsonEntity e = JsonEntities.get(r.getDestTable());
				Var relationBeanDataArray  =ifValueIsNull.thenBlock()._declare(JsonTypes.QJsonArray,"_jsonDataArray" +r.getAlias(), pJsonObject.callMethod(ClsQJsonObject.value,QString.fromStringConstant(OrmUtil.getOneToManyRelationDestAttrNameSingular(r))).callMethod(ClsQJsonValue.toArray));
				ForeachLoop foreachRelationBean = ifValueIsNull.thenBlock()._foreach(new Var(JsonTypes.QJsonValue.toConstRef(),"_jsonData" + r.getAlias()), relationBeanDataArray);
				foreachRelationBean.addInstr(e1.callMethodInstruction(MethodAddRelatedBeanInternal.getMethodName(r), parent.callStaticMethod(MethodGetOneFromJson.getMethodName(e),foreachRelationBean.getVar().callMethod(ClsQJsonValue.toObject))));
			}
			for(ManyRelation r : entity.getManyRelations() ) {
				IfBlock ifValueIsNull = _if(Expressions.and(
						
						pJsonObject.callMethod(ClsQJsonObject.contains, QString.fromStringConstant(OrmUtil.getManyRelationDestAttrNameSingular(r))),
						Expressions.not(pJsonObject.callMethod(ClsQJsonObject.value, QString.fromStringConstant(OrmUtil.getManyRelationDestAttrNameSingular(r))).callMethod(ClsQJsonValue.isNull))));
				JsonEntity e = JsonEntities.get(r.getDestTable());
				Var relationBeanDataArray  =ifValueIsNull.thenBlock()._declare(JsonTypes.QJsonArray,"_jsonDataArray" +r.getAlias(), pJsonObject.callMethod(ClsQJsonObject.value,QString.fromStringConstant(OrmUtil.getManyRelationDestAttrNameSingular(r))).callMethod(ClsQJsonValue.toArray));
				ForeachLoop foreachRelationBean = ifValueIsNull.thenBlock()._foreach(new Var(JsonTypes.QJsonValue.toConstRef(),"_jsonData" + r.getAlias()), relationBeanDataArray);
				foreachRelationBean.addInstr(e1.callMethodInstruction(MethodAddRelatedBeanInternal.getMethodName(r), parent.callStaticMethod(MethodGetOneFromJson.getMethodName(e),foreachRelationBean.getVar().callMethod(ClsQJsonValue.toObject))));
			}
			_return(e1);
		}

	}

	public static String getMethodName(JsonEntity entity) {
		return "getOne" + entity.getName() + "FromJson";
	}
}
