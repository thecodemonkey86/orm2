package php.rest.method;

import java.util.Collection;

import cpp.orm.OrmUtil;
import database.column.Column;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import database.relation.PrimaryKey;
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
import php.entity.Entities;
import php.entity.EntityCls;
import php.entity.method.MethodGetFieldsAsAssocArray;
import php.entityrepository.method.MethodGetById;

public class RestMethodGetById extends Method {

	Collection<EntityCls> entities;
	
	public RestMethodGetById(Collection<EntityCls> entities) {
		super(Public, Types.Void, "getById");
		setStatic(true);
		this.entities = entities;
	}

	@Override
	public void addImplementation() {
		SwitchBlock switchEntityType = _switch(PhpGlobals.$_GET.arrayIndex(new PhpStringLiteral("entityType")));
		for(EntityCls entity : entities) {
			CaseBlock caseBeanType = switchEntityType._case(new PhpStringLiteral(entity.getName()));
			
			PrimaryKey pk= entity.getTbl().getPrimaryKey();
			
			
		Expression[] args=new Expression[pk.getColumnCount()];
		int i=0;
			for(Column pkCol : pk) {
				args[i++] = PhpGlobals.$_GET.arrayIndex(new PhpStringLiteral(pkCol.getName() ));
			}
			Expression e = Types.EntityRepository.callStaticMethod(MethodGetById.getMethodName(entity),args);
			
			Var vBean = caseBeanType._declare(e.getType(),"entity",e );
			
			Var beanData =  caseBeanType._declare(Types.array(Types.String), "entityData",vBean.callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME));
			
			for(OneRelation r : entity.getOneRelations() ) {
				IfBlock ifRelatedBeanIsNotNull = caseBeanType._if(vBean.callMethod( OrmUtil.getOneRelationDestAttrGetter(r)).isNotNull());
				
				Var relationBeanData =ifRelatedBeanIsNotNull.thenBlock()._declare(Types.array(Types.String), "relationBeanData_"+r.getAlias(),vBean.callMethod( OrmUtil.getOneRelationDestAttrGetter(r)).callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME));
				ifRelatedBeanIsNotNull.thenBlock().addInstr( beanData.arrayIndexSet(new PhpStringLiteral(OrmUtil.getOneRelationDestAttrName(r)), relationBeanData));
			}
			for(OneToManyRelation r : entity.getOneToManyRelations() ) {
				caseBeanType.addInstr( beanData.arrayIndexSet(new PhpStringLiteral(OrmUtil.getOneToManyRelationDestAttrNameSingular(r)), new ArrayInitExpression()));
				Var arrRelationBeans =caseBeanType._declare(Types.array(Types.Mixed),"relationBeans", vBean.callAttrGetter( OrmUtil.getOneToManyRelationDestAttrName(r)));
				ForeachLoop foreachRelationBean = caseBeanType._foreach(new Var(Entities.get(r.getDestTable()), "relationBean"+r.getAlias() ), arrRelationBeans);
				
				foreachRelationBean._arrayPush( beanData.arrayIndex(new PhpStringLiteral(OrmUtil.getOneToManyRelationDestAttrNameSingular(r))), foreachRelationBean.getVar().callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME));
			}
			for(ManyRelation r : entity.getManyRelations() ) {
				caseBeanType.addInstr( beanData.arrayIndexSet(new PhpStringLiteral(OrmUtil.getManyRelationDestAttrNameSingular(r)), new ArrayInitExpression()));
				Var arrRelationBeans =caseBeanType._declare(Types.array(Types.Mixed),"relationBeans", vBean.callAttrGetter( OrmUtil.getManyRelationDestAttrName(r)));
				ForeachLoop foreachRelationBean = caseBeanType._foreach(new Var(Entities.get(r.getDestTable()), "relationBean"+r.getAlias() ), arrRelationBeans);
				
				foreachRelationBean._arrayPush( beanData.arrayIndex(new PhpStringLiteral(OrmUtil.getManyRelationDestAttrNameSingular(r))), foreachRelationBean.getVar().callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME));
			}
			
			caseBeanType.addInstr(PhpFunctions.echo(PhpFunctions.json_encode.call(beanData, PhpConstants.JSON_UNESCAPED_UNICODE)));
			caseBeanType._break();
		}


	}

}
