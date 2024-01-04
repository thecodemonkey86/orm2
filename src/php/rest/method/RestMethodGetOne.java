package php.rest.method;

import java.util.Collection;

import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import php.core.PhpConstants;
import php.core.PhpFunctions;
import php.core.PhpGlobals;
import php.core.Types;
import php.core.expression.ArrayInitExpression;
import php.core.expression.BoolExpression;
import php.core.expression.IntExpression;
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
import php.entityrepository.query.method.MethodEntityQueryFetchOne;
import php.lib.ClsBaseEntityQuery;
import php.orm.OrmUtil;

public class RestMethodGetOne extends Method {

	Collection<EntityCls> beans;
	
	public RestMethodGetOne(Collection<EntityCls> beans) {
		super(Public, Types.Void, "getOne");
		setStatic(true);
		this.beans = beans;
	}

	@Override
	public void addImplementation() {
		Var vQueryJson =   _declare(Types.array(Types.Mixed), "_json", PhpFunctions.json_decode.call(PhpGlobals.$_GET.arrayIndex(new PhpStringLiteral("condition")) ,BoolExpression.TRUE));
		SwitchBlock switchEntityType = _switch(PhpGlobals.$_GET.arrayIndex(new PhpStringLiteral("entityType")));
		
		for(EntityCls entity : beans) {
			CaseBlock caseBeanType = switchEntityType._case(new PhpStringLiteral(entity.getName()));
			Var vEntityQuery= caseBeanType._declare(Types.beanQuery(entity), "_query", Types.EntityRepository.callStaticMethod("createQuery"+entity.getName())
					.callMethod(ClsBaseEntityQuery.select) );
			
			IfBlock ifJoinsSet = caseBeanType._if(PhpFunctions.isset.call(vQueryJson.arrayIndex("joins") ));
			Var vJoinJson = ifJoinsSet.thenBlock()._declare(Types.array(Types.Mixed), "_joinJson",vQueryJson.arrayIndex("joins") );
			ForeachLoop forJoins= ifJoinsSet.thenBlock()._foreach(new Var(Types.Mixed, "_j"), vJoinJson);
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
			IfBlock ifConditionsSet = caseBeanType._if(PhpFunctions.isset.call(vQueryJson.arrayIndex("conditions") ));
			Var vCondJson = ifConditionsSet.thenBlock()._declare(Types.array(Types.Mixed), "_condJson",vQueryJson.arrayIndex("conditions") );
			ForeachLoop forCond=  ifConditionsSet.thenBlock()._foreach(new Var(Types.Mixed, "_c"), vCondJson);
			Var vSqlCond =  forCond._declare(Types.String, "_sqlCond", forCond.getVar().arrayIndex("cond"));
			
			forCond._if(PhpFunctions.str_contains.call(vSqlCond,new PhpStringLiteral("\'"))
					._or(PhpFunctions.str_contains.call(vSqlCond,new PhpStringLiteral("\""))
					._or(PhpFunctions.str_contains.call(vSqlCond,new PhpStringLiteral("/*"))
					._or(PhpFunctions.str_contains.call(vSqlCond,new PhpStringLiteral("*/"))
					._or(PhpFunctions.str_contains.call(vSqlCond,new PhpStringLiteral("//"))
					))))).thenBlock().
			addInstr(new ThrowInstruction(Types.Exception, new PhpStringLiteral("invalid SQL")));
			
			forCond.addInstr(new MethodCallInstruction(vEntityQuery.callMethod(ClsBaseEntityQuery.where,vSqlCond,  forCond.getVar().arrayIndex(new PhpStringLiteral("params")))));
			
			Var vEntity = caseBeanType._declare(entity,"entity",vEntityQuery.callMethod(MethodEntityQueryFetchOne.getMethodName()) );
			IfBlock ifEntityIsNull= caseBeanType._if(vEntity.isNull());
			ifEntityIsNull.thenBlock().addInstr(PhpFunctions.exit.call(new IntExpression(0)).asInstruction());
			Var entityData =  caseBeanType._declare(Types.array(Types.String), "entityData",vEntity.callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME));
			
			for(OneRelation r : entity.getOneRelations() ) {
				IfBlock ifRelatedBeanIsNotNull = caseBeanType._if(vEntity.callMethod( OrmUtil.getOneRelationDestAttrGetter(r)).isNotNull());
				
				Var relationBeanData =ifRelatedBeanIsNotNull.thenBlock()._declare(Types.array(Types.String), "relationBeanData_"+r.getAlias(),vEntity.callMethod( OrmUtil.getOneRelationDestAttrGetter(r)).callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME));
				ifRelatedBeanIsNotNull.thenBlock().addInstr( entityData.arrayIndexSet(new PhpStringLiteral(OrmUtil.getOneRelationDestAttrName(r)), relationBeanData));
			}
			for(OneToManyRelation r : entity.getOneToManyRelations() ) {
				caseBeanType.addInstr( entityData.arrayIndexSet(new PhpStringLiteral(OrmUtil.getOneToManyRelationDestAttrNameSingular(r)), new ArrayInitExpression()));
				Var arrRelationBeans =caseBeanType._declare(Types.array(Types.Mixed),"relationBeans", vEntity.callAttrGetter( OrmUtil.getOneToManyRelationDestAttrName(r)));
				ForeachLoop foreachRelationBean = caseBeanType._foreach(new Var(Entities.get(r.getDestTable()), "relationBean"+r.getAlias() ), arrRelationBeans);
				
				foreachRelationBean._arrayPush( entityData.arrayIndex(new PhpStringLiteral(OrmUtil.getOneToManyRelationDestAttrNameSingular(r))), foreachRelationBean.getVar().callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME));
			}
			for(ManyRelation r : entity.getManyRelations() ) {
				caseBeanType.addInstr( entityData.arrayIndexSet(new PhpStringLiteral(OrmUtil.getManyRelationDestAttrNameSingular(r)), new ArrayInitExpression()));
				Var arrRelationBeans =caseBeanType._declare(Types.array(Types.Mixed),"relationBeans", vEntity.callAttrGetter( OrmUtil.getManyRelationDestAttrName(r)));
				ForeachLoop foreachRelationBean = caseBeanType._foreach(new Var(Entities.get(r.getDestTable()), "relationBean"+r.getAlias() ), arrRelationBeans);
				
				foreachRelationBean._arrayPush( entityData.arrayIndex(new PhpStringLiteral(OrmUtil.getManyRelationDestAttrNameSingular(r))), foreachRelationBean.getVar().callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME));
			}
			
			caseBeanType.addInstr(PhpFunctions.echo(PhpFunctions.json_encode.call(entityData, PhpConstants.JSON_UNESCAPED_UNICODE)));
			caseBeanType._break();
		}

	}

}
