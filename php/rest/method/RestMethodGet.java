package php.rest.method;

import java.util.Collection;

import cpp.orm.OrmUtil;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import php.bean.BeanCls;
import php.bean.Beans;
import php.bean.method.MethodGetFieldsAsAssocArray;
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
import php.lib.ClsBaseBeanQuery;

public class RestMethodGet extends Method {

	Collection<BeanCls> beans;
	
	public RestMethodGet(Collection<BeanCls> beans) {
		super(Public, Types.Void, "get");
		setStatic(true);
		this.beans = beans;
	}

	@Override
	public void addImplementation() {
		SwitchBlock switchEntityType = _switch(PhpGlobals.$_GET.arrayIndex(new PhpStringLiteral("entityType")));
		for(BeanCls bean : beans) {
			CaseBlock caseBeanType = switchEntityType._case(new PhpStringLiteral(bean.getName()));
			
			Expression e = Types.BeanRepository.callStaticMethod("createQuery"+bean.getName())
					.callMethod(ClsBaseBeanQuery.select);
			Var vQuery = caseBeanType._declare(e.getType(),"query"+bean.getName(),e);
			IfBlock ifIssetCondition= caseBeanType
					._if(PhpFunctions.isset.call(PhpGlobals.$_GET.arrayIndex(new PhpStringLiteral("condition")))) ;
			ifIssetCondition.thenBlock().addInstr(vQuery.callMethodInstruction(ClsBaseBeanQuery.where,PhpGlobals.$_GET.arrayIndex(new PhpStringLiteral("condition"))));
			Var vBeans = caseBeanType._declare(e.getType(),"beans",vQuery.callMethod("fetch") );
			Var vResult = caseBeanType._declare(Types.array(Types.String), "result", new ArrayInitExpression());
			ForeachLoop foreachBean = caseBeanType._foreach(new Var(bean, "bean"), vBeans);
			
			Var beanData =  foreachBean._declare(Types.array(Types.String), "beanData",foreachBean.getVar().callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME));
			
			for(OneRelation r : bean.getOneRelations() ) {
				IfBlock ifRelatedBeanIsNotNull = foreachBean._if(foreachBean.getVar().callMethod( OrmUtil.getOneRelationDestAttrGetter(r)).isNotNull());
				
				Var relationBeanData =ifRelatedBeanIsNotNull.thenBlock()._declare(Types.array(Types.String), "relationBeanData_"+r.getAlias(),foreachBean.getVar().callMethod( OrmUtil.getOneRelationDestAttrGetter(r)).callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME));
				ifRelatedBeanIsNotNull.thenBlock().addInstr( beanData.arrayIndexSet(new PhpStringLiteral(OrmUtil.getOneRelationDestAttrName(r)), relationBeanData));
			}
			for(OneToManyRelation r : bean.getOneToManyRelations() ) {
				Var arrRelationBeans =foreachBean._declare(Types.array(Types.Mixed),"relationBeans", foreachBean.getVar().callAttrGetter( OrmUtil.getOneToManyRelationDestAttrName(r)));
				ForeachLoop foreachRelationBean = foreachBean._foreach(new Var(Beans.get(r.getDestTable()), "relationBean"+r.getAlias() ), arrRelationBeans);
				
				Var relationBeanData= foreachRelationBean._declare(Types.array(Types.String), "relationBeanData_"+r.getAlias(), foreachRelationBean.getVar().callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME));
				beanData.arrayIndexSet(new PhpStringLiteral(OrmUtil.getOneToManyRelationDestAttrNameSingular(r)), relationBeanData);
			}
			for(ManyRelation r : bean.getManyRelations() ) {
				Var arrRelationBeans =foreachBean._declare(Types.array(Types.Mixed),"relationBeans", foreachBean.getVar().callAttrGetter( OrmUtil.getManyRelationDestAttrName(r)));
				ForeachLoop foreachRelationBean = foreachBean._foreach(new Var(Beans.get(r.getDestTable()), "relationBean"+r.getAlias() ), arrRelationBeans);
				
				Var relationBeanData= foreachRelationBean._declare(Types.array(Types.String), "relationBeanData_"+r.getAlias(), foreachRelationBean.getVar().callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME));
				beanData.arrayIndexSet(new PhpStringLiteral(OrmUtil.getManyRelationDestAttrNameSingular(r)), relationBeanData);
			}
			
			foreachBean.addInstr( vResult.arrayPush(beanData) );
			
			caseBeanType.addInstr(PhpFunctions.echo(PhpFunctions.json_encode.call(vResult))); 
			caseBeanType._break();
		}

	}

}
