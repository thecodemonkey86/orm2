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
			CaseBlock caseEntityType = switchEntityType._case(new PhpStringLiteral(entity.getName()));
			
			PrimaryKey pk= entity.getTbl().getPrimaryKey();
			
			
		Expression[] args=new Expression[pk.getColumnCount()];
		int i=0;
			for(Column pkCol : pk) {
				args[i++] = PhpGlobals.$_GET.arrayIndex(new PhpStringLiteral(pkCol.getName() ));
			}
			Expression e = Types.EntityRepository.callStaticMethod(MethodGetById.getMethodName(entity),args);
			
			Var vEntity = caseEntityType._declare(e.getType(),"entity",e );
			
			Var entityData =  caseEntityType._declare(Types.array(Types.String), "entityData",vEntity.callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME));
			
			for(OneRelation r : entity.getOneRelations() ) {
				IfBlock ifRelatedEntityIsNotNull = caseEntityType._if(vEntity.callMethod( OrmUtil.getOneRelationDestAttrGetter(r)).isNotNull());
				
				Var relationEntityData =ifRelatedEntityIsNotNull.thenBlock()._declare(Types.array(Types.String), "relationEntityData_"+r.getAlias(),vEntity.callMethod( OrmUtil.getOneRelationDestAttrGetter(r)).callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME));
				ifRelatedEntityIsNotNull.thenBlock().addInstr( entityData.arrayIndexSet(new PhpStringLiteral(OrmUtil.getOneRelationDestAttrName(r)), relationEntityData));
			}
			for(OneToManyRelation r : entity.getOneToManyRelations() ) {
				caseEntityType.addInstr( entityData.arrayIndexSet(new PhpStringLiteral(OrmUtil.getOneToManyRelationDestAttrNameSingular(r)), new ArrayInitExpression()));
				Var arrRelationEntities =caseEntityType._declare(Types.array(Types.Mixed),"relationEntities", vEntity.callAttrGetter( OrmUtil.getOneToManyRelationDestAttrName(r)));
				ForeachLoop foreachRelationEntity = caseEntityType._foreach(new Var(Entities.get(r.getDestTable()), "relationEntity"+r.getAlias() ), arrRelationEntities);
				
				foreachRelationEntity._arrayPush( entityData.arrayIndex(new PhpStringLiteral(OrmUtil.getOneToManyRelationDestAttrNameSingular(r))), foreachRelationEntity.getVar().callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME));
			}
			for(ManyRelation r : entity.getManyRelations() ) {
				caseEntityType.addInstr( entityData.arrayIndexSet(new PhpStringLiteral(OrmUtil.getManyRelationDestAttrNameSingular(r)), new ArrayInitExpression()));
				Var arrRelationEntities =caseEntityType._declare(Types.array(Types.Mixed),"relationEntities", vEntity.callAttrGetter( OrmUtil.getManyRelationDestAttrName(r)));
				ForeachLoop foreachRelationEntity = caseEntityType._foreach(new Var(Entities.get(r.getDestTable()), "relationEntity"+r.getAlias() ), arrRelationEntities);
				
				foreachRelationEntity._arrayPush( entityData.arrayIndex(new PhpStringLiteral(OrmUtil.getManyRelationDestAttrNameSingular(r))), foreachRelationEntity.getVar().callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME));
			}
			
			caseEntityType.addInstr(PhpFunctions.echo(PhpFunctions.json_encode.call(entityData, PhpConstants.JSON_UNESCAPED_UNICODE)));
			caseEntityType._break();
		}


	}

}
