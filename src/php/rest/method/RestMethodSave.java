package php.rest.method;

import java.util.Collection;

import database.column.Column;
import php.core.PhpConstants;
import php.core.PhpFunctions;
import php.core.PhpGlobals;
import php.core.Types;
import php.core.expression.ArrayInitExpression;
import php.core.expression.BoolExpression;
import php.core.expression.Expression;
import php.core.expression.InlineIfExpression;
import php.core.expression.PhpStringLiteral;
import php.core.expression.Var;
import php.core.instruction.AssignInstruction;
import php.core.instruction.CaseBlock;
import php.core.instruction.IfBlock;
import php.core.instruction.SwitchBlock;
import php.core.instruction.TryCatchBlock;
import php.core.method.Method;
import php.entity.EntityCls;
import php.entity.method.MethodColumnAttrSetter;
import php.entity.method.MethodCreateNew;
import php.entityrepository.method.MethodEntitySave;
import php.entityrepository.method.MethodGetById;
import php.lib.ClsException;

public class RestMethodSave extends Method {

	Collection<EntityCls> entities;
	
	public RestMethodSave(Collection<EntityCls> entities) {
		super(Public, Types.Void, "save");
		setStatic(true);
		this.entities = entities;
	}

	@Override
	public void addImplementation() {
		Var vResult = _declare(Types.array(Types.Mixed),"result",new ArrayInitExpression());
		 TryCatchBlock tryCatch = _tryCatch();
		SwitchBlock switchEntityType = tryCatch.getTryBlock()._switch(PhpGlobals.$_GET.arrayIndex(new PhpStringLiteral("entityType")));
		for(EntityCls entity : entities) {
			CaseBlock caseBeanType = switchEntityType._case(new PhpStringLiteral(entity.getName()));
			Var postData = caseBeanType._declare(Types.array(Types.Mixed), "_postdata",PhpFunctions.json_decode.call( PhpGlobals.$_POST.arrayIndex("data"),BoolExpression.TRUE));
			Var vIsPkMod = caseBeanType._declare(Types.Bool,"_isPkMod",postData.arrayIndexIsset(new PhpStringLiteral(entity.getTbl().getPrimaryKey().getFirstColumn().getName()+"Previous")));
			
			IfBlock ifInsert= caseBeanType._if(postData.arrayIndex(new PhpStringLiteral("insert")).cast(Types.Bool));
			
			Expression[] getByIdArgs = new Expression[entity.getTbl().getPrimaryKey().getColumnCount()];
			Expression[] getByPrevIdArgs = new Expression[entity.getTbl().getPrimaryKey().getColumnCount()];
			
			int i=0;
			for(Column colPk : entity.getTbl().getPrimaryKey()) {
				getByIdArgs[i] = postData.arrayIndex(new PhpStringLiteral(colPk.getName()));
				getByPrevIdArgs[i] = postData.arrayIndex(new PhpStringLiteral(colPk.getName()+"Previous"));
				++i;
			}
			
			
			Var vEntity=ifInsert.thenBlock()._declare(entity,"_entity",entity.callStaticMethod(MethodCreateNew.getMethodName()));
			Var vEntityUpdate=ifInsert.elseBlock()._declare(entity,"_entity", new InlineIfExpression(vIsPkMod, Types.EntityRepository.callStaticMethod(MethodGetById.getMethodName(entity),getByPrevIdArgs) , Types.EntityRepository.callStaticMethod(MethodGetById.getMethodName(entity),getByIdArgs))	);
			
			for(Column col:entity.getTbl().getColumnsWithoutPrimaryKey()) {
				ifInsert.thenBlock()._callMethodInstr(vEntity, MethodColumnAttrSetter.getMethodName(col), EntityCls.getTypeMapper().getConvertJsonValueToTypedExpression(postData.arrayIndex(new PhpStringLiteral(col.getName())), col));
				ifInsert.elseBlock()._if(postData.arrayIndexIsset(new PhpStringLiteral(col.getName()))).thenBlock()._callMethodInstr(vEntity, MethodColumnAttrSetter.getMethodName(col), EntityCls.getTypeMapper().getConvertJsonValueToTypedExpression(postData.arrayIndex(new PhpStringLiteral(col.getName())), col) );
			}
			
			if(!entity.getTbl().isAutoIncrement()) {
				for(Column colPk : entity.getTbl().getPrimaryKey()) {
					
					
					ifInsert.thenBlock()._callMethodInstr(vEntity, MethodColumnAttrSetter.getMethodName(colPk), postData.arrayIndex(new PhpStringLiteral(colPk.getName())));
					ifInsert.elseBlock()._if(postData.arrayIndexIsset(new PhpStringLiteral(colPk.getName()))).thenBlock()._callMethodInstr(vEntityUpdate, MethodColumnAttrSetter.getMethodName(colPk), postData.arrayIndex(new PhpStringLiteral(colPk.getName())));
				}
			}
			/*Expression e = Types.EntityRepository.callStaticMethod("createQuery"+entity.getName())
					.callMethod(ClsBaseEntityQuery.select);
			Var vQuery = caseBeanType._declare(e.getType(),"query"+entity.getName(),e);
			IfBlock ifIssetCondition= caseBeanType
					._if(PhpFunctions.isset.call(PhpGlobals.$_GET.arrayIndex(new PhpStringLiteral("condition")))) ;
			ifIssetCondition.thenBlock().addInstr(vQuery.callMethodInstruction(ClsBaseEntityQuery.where,PhpGlobals.$_GET.arrayIndex(new PhpStringLiteral("condition"))));
			Var vBeans = caseBeanType._declare(e.getType(),"entities",vQuery.callMethod("fetch") );
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
			
			caseBeanType.addInstr(PhpFunctions.echo(PhpFunctions.json_encode.call(vResult))); */
			
			
			if(entity.getTbl().isAutoIncrement()) {
				Column col=entity.getTbl().getPrimaryKey().getFirstColumn();
				ifInsert.thenBlock().addInstr(Types.EntityRepository.callStaticMethod(MethodEntitySave.getMethodName(entity), vEntity).asInstruction());
				ifInsert.thenBlock()._arraySet(vResult, new PhpStringLiteral(col.getName()), vEntity.callAttrGetter(col.getCamelCaseName()));
				ifInsert.elseBlock().addInstr(Types.EntityRepository.callStaticMethod(MethodEntitySave.getMethodName(entity), vEntity).asInstruction());
			} else {
				caseBeanType.addInstr(Types.EntityRepository.callStaticMethod(MethodEntitySave.getMethodName(entity), vEntity).asInstruction());
			}
			
			caseBeanType._break();
			
		}
		Var vExc = new Var(Types.Exception, "_ex");
		tryCatch.addCatch(vExc, new AssignInstruction(vResult.arrayIndex("error"),BoolExpression.TRUE),new AssignInstruction(vResult.arrayIndex("message"),vExc.callMethod(ClsException.getMessage)));
		tryCatch.getTryBlock()._arraySet(vResult, new PhpStringLiteral("error"), BoolExpression.FALSE);
		addInstr(PhpFunctions.echo(PhpFunctions.json_encode.call(vResult, PhpConstants.JSON_UNESCAPED_UNICODE)));
	}

}
