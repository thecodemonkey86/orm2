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
			CaseBlock caseBeanType = switchEntityType._case(new PhpStringLiteral(entity.getName()));
			
			Expression e = Types.EntityRepository.callStaticMethod("createQuery"+entity.getName())
					.callMethod(ClsBaseEntityQuery.select);
			Var vEntityQuery = caseBeanType._declare(e.getType(),"query"+entity.getName(),e);
			IfBlock ifIssetCondition= caseBeanType
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
		/*	IfBlock ifIssetOrderBy= caseBeanType
					._if(PhpFunctions.isset.call(PhpGlobals.$_GET.arrayIndex(new PhpStringLiteral("orderBy")))) ;
			ifIssetOrderBy.thenBlock().addInstr(vEntityQuery.callMethodInstruction(ClsBaseEntityQuery.orderBy,PhpGlobals.$_GET.arrayIndex(new PhpStringLiteral("orderBy"))));*/
			Var vBeans = caseBeanType._declare(e.getType(),"entities",vEntityQuery.callMethod( MethodEntityQueryFetch.getMethodName()) );
			Var vResult = caseBeanType._declare(Types.array(Types.String), "result", new ArrayInitExpression());
			ForeachLoop foreachBean = caseBeanType._foreach(new Var(entity, "entity"), vBeans);
			
			Var beanData =  foreachBean._declare(Types.array(Types.String), "entityData",foreachBean.getVar().callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME));
			
			for(OneRelation r : entity.getOneRelations() ) {
				IfBlock ifRelatedBeanIsNotNull = foreachBean._if(foreachBean.getVar().callMethod( OrmUtil.getOneRelationDestAttrGetter(r)).isNotNull());
				
				Var relationBeanData =ifRelatedBeanIsNotNull.thenBlock()._declare(Types.array(Types.String), "relationBeanData_"+r.getAlias(),foreachBean.getVar().callMethod( OrmUtil.getOneRelationDestAttrGetter(r)).callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME));
				ifRelatedBeanIsNotNull.thenBlock().addInstr( beanData.arrayIndexSet(new PhpStringLiteral(OrmUtil.getOneRelationDestAttrName(r)), relationBeanData));
			}
			for(OneToManyRelation r : entity.getOneToManyRelations() ) {
				caseBeanType.addInstr( beanData.arrayIndexSet(new PhpStringLiteral(OrmUtil.getOneToManyRelationDestAttrNameSingular(r)), new ArrayInitExpression()));
				Var arrRelationBeans =caseBeanType._declare(Types.array(Types.Mixed),"relationBeans", foreachBean.getVar().callAttrGetter( OrmUtil.getOneToManyRelationDestAttrName(r)));
				ForeachLoop foreachRelationBean = caseBeanType._foreach(new Var(Entities.get(r.getDestTable()), "relationBean"+r.getAlias() ), arrRelationBeans);
				
				foreachRelationBean._arrayPush( beanData.arrayIndex(new PhpStringLiteral(OrmUtil.getOneToManyRelationDestAttrNameSingular(r))), foreachRelationBean.getVar().callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME));
			}
			for(ManyRelation r : entity.getManyRelations() ) {
				caseBeanType.addInstr( beanData.arrayIndexSet(new PhpStringLiteral(OrmUtil.getManyRelationDestAttrNameSingular(r)), new ArrayInitExpression()));
				Var arrRelationBeans =caseBeanType._declare(Types.array(Types.Mixed),"relationBeans", foreachBean.getVar().callAttrGetter( OrmUtil.getManyRelationDestAttrName(r)));
				ForeachLoop foreachRelationBean = caseBeanType._foreach(new Var(Entities.get(r.getDestTable()), "relationBean"+r.getAlias() ), arrRelationBeans);
				
				foreachRelationBean._arrayPush( beanData.arrayIndex(new PhpStringLiteral(OrmUtil.getManyRelationDestAttrNameSingular(r))), foreachRelationBean.getVar().callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME));
			}
			
			foreachBean.addInstr( vResult.arrayPush(beanData) );
			
			caseBeanType.addInstr(PhpFunctions.echo(PhpFunctions.json_encode.call(vResult))); 
			caseBeanType._break();
		}
