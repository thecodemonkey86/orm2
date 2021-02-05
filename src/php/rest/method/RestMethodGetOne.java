package php.rest.method;

import java.util.Collection;

import php.orm.OrmUtil;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import php.bean.Entities;
import php.bean.EntityCls;
import php.bean.method.MethodGetFieldsAsAssocArray;
import php.core.PhpConstants;
import php.core.PhpFunctions;
import php.core.PhpGlobals;
import php.core.Types;
import php.core.expression.Expression;
import php.core.expression.PhpStringLiteral;
import php.core.expression.Var;
import php.core.instruction.CaseBlock;
import php.core.instruction.ForeachLoop;
import php.core.instruction.IfBlock;
import php.core.instruction.SwitchBlock;
import php.core.method.Method;
import php.lib.ClsBaseEntityQuery;

public class RestMethodGetOne extends Method {

	Collection<EntityCls> beans;
	
	public RestMethodGetOne(Collection<EntityCls> beans) {
		super(Public, Types.Void, "getOne");
		setStatic(true);
		this.beans = beans;
	}

	@Override
	public void addImplementation() {
		SwitchBlock switchEntityType = _switch(PhpGlobals.$_GET.arrayIndex(new PhpStringLiteral("entityType")));
		for(EntityCls bean : beans) {
			CaseBlock caseBeanType = switchEntityType._case(new PhpStringLiteral(bean.getName()));
			
			Expression e = Types.BeanRepository.callStaticMethod("createQuery"+bean.getName())
					.callMethod(ClsBaseEntityQuery.select);
			Var vQuery = caseBeanType._declare(e.getType(),"query"+bean.getName(),e);
			IfBlock ifIssetCondition= caseBeanType
					._if(PhpFunctions.isset.call(PhpGlobals.$_GET.arrayIndex(new PhpStringLiteral("condition")))) ;
			ifIssetCondition.thenBlock().addInstr(vQuery.callMethodInstruction(ClsBaseEntityQuery.where,PhpGlobals.$_GET.arrayIndex(new PhpStringLiteral("condition"))));
					;
					Var vEntity = caseBeanType._declare(bean, "entity", vQuery.callMethod("fetchOne"));
					
					Var beanData =  caseBeanType._declare(Types.array(Types.String), "entityData",vEntity.callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME));
					
					for(OneRelation r : bean.getOneRelations() ) {
						IfBlock ifRelatedBeanIsNotNull = caseBeanType._if(vEntity.callMethod( OrmUtil.getOneRelationDestAttrGetter(r)).isNotNull());
						
						Var relationBeanData =ifRelatedBeanIsNotNull.thenBlock()._declare(Types.array(Types.String), "relationBeanData_"+r.getAlias(),vEntity.callMethod( OrmUtil.getOneRelationDestAttrGetter(r)).callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME));
						ifRelatedBeanIsNotNull.thenBlock().addInstr( beanData.arrayIndexSet(new PhpStringLiteral(OrmUtil.getOneRelationDestAttrName(r)), relationBeanData));
					}
					for(OneToManyRelation r : bean.getOneToManyRelations() ) {
						Var arrRelationBeans =caseBeanType._declare(Types.array(Types.Mixed),"relationBeans", vEntity.callAttrGetter( OrmUtil.getOneToManyRelationDestAttrName(r)));
						ForeachLoop foreachRelationBean = caseBeanType._foreach(new Var(Entities.get(r.getDestTable()), "relationBean"+r.getAlias() ), arrRelationBeans);
						
						Var relationBeanData= foreachRelationBean._declare(Types.array(Types.String), "relationBeanData_"+r.getAlias(), foreachRelationBean.getVar().callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME));
						beanData.arrayIndexSet(new PhpStringLiteral(OrmUtil.getOneToManyRelationDestAttrNameSingular(r)), relationBeanData);
					}
					for(ManyRelation r : bean.getManyRelations() ) {
						Var arrRelationBeans =caseBeanType._declare(Types.array(Types.Mixed),"relationBeans", vEntity.callAttrGetter( OrmUtil.getManyRelationDestAttrName(r)));
						ForeachLoop foreachRelationBean = caseBeanType._foreach(new Var(Entities.get(r.getDestTable()), "relationBean"+r.getAlias() ), arrRelationBeans);
						
						Var relationBeanData= foreachRelationBean._declare(Types.array(Types.String), "relationBeanData_"+r.getAlias(), foreachRelationBean.getVar().callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME));
						beanData.arrayIndexSet(new PhpStringLiteral(OrmUtil.getManyRelationDestAttrNameSingular(r)), relationBeanData);
					}
					caseBeanType.addInstr(PhpFunctions.echo(PhpFunctions.json_encode.call(vEntity.callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME), PhpConstants.JSON_UNESCAPED_UNICODE)));
			caseBeanType._break();
		}

	}

}
