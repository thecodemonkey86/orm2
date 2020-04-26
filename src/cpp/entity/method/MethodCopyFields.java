package cpp.entity.method;

import cpp.CoreTypes;
import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.expression.CreateObjectExpression;
import cpp.core.expression.Expressions;
import cpp.core.expression.Var;
import cpp.core.instruction.ForeachLoop;
import cpp.core.instruction.IfBlock;
import cpp.core.method.MethodAttributeSetter;
import cpp.entity.Entities;
import cpp.entity.EntityCls;
import cpp.entity.Nullable;
import cpp.lib.ClsQSet;
import cpp.orm.OrmUtil;
import database.column.Column;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;

public class MethodCopyFields extends Method{

	Param pSrc,pExclude,pRelations;
	
	public MethodCopyFields(EntityCls bean) {
		super(Public, CoreTypes.Void, "copyFieldsFrom");
		pSrc = addParam(new Param(bean.toSharedPtr().toConstRef(), "src"));
		if(bean.hasRelations() ) {
			pRelations = addParam(new Param(CoreTypes.Bool, "copyRelations"));
		}
		
	}

	@Override
	public void addImplementation() {
		EntityCls bean = (EntityCls) parent;
		
		for(Column col : bean.getTbl().getColumnsWithoutPrimaryKey()) {
			
			// FIXME column.hasRelation not working
			boolean hasRelation = false;
			for(OneRelation r : bean.getOneRelations()) {
				for(int i=0;i<r.getColumnCount();i++) {
					if(r.getColumns(i).getValue1().equals(col)) {
						hasRelation =true;
						break;
					}
				}
			}
			for(OneToManyRelation r : bean.getOneToManyRelations()) {
				for(int i=0;i<r.getColumnCount();i++) {
					if(r.getColumns(i).getValue1().equals(col)) {
						hasRelation =true;
						break;
					}
				}
			}
			for(ManyRelation r : bean.getManyRelations()) {
				for(int i=0;i<r.getSourceColumnCount();i++) {
					if(r.getSourceEntityColumn(i).equals(col)) {
						hasRelation =true;
						break;
					}
				}
			}
		
		
			if(!hasRelation) {
				if(pExclude == null) {
					pExclude = addParam(new Param(Types.qset(Types.QString).toConstRef(), "exclude",new CreateObjectExpression(Types.qset(Types.QString))));
				}
				
				IfBlock ifNotExclude = _if(Expressions.not(pExclude.callMethod(ClsQSet.contains, QString.fromStringConstant(col.getName()))));
				if(col.isNullable()) {
					IfBlock ifValIsNull = ifNotExclude.thenBlock()._if( pSrc.callAttrGetter(col.getCamelCaseName()).callMethod(Nullable.isNull));
					ifValIsNull.thenBlock()._callMethodInstr(_this(),  MethodColumnAttrSetNull.getMethodName(col));
					ifValIsNull.elseBlock()._callMethodInstr(_this(), MethodColumnAttrSetter.getMethodName(col), pSrc.callAttrGetter(col.getCamelCaseName()).callMethod(Nullable.val));
				} else {
					ifNotExclude.thenBlock()._callMethodInstr(_this(), MethodColumnAttrSetter.getMethodName(col), pSrc.callAttrGetter(col.getCamelCaseName()));
				}
				
				
			}
		}
		if(bean.hasRelations() ) {
		IfBlock ifCopyRelations = _if(pRelations);
		for(OneRelation r : bean.getOneRelations()) {
			ifCopyRelations.thenBlock()._callMethodInstr(_this(), MethodAttributeSetter.getMethodName(bean.getAttrByName(OrmUtil.getOneRelationDestAttrName(r))), pSrc.callAttrGetter(bean.getAttrByName(OrmUtil.getOneRelationDestAttrName(r))));
		}
		for(OneToManyRelation r : bean.getOneToManyRelations()) {
			ForeachLoop foreachRelationEntity = ifCopyRelations.thenBlock()._foreach(new Var(Entities.get(r.getDestTable()).toSharedPtr().toConstRef(), OrmUtil.getOneToManyRelationDestAttrNameSingular(r)), pSrc.callAttrGetter(OrmUtil.getOneToManyRelationDestAttrName(r)));
			foreachRelationEntity._callMethodInstr(_this(), MethodAddRelatedEntity.getMethodName(r), foreachRelationEntity.getVar());
		}
		for(ManyRelation r : bean.getManyRelations()) {
			ForeachLoop foreachRelationEntity = ifCopyRelations.thenBlock()._foreach(new Var(Entities.get(r.getDestTable()).toSharedPtr().toConstRef(), OrmUtil.getManyRelationDestAttrNameSingular(r)), pSrc.callAttrGetter(OrmUtil.getManyRelationDestAttrName(r)));
			foreachRelationEntity._callMethodInstr(_this(), MethodAddManyToManyRelatedEntity.getMethodName(r), foreachRelationEntity.getVar());
		}
		}
	}

}