//		Var vResult = _declare(Types.array(Types.String), "result", new ArrayInitExpression());
//		SwitchBlock switchEntityType = _switch(PhpGlobals.$_GET.arrayIndex(new PhpStringLiteral("entityType")));
//		for(EntityCls entity : entities) {
//			CaseBlock caseBeanType = switchEntityType._case(new PhpStringLiteral(entity.getName()));
//			
//			
//			Var vEntityQuery= caseBeanType._declare(Types.beanQuery(entity), "_query", Types.EntityRepository.callStaticMethod("createQuery"+entity.getName())
//					.callMethod(ClsBaseEntityQuery.select) );
//			Var vQueryJson =  caseBeanType._declare(Types.array(Types.Mixed), "_json", PhpFunctions.json_decode.call(PhpGlobals.$_GET.arrayIndex(new PhpStringLiteral("condition")) ,BoolExpression.TRUE));
//			Var vBeans = caseBeanType._declare(Types.array(Types.Mixed),"entities",vEntityQuery.callMethod( MethodEntityQueryFetch.getMethodName()) );
//			ForeachLoop foreachBean = caseBeanType._foreach(new Var(entity, "entity"), vBeans);
//			Var vJoinJson = caseBeanType._declare(Types.array(Types.Mixed), "_joinJson",vQueryJson.arrayIndex("joins") );
//			ForeachLoop forJoins= caseBeanType._foreach(new Var(Types.Mixed, "_j"), vJoinJson);
//			Var vSqlJoinTable =  forJoins._declare(Types.String, "_sqlJoinTable", forJoins.getVar().arrayIndex("table"));
//			Var vSqlJoinOn =  forJoins._declare(Types.String, "_sqlJoinOn", forJoins.getVar().arrayIndex("on"));
//			
//			forJoins._if(PhpFunctions.str_contains.call(vSqlJoinTable,new PhpStringLiteral("\'"))
//					._or(PhpFunctions.str_contains.call(vSqlJoinTable,new PhpStringLiteral("\""))
//					._or(PhpFunctions.str_contains.call(vSqlJoinTable,new PhpStringLiteral("/*"))
//					._or(PhpFunctions.str_contains.call(vSqlJoinTable,new PhpStringLiteral("*/"))
//					._or(PhpFunctions.str_contains.call(vSqlJoinTable,new PhpStringLiteral("//"))
//					._or(PhpFunctions.str_contains.call(vSqlJoinOn,new PhpStringLiteral("\'"))
//					._or(PhpFunctions.str_contains.call(vSqlJoinOn,new PhpStringLiteral("\""))
//					._or(PhpFunctions.str_contains.call(vSqlJoinOn,new PhpStringLiteral("/*"))
//					._or(PhpFunctions.str_contains.call(vSqlJoinOn,new PhpStringLiteral("*/"))
//					._or(PhpFunctions.str_contains.call(vSqlJoinOn,new PhpStringLiteral("//"))
//					)))))))))).thenBlock().
//			addInstr(new ThrowInstruction(Types.Exception, new PhpStringLiteral("invalid SQL")));
//			
//			forJoins.addInstr(new MethodCallInstruction(vEntityQuery.callMethod(ClsBaseEntityQuery.join,vSqlJoinTable,vSqlJoinOn,  forJoins.getVar().arrayIndex(new PhpStringLiteral("params")))));
//			
//			Var vCondJson = caseBeanType._declare(Types.array(Types.Mixed), "_condJson",vQueryJson.arrayIndex("conditions") );
//			ForeachLoop forCond= caseBeanType._foreach(new Var(Types.Mixed, "_c"), vCondJson);
//			Var vSqlCond =  forCond._declare(Types.String, "_sqlCond", forCond.getVar().arrayIndex("cond"));
//			
//			forCond._if(PhpFunctions.str_contains.call(vSqlCond,new PhpStringLiteral("\'"))
//					._or(PhpFunctions.str_contains.call(vSqlCond,new PhpStringLiteral("\""))
//					._or(PhpFunctions.str_contains.call(vSqlCond,new PhpStringLiteral("/*"))
//					._or(PhpFunctions.str_contains.call(vSqlCond,new PhpStringLiteral("*/"))
//					._or(PhpFunctions.str_contains.call(vSqlCond,new PhpStringLiteral("//"))
//					))))).thenBlock().
//			addInstr(new ThrowInstruction(Types.Exception, new PhpStringLiteral("invalid SQL")));
//			
//			forCond.addInstr(new MethodCallInstruction(vEntityQuery.callMethod(ClsBaseEntityQuery.where,vSqlCond,  forCond.getVar().arrayIndex(new PhpStringLiteral("params")))));
//			
//			Var vBean = caseBeanType._declare(entity,"entity",vEntityQuery.callMethod(MethodEntityQueryFetchOne.getMethodName()) );
//			
//			Var beanData =  caseBeanType._declare(Types.array(Types.String), "entityData",vBean.callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME));
//			
//			for(OneRelation r : entity.getOneRelations() ) {
//				IfBlock ifRelatedBeanIsNotNull = caseBeanType._if(vBean.callMethod( OrmUtil.getOneRelationDestAttrGetter(r)).isNotNull());
//				
//				Var relationBeanData =ifRelatedBeanIsNotNull.thenBlock()._declare(Types.array(Types.String), "relationBeanData_"+r.getAlias(),vBean.callMethod( OrmUtil.getOneRelationDestAttrGetter(r)).callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME));
//				ifRelatedBeanIsNotNull.thenBlock().addInstr( beanData.arrayIndexSet(new PhpStringLiteral(OrmUtil.getOneRelationDestAttrName(r)), relationBeanData));
//			}
//			for(OneToManyRelation r : entity.getOneToManyRelations() ) {
//				caseBeanType.addInstr( beanData.arrayIndexSet(new PhpStringLiteral(OrmUtil.getOneToManyRelationDestAttrNameSingular(r)), new ArrayInitExpression()));
//				Var arrRelationBeans =caseBeanType._declare(Types.array(Types.Mixed),"relationBeans", vBean.callAttrGetter( OrmUtil.getOneToManyRelationDestAttrName(r)));
//				ForeachLoop foreachRelationBean = caseBeanType._foreach(new Var(Entities.get(r.getDestTable()), "relationBean"+r.getAlias() ), arrRelationBeans);
//				
//				foreachRelationBean._arrayPush( beanData.arrayIndex(new PhpStringLiteral(OrmUtil.getOneToManyRelationDestAttrNameSingular(r))), foreachRelationBean.getVar().callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME));
//			}
//			for(ManyRelation r : entity.getManyRelations() ) {
//				caseBeanType.addInstr( beanData.arrayIndexSet(new PhpStringLiteral(OrmUtil.getManyRelationDestAttrNameSingular(r)), new ArrayInitExpression()));
//				Var arrRelationBeans =caseBeanType._declare(Types.array(Types.Mixed),"relationBeans", vBean.callAttrGetter( OrmUtil.getManyRelationDestAttrName(r)));
//				ForeachLoop foreachRelationBean = caseBeanType._foreach(new Var(Entities.get(r.getDestTable()), "relationBean"+r.getAlias() ), arrRelationBeans);
//				
//				foreachRelationBean._arrayPush( beanData.arrayIndex(new PhpStringLiteral(OrmUtil.getManyRelationDestAttrNameSingular(r))), foreachRelationBean.getVar().callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME));
//			}
//			
//			foreachBean.addInstr( vResult.arrayPush(beanData) );
//			caseBeanType.addInstr(PhpFunctions.echo(PhpFunctions.json_encode.call(vResult))); 
//			caseBeanType._break();
//		}
	}

}
