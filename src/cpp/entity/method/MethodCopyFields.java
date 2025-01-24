package cpp.entity.method;

import java.util.ArrayList;

import cpp.CoreTypes;
import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.CreateObjectExpression;
import cpp.core.expression.Expression;
import cpp.core.expression.Expressions;
import cpp.core.expression.ParenthesesExpression;
import cpp.core.expression.Var;
import cpp.core.instruction.ForeachLoop;
import cpp.core.instruction.IfBlock;
import cpp.core.method.MethodAttributeSetter;
import cpp.entity.Entities;
import cpp.entity.EntityCls;
import cpp.entity.Nullable;
import cpp.entityrepository.method.MethodRepoCreateNew;
import cpp.lib.ClsBaseRepository;
import cpp.lib.ClsQSet;
import cpp.orm.OrmUtil;
import database.column.Column;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import php.lib.ClsBaseEntity;
import util.StringUtil;

public class MethodCopyFields extends Method{

	private static final String METHOD_NAME = "copyFieldsFrom";
	Param pSrc,pExclude;
	
	public MethodCopyFields(EntityCls entity) {
		super(Public, CoreTypes.Void, METHOD_NAME);
		pSrc = addParam(new Param(entity.toSharedPtr().toConstRef(), "src"));
		pExclude = addParam(new Param(Types.qset(Types.QString).toConstRef(), "exclude",new CreateObjectExpression(Types.qset(Types.QString))).setMaybeUnused());
		for(OneRelation r : entity.getOneRelations()) {
			addParam(new Param(CoreTypes.Bool, "copyRelation"+r.getDestTable().getUc1stCamelCaseName()+StringUtil.ucfirst(r.getAlias()),BoolExpression.FALSE));
		}
		for(OneToManyRelation r : entity.getOneToManyRelations()) {
			addParam(new Param(CoreTypes.Bool, "copyRelation"+r.getDestTable().getUc1stCamelCaseName()+StringUtil.ucfirst(r.getAlias()),BoolExpression.FALSE));
		}
		for(ManyRelation r : entity.getManyRelations()) {
			addParam(new Param(CoreTypes.Bool, "copyRelation"+r.getDestTable().getUc1stCamelCaseName()+StringUtil.ucfirst(r.getAlias()),BoolExpression.FALSE));
		}
	}
	
	@Override
	public boolean includeIfEmpty() {
		return true;
	}

	@Override
	public void addImplementation() {
		EntityCls entity = (EntityCls) parent;
		
		for(Column col : entity.getTbl().getColumnsWithoutPrimaryKey()) {
			
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
				
//				if(withExclude) {
					IfBlock ifNotExclude = _if(Expressions.not(pExclude.callMethod(ClsQSet.contains, QString.fromStringConstant(col.getName()))));
					if(col.isNullable()) {
						IfBlock ifValIsNull = ifNotExclude.thenBlock()._if( pSrc.callAttrGetter(col.getCamelCaseName()).callMethod(Nullable.isNull));
						ifValIsNull.thenBlock()._callMethodInstr(_this(),  MethodColumnAttrSetNull.getMethodName(col));
						ifValIsNull.elseBlock()._callMethodInstr(_this(), MethodColumnAttrSetter.getMethodName(col), pSrc.callAttrGetter(col.getCamelCaseName()).callMethod(Nullable.val));
					} else {
						ifNotExclude.thenBlock()._callMethodInstr(_this(), MethodColumnAttrSetter.getMethodName(col), pSrc.callAttrGetter(col.getCamelCaseName()));
					}
//				} else {
//					if(col.isNullable()) {
//						IfBlock ifValIsNull = _if( pSrc.callAttrGetter(col.getCamelCaseName()).callMethod(Nullable.isNull));
//						ifValIsNull.thenBlock()._callMethodInstr(_this(),  MethodColumnAttrSetNull.getMethodName(col));
//						ifValIsNull.elseBlock()._callMethodInstr(_this(), MethodColumnAttrSetter.getMethodName(col), pSrc.callAttrGetter(col.getCamelCaseName()).callMethod(Nullable.val));
//					} else {
//						_callMethodInstr(_this(), MethodColumnAttrSetter.getMethodName(col), pSrc.callAttrGetter(col.getCamelCaseName()));
//					}
//				}
				
			}
		}
		if(entity.hasRelations()) {
			ArrayList<Expression> exprHasRelation = new ArrayList<>();
			Expression thisRepository = _accessThis(parent.getAttrByName("repository"));
			IfBlock ifHasRelation = _if(null);
			
			for(OneRelation r : entity.getOneRelations()) {
				IfBlock ifCopyRelations = _if(getParam("copyRelation"+r.getDestTable().getUc1stCamelCaseName()+StringUtil.ucfirst(r.getAlias())));
				exprHasRelation.add(ifCopyRelations.getCondition());
				ifCopyRelations.thenBlock()._callMethodInstr(_this(), MethodAttributeSetter.getMethodName(entity.getAttrByName(OrmUtil.getOneRelationDestAttrName(r))), pSrc.callAttrGetter(entity.getAttrByName(OrmUtil.getOneRelationDestAttrName(r))));
			}
			for(OneToManyRelation r : entity.getOneToManyRelations()) {
				IfBlock ifCopyRelations = _if(getParam("copyRelation"+r.getDestTable().getUc1stCamelCaseName()+StringUtil.ucfirst(r.getAlias())));
				exprHasRelation.add(ifCopyRelations.getCondition());
				ForeachLoop foreachRelationEntity = ifCopyRelations.thenBlock()._foreach(new Var(Entities.get(r.getDestTable()).toSharedPtr().toConstRef(), OrmUtil.getOneToManyRelationDestAttrNameSingular(r)), pSrc.callAttrGetter(OrmUtil.getOneToManyRelationDestAttrName(r)));
				Var vNewRelationEntity = foreachRelationEntity._declare(foreachRelationEntity.getVar().getType(), "_newRelationEntity", thisRepository.callMethod(MethodRepoCreateNew.getMethodName(Entities.get(r.getDestTable()))));
				foreachRelationEntity._callMethodInstr(vNewRelationEntity, METHOD_NAME, foreachRelationEntity.getVar());
				foreachRelationEntity._callMethodInstr(_this(), MethodAddRelatedEntity.getMethodName(r), vNewRelationEntity);
				foreachRelationEntity.addInstr(thisRepository.callMethodInstruction(ClsBaseRepository.save, vNewRelationEntity.dereference()));
			}
			for(ManyRelation r : entity.getManyRelations()) {
				IfBlock ifCopyRelations = _if(getParam("copyRelation"+r.getDestTable().getUc1stCamelCaseName()+StringUtil.ucfirst(r.getAlias())));
				exprHasRelation.add(ifCopyRelations.getCondition());
				ForeachLoop foreachRelationEntity = ifCopyRelations.thenBlock()._foreach(new Var(Entities.get(r.getDestTable()).toSharedPtr().toConstRef(), OrmUtil.getManyRelationDestAttrNameSingular(r)), pSrc.callAttrGetter(OrmUtil.getManyRelationDestAttrName(r)));
				foreachRelationEntity._callMethodInstr(_this(), MethodAddManyToManyRelatedEntity.getMethodName(r), foreachRelationEntity.getVar());
			}
			
			ifHasRelation.setCondition(Expressions.and(_this().accessAttr(ClsBaseEntity.insert),new ParenthesesExpression( Expressions.or(exprHasRelation))));
			ifHasRelation.thenBlock().addInstr(thisRepository.callMethodInstruction(ClsBaseRepository.save, _this().dereference()));
		}
	}

}
