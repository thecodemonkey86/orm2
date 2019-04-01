package php.rest.method;

import java.util.Collection;

import cpp.orm.OrmUtil;
import database.column.Column;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import database.relation.PrimaryKey;
import php.bean.BeanCls;
import php.bean.Beans;
import php.bean.method.MethodGetFieldsAsAssocArray;
import php.core.PhpConstants;
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

public class RestMethodGetById extends Method {

	Collection<BeanCls> beans;
	
	public RestMethodGetById(Collection<BeanCls> beans) {
		super(Public, Types.Void, "getById");
		setStatic(true);
		this.beans = beans;
	}

	@Override
	public void addImplementation() {
		SwitchBlock switchEntityType = _switch(PhpGlobals.$_GET.arrayIndex(new PhpStringLiteral("entityType")));
		for(BeanCls bean : beans) {
			CaseBlock caseBeanType = switchEntityType._case(new PhpStringLiteral(bean.getName()));
			
			PrimaryKey pk= bean.getTbl().getPrimaryKey();
			
			Expression e = Types.BeanRepository.callStaticMethod("createQuery"+bean.getName())
					.callMethod(ClsBaseBeanQuery.select);
				
			for(Column pkCol : pk) {
				e = e.callMethod(ClsBaseBeanQuery.where, new PhpStringLiteral("b1."+pkCol.getEscapedName()+ "=?"), PhpGlobals.$_GET.arrayIndex(new PhpStringLiteral(pkCol.getName() )));
			}
			
			e = e.callMethod("fetchOne");
			Var vBean = caseBeanType._declare(e.getType(),"bean",e );
			
			Var beanData =  caseBeanType._declare(Types.array(Types.String), "beanData",vBean.callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME));
			
			for(OneRelation r : bean.getOneRelations() ) {
				IfBlock ifRelatedBeanIsNotNull = caseBeanType._if(vBean.callMethod( OrmUtil.getOneRelationDestAttrGetter(r)).isNotNull());
				
				Var relationBeanData =ifRelatedBeanIsNotNull.thenBlock()._declare(Types.array(Types.String), "relationBeanData_"+r.getAlias(),vBean.callMethod( OrmUtil.getOneRelationDestAttrGetter(r)).callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME));
				ifRelatedBeanIsNotNull.thenBlock().addInstr( beanData.arrayIndexSet(new PhpStringLiteral(OrmUtil.getOneRelationDestAttrName(r)), relationBeanData));
			}
			for(OneToManyRelation r : bean.getOneToManyRelations() ) {
				caseBeanType.addInstr( beanData.arrayIndexSet(new PhpStringLiteral(OrmUtil.getOneToManyRelationDestAttrNameSingular(r)), new ArrayInitExpression()));
				Var arrRelationBeans =caseBeanType._declare(Types.array(Types.Mixed),"relationBeans", vBean.callAttrGetter( OrmUtil.getOneToManyRelationDestAttrName(r)));
				ForeachLoop foreachRelationBean = caseBeanType._foreach(new Var(Beans.get(r.getDestTable()), "relationBean"+r.getAlias() ), arrRelationBeans);
				
				foreachRelationBean._arrayPush( beanData.arrayIndex(new PhpStringLiteral(OrmUtil.getOneToManyRelationDestAttrNameSingular(r))), foreachRelationBean.getVar().callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME));
			}
			for(ManyRelation r : bean.getManyRelations() ) {
				caseBeanType.addInstr( beanData.arrayIndexSet(new PhpStringLiteral(OrmUtil.getManyRelationDestAttrNameSingular(r)), new ArrayInitExpression()));
				Var arrRelationBeans =caseBeanType._declare(Types.array(Types.Mixed),"relationBeans", vBean.callAttrGetter( OrmUtil.getManyRelationDestAttrName(r)));
				ForeachLoop foreachRelationBean = caseBeanType._foreach(new Var(Beans.get(r.getDestTable()), "relationBean"+r.getAlias() ), arrRelationBeans);
				
				foreachRelationBean._arrayPush( beanData.arrayIndex(new PhpStringLiteral(OrmUtil.getManyRelationDestAttrNameSingular(r))), foreachRelationBean.getVar().callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME));
			}
			
			caseBeanType.addInstr(PhpFunctions.echo(PhpFunctions.json_encode.call(beanData, PhpConstants.JSON_UNESCAPED_UNICODE)));
			caseBeanType._break();
		}


	}

}
