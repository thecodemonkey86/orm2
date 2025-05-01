package php.rest.method;

import java.util.Collection;

import cpp.orm.OrmUtil;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import php.core.PhpFunctions;
import php.core.PhpGlobals;
import php.core.Types;
import php.core.expression.ArrayInitExpression;
import php.core.expression.BoolExpression;
import php.core.expression.Expression;
import php.core.expression.InlineIfExpression;
import php.core.expression.PhpStringLiteral;
import php.core.expression.Var;
import php.core.instruction.CaseBlock;
import php.core.instruction.ForeachLoop;
import php.core.instruction.IfBlock;
import php.core.instruction.MethodCallInstruction;
import php.core.instruction.SwitchBlock;
import php.core.instruction.ThrowInstruction;
import php.core.method.Method;
import php.entity.Entities;
import php.entity.EntityCls;
import php.entity.method.MethodGetFieldsAsAssocArray;
import php.entityrepository.query.method.MethodEntityQueryFetch;
import php.lib.ClsBaseEntityQuery;

public class RestMethodGetList extends Method {

	Collection<EntityCls> entities;
	public RestMethodGetList(Collection<EntityCls> entities) {
		super(Public, Types.Void, "get");
		setStatic(true);
		this.entities = entities;
	}

	@Override
	public void addImplementation() {
		SwitchBlock switchEntityType = _switch(PhpGlobals.$_GET.arrayIndex(new PhpStringLiteral("entityType")));
		for(EntityCls entity : entities) {
			CaseBlock caseEntityType = switchEntityType._case(new PhpStringLiteral(entity.getName()));
			
			Expression e = Types.EntityRepository.callStaticMethod("createQuery"+entity.getName())
					.callMethod(ClsBaseEntityQuery.select);
			Var vEntityQuery = caseEntityType._declare(e.getType(),"query"+entity.getName(),e);
			IfBlock ifIssetCondition= caseEntityType
					._if(PhpFunctions.isset.call(PhpGlobals.$_GET.arrayIndex(new PhpStringLiteral("condition")))) ;
			Var vQueryJson = ifIssetCondition.thenBlock()._declare(Types.array(Types.Mixed), "_json", PhpFunctions.json_decode.call(PhpGlobals.$_GET.arrayIndex(new PhpStringLiteral("condition")) ,BoolExpression.TRUE));
			Var vJoinJson = ifIssetCondition.thenBlock()._declare(Types.array(Types.Mixed), "_joinJson",vQueryJson.arrayIndex("joins") );
			Var vOrderByJson = ifIssetCondition.thenBlock()._declare(Types.array(Types.Mixed), "_jOrderBy",vQueryJson.arrayIndex("orderby") );
			ForeachLoop forJoins= ifIssetCondition.thenBlock()._foreach(new Var(Types.Mixed, "_j"), vJoinJson);
			Var vSqlJoinTable =  forJoins._declare(Types.String, "_sqlJoinTable", forJoins.getVar().arrayIndex("table"));
			Var vSqlJoinOn =  forJoins._declare(Types.String, "_sqlJoinOn", forJoins.getVar().arrayIndex("on"));
			
			forJoins._if(PhpFunctions.str_contains.call(vSqlJoinTable,new PhpStringLiteral("\'"))
					._or(PhpFunctions.str_contains.call(vSqlJoinTable,new PhpStringLiteral("\""))
					._or(PhpFunctions.str_contains.call(vSqlJoinTable,new PhpStringLiteral("/*"))
					._or(PhpFunctions.str_contains.call(vSqlJoinTable,new PhpStringLiteral("*/"))
					._or(PhpFunctions.str_contains.call(vSqlJoinTable,new PhpStringLiteral("//"))
					._or(PhpFunctions.str_contains.call(vSqlJoinOn,new PhpStringLiteral("\'"))
					._or(PhpFunctions.str_contains.call(vSqlJoinOn,new PhpStringLiteral("\""))
					._or(PhpFunctions.str_contains.call(vSqlJoinOn,new PhpStringLiteral("/*"))
					._or(PhpFunctions.str_contains.call(vSqlJoinOn,new PhpStringLiteral("*/"))
					._or(PhpFunctions.str_contains.call(vSqlJoinOn,new PhpStringLiteral("//"))
					)))))))))).thenBlock().
			addInstr(new ThrowInstruction(Types.Exception, new PhpStringLiteral("invalid SQL")));
			
			forJoins.addInstr(new MethodCallInstruction(vEntityQuery.callMethod(ClsBaseEntityQuery.join,vSqlJoinTable,vSqlJoinOn,  forJoins.getVar().arrayIndex(new PhpStringLiteral("params")))));
			Expression eConditions = vQueryJson.arrayIndex("conditions");
			IfBlock ifIssetConditions =  ifIssetCondition.thenBlock()._if(PhpFunctions.isset.call(eConditions));
					
			Var vCondJson =  ifIssetConditions.thenBlock()._declare(Types.array(Types.Mixed), "_condJson",eConditions );
			ForeachLoop forCond=  ifIssetConditions.thenBlock()._foreach(new Var(Types.Mixed, "_c"), vCondJson);
			Var vSqlCond =  forCond._declare(Types.String, "_sqlCond", forCond.getVar().arrayIndex("cond"));
			
			forCond._if(PhpFunctions.str_contains.call(vSqlCond,new PhpStringLiteral("\'"))
					._or(PhpFunctions.str_contains.call(vSqlCond,new PhpStringLiteral("\""))
					._or(PhpFunctions.str_contains.call(vSqlCond,new PhpStringLiteral("/*"))
					._or(PhpFunctions.str_contains.call(vSqlCond,new PhpStringLiteral("*/"))
					._or(PhpFunctions.str_contains.call(vSqlCond,new PhpStringLiteral("//"))
					))))).thenBlock().
			addInstr(new ThrowInstruction(Types.Exception, new PhpStringLiteral("invalid SQL")));
			
			forCond.addInstr(new MethodCallInstruction(vEntityQuery.callMethod(ClsBaseEntityQuery.where,vSqlCond,  forCond.getVar().arrayIndex(new PhpStringLiteral("params")))));
			
			ForeachLoop forOrder= ifIssetCondition.thenBlock()._foreach(new Var(Types.Mixed, "_o"), vOrderByJson);
Var vOrderByExpr =  forOrder._declare(Types.String, "_orderByExpr", forOrder.getVar().arrayIndex("expr"));
Var vAsc = forOrder._declare(Types.String, "_orderByAsc", forOrder.getVar().arrayIndex(new PhpStringLiteral("asc")).cast(Types.Bool));			
forOrder._if(PhpFunctions.str_contains.call(vOrderByExpr,new PhpStringLiteral("\'"))
					._or(PhpFunctions.str_contains.call(vOrderByExpr,new PhpStringLiteral("\""))
					._or(PhpFunctions.str_contains.call(vOrderByExpr,new PhpStringLiteral("/*"))
					._or(PhpFunctions.str_contains.call(vOrderByExpr,new PhpStringLiteral("*/"))
					._or(PhpFunctions.str_contains.call(vOrderByExpr,new PhpStringLiteral("//"))
					))))).thenBlock().addInstr(new ThrowInstruction(Types.Exception, new PhpStringLiteral("invalid SQL")));

			forOrder.addInstr(new MethodCallInstruction(vEntityQuery.callMethod(ClsBaseEntityQuery.orderBy,vOrderByExpr, new InlineIfExpression(vAsc,new PhpStringLiteral("asc"),new PhpStringLiteral("desc")))));
		
			Var vEntities = caseEntityType._declare(e.getType(),"entities",vEntityQuery.callMethod( MethodEntityQueryFetch.getMethodName()) );
			Var vResult = caseEntityType._declare(Types.array(Types.String), "result", new ArrayInitExpression());
			ForeachLoop foreachEntity = caseEntityType._foreach(new Var(entity, "entity"), vEntities);
			
			Var entityData =  foreachEntity._declare(Types.array(Types.String), "entityData",foreachEntity.getVar().callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME));
			
			for(OneRelation r : entity.getOneRelations() ) {
				IfBlock ifRelatedEntityIsNotNull = foreachEntity._if(foreachEntity.getVar().callMethod( OrmUtil.getOneRelationDestAttrGetter(r)).isNotNull());
				
				Var relationEntityData =ifRelatedEntityIsNotNull.thenBlock()._declare(Types.array(Types.String), "relationEntityData_"+r.getAlias(),foreachEntity.getVar().callMethod( OrmUtil.getOneRelationDestAttrGetter(r)).callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME));
				ifRelatedEntityIsNotNull.thenBlock().addInstr( entityData.arrayIndexSet(new PhpStringLiteral(OrmUtil.getOneRelationDestAttrName(r)), relationEntityData));
			}
			for(OneToManyRelation r : entity.getOneToManyRelations() ) {
				caseEntityType.addInstr( entityData.arrayIndexSet(new PhpStringLiteral(OrmUtil.getOneToManyRelationDestAttrNameSingular(r)), new ArrayInitExpression()));
				Var arrRelationEntities =caseEntityType._declare(Types.array(Types.Mixed),"relationEntities", foreachEntity.getVar().callAttrGetter( OrmUtil.getOneToManyRelationDestAttrName(r)));
				ForeachLoop foreachRelationEntity = caseEntityType._foreach(new Var(Entities.get(r.getDestTable()), "relationEntity"+r.getAlias() ), arrRelationEntities);
				
				foreachRelationEntity._arrayPush( entityData.arrayIndex(new PhpStringLiteral(OrmUtil.getOneToManyRelationDestAttrNameSingular(r))), foreachRelationEntity.getVar().callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME));
			}
			for(ManyRelation r : entity.getManyRelations() ) {
				caseEntityType.addInstr( entityData.arrayIndexSet(new PhpStringLiteral(OrmUtil.getManyRelationDestAttrNameSingular(r)), new ArrayInitExpression()));
				Var arrRelationEntities =caseEntityType._declare(Types.array(Types.Mixed),"relationEntities", foreachEntity.getVar().callAttrGetter( OrmUtil.getManyRelationDestAttrName(r)));
				ForeachLoop foreachRelationEntity = caseEntityType._foreach(new Var(Entities.get(r.getDestTable()), "relationEntity"+r.getAlias() ), arrRelationEntities);
				
				foreachRelationEntity._arrayPush( entityData.arrayIndex(new PhpStringLiteral(OrmUtil.getManyRelationDestAttrNameSingular(r))), foreachRelationEntity.getVar().callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME));
			}
			
			foreachEntity.addInstr( vResult.arrayPush(entityData) );
			
			caseEntityType.addInstr(PhpFunctions.echo(PhpFunctions.json_encode.call(vResult))); 
			caseEntityType._break();
		}
//		}
	}

}
