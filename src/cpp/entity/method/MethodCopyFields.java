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
import cpp.util.ClsDbPool;
import database.column.Column;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;

public class MethodCopyFields extends Method{

	protected Param pSrc,pExclude,pRelations;
	protected Param pSqlCon;
	
	public MethodCopyFields(EntityCls entity) {
		super(Public, CoreTypes.Void, "copyFieldsFrom");
		pSrc = addParam(new Param(entity.toSharedPtr().toConstRef(), "src"));
		if(entity.hasRelations() ) {
			pRelations = addParam(new Param(CoreTypes.Bool, "copyRelations"));
			pSqlCon = addParam(Types.QSqlDatabase.toConstRef(),"sqlCon",ClsDbPool.instance.callStaticMethod(ClsDbPool.getDatabase));
		}
		
		
	}

	@Override
	public void addImplementation() {
		EntityCls entity = (EntityCls) parent;
		
		for(Column col : entity.getTbl().getColumnsWithoutPrimaryKey()) {
			
			// FIXME column.hasRelation not working
			boolean hasRelation = false;
			for(OneRelation r : entity.getOneRelations()) {
				for(int i=0;i<r.getColumnCount();i++) {
					if(r.getColumns(i).getValue1().equals(col)) {
						hasRelation =true;
						break;
					}
				}
			}
			for(OneToManyRelation r : entity.getOneToManyRelations()) {
				for(int i=0;i<r.getColumnCount();i++) {
					if(r.getColumns(i).getValue1().equals(col)) {
						hasRelation =true;
						break;
					}
				}
			}
			for(ManyRelation r : entity.getManyRelations()) {
				for(int i=0;i<r.getSourceColumnCount();i++) {
					if(r.getSourceEntityColumn(i).equals(col)) {
						hasRelation =true;
						break;
					}
				}
			}
		
		
			if(!hasRelation && !col.isFileImportEnabled()) {
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
		if(entity.hasRelations() ) {
		IfBlock ifCopyRelations = _if(pRelations);
		for(OneRelation r : entity.getOneRelations()) {
			ifCopyRelations.thenBlock()._callMethodInstr(_this(), MethodAttributeSetter.getMethodName(entity.getAttrByName(OrmUtil.getOneRelationDestAttrName(r))), pSrc.callAttrGetter(entity.getAttrByName(OrmUtil.getOneRelationDestAttrName(r)),pSqlCon ));
		}
		for(OneToManyRelation r : entity.getOneToManyRelations()) {
			ForeachLoop foreachRelationEntity = ifCopyRelations.thenBlock()._foreach(new Var(Entities.get(r.getDestTable()).toSharedPtr().toConstRef(), OrmUtil.getOneToManyRelationDestAttrNameSingular(r)), pSrc.callAttrGetter(OrmUtil.getOneToManyRelationDestAttrName(r),pSqlCon));
			foreachRelationEntity._callMethodInstr(_this(), MethodAddRelatedEntity.getMethodName(r), foreachRelationEntity.getVar());
		}
		for(ManyRelation r : entity.getManyRelations()) {
			ForeachLoop foreachRelationEntity = ifCopyRelations.thenBlock()._foreach(new Var(Entities.get(r.getDestTable()).toSharedPtr().toConstRef(), OrmUtil.getManyRelationDestAttrNameSingular(r)), pSrc.callAttrGetter(OrmUtil.getManyRelationDestAttrName(r),pSqlCon));
			foreachRelationEntity._callMethodInstr(_this(), MethodAddManyToManyRelatedEntity.getMethodName(r), foreachRelationEntity.getVar(),pSqlCon);
		}
		}
	}

}
