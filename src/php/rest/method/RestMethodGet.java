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
import php.core.expression.Expression;
import php.core.expression.PhpStringLiteral;
import php.core.expression.Var;
import php.core.instruction.CaseBlock;
import php.core.instruction.ForeachLoop;
import php.core.instruction.IfBlock;
import php.core.instruction.SwitchBlock;
import php.core.method.Method;
import php.entity.Entities;
import php.entity.EntityCls;
import php.entity.method.MethodGetFieldsAsAssocArray;
import php.entityrepository.query.method.MethodEntityQueryFetch;
import php.entityrepository.query.method.MethodEntityQueryFetchOne;
import php.lib.ClsBaseEntityQuery;

public class RestMethodGet extends Method {

	Collection<EntityCls> entities;
	public RestMethodGet(Collection<EntityCls> entities) {
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
			Var vQuery = caseBeanType._declare(e.getType(),"query"+entity.getName(),e);
			IfBlock ifIssetCondition= caseBeanType
					._if(PhpFunctions.isset.call(PhpGlobals.$_GET.arrayIndex(new PhpStringLiteral("condition")))) ;
			ifIssetCondition.thenBlock().addInstr(vQuery.callMethodInstruction(ClsBaseEntityQuery.where,PhpGlobals.$_GET.arrayIndex(new PhpStringLiteral("condition"))));
			Var vBeans = caseBeanType._declare(e.getType(),"entities",vQuery.callMethod( MethodEntityQueryFetch.getMethodName()) );
			Var vResult = caseBeanType._declare(Types.array(Types.String), "result", new ArrayInitExpression());
			ForeachLoop foreachBean = caseBeanType._foreach(new Var(entity, "entity"), vBeans);
			
			Var beanData =  foreachBean._declare(Types.array(Types.String), "entityData",foreachBean.getVar().callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME));
			
			for(OneRelation r : entity.getOneRelations() ) {
				IfBlock ifRelatedBeanIsNotNull = foreachBean._if(foreachBean.getVar().callMethod( OrmUtil.getOneRelationDestAttrGetter(r)).isNotNull());
				
				Var relationBeanData =ifRelatedBeanIsNotNull.thenBlock()._declare(Types.array(Types.String), "relationBeanData_"+r.getAlias(),foreachBean.getVar().callMethod( OrmUtil.getOneRelationDestAttrGetter(r)).callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME));
				ifRelatedBeanIsNotNull.thenBlock().addInstr( beanData.arrayIndexSet(new PhpStringLiteral(OrmUtil.getOneRelationDestAttrName(r)), relationBeanData));
			}
			for(OneToManyRelation r : entity.getOneToManyRelations() ) {
				Var arrRelationBeans =foreachBean._declare(Types.array(Types.Mixed),"relationBeans", foreachBean.getVar().callAttrGetter( OrmUtil.getOneToManyRelationDestAttrName(r)));
				ForeachLoop foreachRelationBean = foreachBean._foreach(new Var(Entities.get(r.getDestTable()), "relationBean"+r.getAlias() ), arrRelationBeans);
				
				Var relationBeanData= foreachRelationBean._declare(Types.array(Types.String), "relationBeanData_"+r.getAlias(), foreachRelationBean.getVar().callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME));
				beanData.arrayIndexSet(new PhpStringLiteral(OrmUtil.getOneToManyRelationDestAttrNameSingular(r)), relationBeanData);
			}
			for(ManyRelation r : entity.getManyRelations() ) {
				Var arrRelationBeans =foreachBean._declare(Types.array(Types.Mixed),"relationBeans", foreachBean.getVar().callAttrGetter( OrmUtil.getManyRelationDestAttrName(r)));
				ForeachLoop foreachRelationBean = foreachBean._foreach(new Var(Entities.get(r.getDestTable()), "relationBean"+r.getAlias() ), arrRelationBeans);
				
				Var relationBeanData= foreachRelationBean._declare(Types.array(Types.String), "relationBeanData_"+r.getAlias(), foreachRelationBean.getVar().callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME));
				beanData.arrayIndexSet(new PhpStringLiteral(OrmUtil.getManyRelationDestAttrNameSingular(r)), relationBeanData);
			}
			
			foreachBean.addInstr( vResult.arrayPush(beanData) );
			
			caseBeanType.addInstr(PhpFunctions.echo(PhpFunctions.json_encode.call(vResult))); 
			caseBeanType._break();
		}

	}

}
