package php.entityrepository.query.method;

import java.util.ArrayList;
import java.util.List;

import database.column.Column;
import database.relation.AbstractRelation;
import database.relation.OneRelation;
import database.relation.PrimaryKey;
import php.core.PhpFunctions;
import php.core.Type;
import php.core.Types;
import php.core.expression.BoolExpression;
import php.core.expression.Expression;
import php.core.expression.Expressions;
import php.core.expression.NewOperator;
import php.core.expression.PhpStringLiteral;
import php.core.expression.Var;
import php.core.instruction.DoWhile;
import php.core.instruction.IfBlock;
import php.core.instruction.InstructionBlock;
import php.core.method.Method;
import php.entity.Entities;
import php.entity.EntityCls;
import php.entity.method.MethodAttrSetterInternal;
import php.entity.method.MethodOneRelationBeanIsNull;
import php.entityrepository.method.MethodGetFromQueryAssocArray;
import php.lib.ClsBaseEntity;
import php.lib.ClsSqlQuery;
import php.orm.OrmUtil;
import util.StringUtil;

public class MethodEntityQueryFetch extends Method{
	EntityCls bean;
	
	public MethodEntityQueryFetch(EntityCls bean) {
		super(Public, Types.array(bean).toNullable(), "fetch");
		this.bean=bean;
	}
	
	private Expression getFetchExpression(Var res) {
		return EntityCls.getTypeMapper().getDefaultFetchExpression(res);
		
	}

	@Override
	public void addImplementation() {

		List<OneRelation> oneRelations = bean.getOneRelations();
		PrimaryKey pk=bean.getTbl().getPrimaryKey();
		
		Var result = _declareNewArray( "result");
		Var res =_declare(Types.mysqli_result, "res",_this().accessAttr("sqlQuery").callMethod(ClsSqlQuery.query) );
		
		Type e1PkType = pk.isMultiColumn() ? bean.getPkType() : EntityCls.getTypeMapper().columnToType( pk.getFirstColumn());

		
		ArrayList<AbstractRelation> manyRelations = new ArrayList<>();
		
		manyRelations.addAll(bean.getOneToManyRelations());
		manyRelations.addAll(bean.getManyToManyRelations());
		
		Var row = _declare(Types.array(Types.Mixed), "row", getFetchExpression(res) );
		IfBlock ifRowNotNull =	_if(row);
		InstructionBlock ifInstr = ifRowNotNull.thenBlock();
		Var e1Map =  ifInstr._declareNewArray((!manyRelations.isEmpty()) ? Types.array(e1PkType, bean.getFetchListHelperCls()) : Types.array(e1PkType), "e1Map");
		
		DoWhile doWhileQueryNext = ifRowNotNull.thenBlock()._doWhile();
		doWhileQueryNext.setCondition(ifRowNotNull.getCondition());
		
		Var fetchListHelper = null;
		if (!manyRelations.isEmpty()) {
			fetchListHelper = doWhileQueryNext._declare(bean.getFetchListHelperCls(), "fetchListHelper", Expressions.Null);
		}
		Var e1DoWhile = doWhileQueryNext._declare(bean, "e1", Expressions.Null);
		Expression e1ArrayIndexExpression = null;
		if (pk.isMultiColumn()) {
			
			Expression[] e1PkArgs = new Expression[pk.getColumnCount()];
			for(int i = 0; i < pk.getColumnCount(); i++) {
				e1PkArgs[i] = row.arrayIndex(new PhpStringLiteral(EntityCls.getTypeMapper().filterFetchAssocArrayKey("e1__" + pk.getColumn(i).getName())));
			}
			
			Var e1Pk = doWhileQueryNext._declareNew(bean.getPkType(), "e1pk", e1PkArgs);
			e1ArrayIndexExpression = PhpFunctions.md5.call(PhpFunctions.serialize.call(e1Pk));
			//throw new RuntimeException("not implemented");
		} else {
			e1ArrayIndexExpression = row.arrayIndex( new PhpStringLiteral(EntityCls.getTypeMapper().filterFetchAssocArrayKey("e1__"+ pk.getFirstColumn().getName())));
		}
		
		IfBlock ifNotE1SetContains = doWhileQueryNext._if(Expressions.not(PhpFunctions.isset.call(e1Map.arrayIndex(e1ArrayIndexExpression))));
		
		
	
		
		ifNotE1SetContains.thenBlock()._assign(e1DoWhile, Types.BeanRepository.callStaticMethod(MethodGetFromQueryAssocArray.getMethodName(bean), row,  new PhpStringLiteral("e1")));
		
		if (!manyRelations.isEmpty()) {
			
			
			ifNotE1SetContains.thenBlock()._assign(fetchListHelper, new NewOperator(fetchListHelper.getType(), e1DoWhile));
			ifNotE1SetContains.thenBlock()._arraySet(e1Map, e1ArrayIndexExpression, fetchListHelper );
			
			ifNotE1SetContains.elseBlock()._assign(fetchListHelper, e1Map.arrayIndex( e1ArrayIndexExpression));		
			ifNotE1SetContains.elseBlock()._assign(e1DoWhile, fetchListHelper.callMethod("getE1"));		
			
			for(AbstractRelation r:manyRelations) {
				Type beanPk=OrmUtil.getRelationForeignPrimaryKeyType(r);
				Expression foreignBeanExpression = Types.BeanRepository.callStaticMethod(MethodGetFromQueryAssocArray.getMethodName(Entities.get(r.getDestTable())), row,  new PhpStringLiteral(r.getAlias()));
//				IfBlock ifRecValueIsNotNull = null;
				Var foreignBean = null;				
				
				IfBlock ifNotPkForeignIsNull= doWhileQueryNext._if( row.arrayIndex(new PhpStringLiteral(EntityCls.getTypeMapper().filterFetchAssocArrayKey(r.getAlias()+"__"+ r.getDestTable().getPrimaryKey().getFirstColumn().getName()))).isNotNull());
				
				Var pkForeign = null;
				if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
					Expression[] argsRelationPk = new Expression[r.getDestTable().getPrimaryKey().getColumnCount()];
					for(int i = 0; i < r.getDestTable().getPrimaryKey().getColumnCount();i++) {
						argsRelationPk[i] = row.arrayIndex(new PhpStringLiteral(EntityCls.getTypeMapper().filterFetchAssocArrayKey(r.getAlias()+"__"+ r.getDestTable().getPrimaryKey().getColumn(i).getName())));
					}
					pkForeign = ifNotPkForeignIsNull.thenBlock()._declareNew(beanPk, "pkForeignB"+r.getAlias(), argsRelationPk);
					
					
					
				} else {
					Column colPk=r.getDestTable().getPrimaryKey().getFirstColumn();
					pkForeign = ifNotPkForeignIsNull.thenBlock()._declare(beanPk, "pkForeignB"+r.getAlias(), row.arrayIndex(new PhpStringLiteral(EntityCls.getTypeMapper().filterFetchAssocArrayKey(r.getAlias()+"__"+ colPk.getName()))));
					
				}
				
				IfBlock ifNotContainsRelationPk = ifNotPkForeignIsNull.thenBlock()._if(
						Expressions.not(fetchListHelper
										.callMethod("containsPk" + StringUtil.ucfirst(r.getAlias()),
												pkForeign													
												))
								
						
						
					);
				
				foreignBean =ifNotContainsRelationPk.thenBlock()
						
						
						._declare(foreignBeanExpression.getType(), "foreignB"+r.getAlias(),foreignBeanExpression) ;
				
								
				ifNotContainsRelationPk.thenBlock().addInstr(e1DoWhile
						.callMethodInstruction(EntityCls.getAddRelatedBeanMethodName(r), foreignBean));
				ifNotContainsRelationPk.thenBlock().addInstr(
						fetchListHelper.callMethod("addPk" + StringUtil.ucfirst(r.getAlias()), 
								pkForeign
									).asInstruction())
					 ;
				
				
			}
			

			
		} else {
			/* manyRelations.isEmpty() */
			ifNotE1SetContains.thenBlock()._arraySet(e1Map,  e1ArrayIndexExpression, e1DoWhile);
		}
		for(OneRelation r:oneRelations) {
			EntityCls foreignCls = Entities.get(r.getDestTable());
			Expression foreignBeanExpression = Types.BeanRepository.callStaticMethod(MethodGetFromQueryAssocArray.getMethodName(foreignCls), row, new PhpStringLiteral(r.getAlias()));
			
			IfBlock ifRelatedBeanIsNull= ifNotE1SetContains.thenBlock().
					_if(Expressions.and( e1DoWhile.callMethod(new MethodOneRelationBeanIsNull(r)) ,row.arrayIndex(new PhpStringLiteral(EntityCls.getTypeMapper().filterFetchAssocArrayKey(r.getAlias() + "__" + r.getDestTable().getPrimaryKey().getFirstColumn().getName()))).isNotNull()));
			
			Var foreignBean =ifRelatedBeanIsNull.thenBlock()._declare(foreignBeanExpression.getType(), "foreignB"+r.getAlias(),foreignBeanExpression) ;
			ifRelatedBeanIsNull.thenBlock()
				._callMethodInstr(
						e1DoWhile ,
						new MethodAttrSetterInternal(foreignCls,
								bean.getAttrByName(OrmUtil.getOneRelationDestAttrName(r)))
						,  foreignBean);
			
		// FIXME bidirectional relations
//			for (OneRelation foreignOneRelation: foreignCls.getOneRelations()) {
//				if (foreignOneRelation.getDestTable().equals(bean.getTbl())) {
//					ifRelatedBeanIsNull.thenBlock().addInstr(foreignBean.callMethodInstruction("set"+r.getSourceTable().getUc1stCamelCaseName()+"Internal", e1DoWhile));
//				}
//			}
			
		}
		doWhileQueryNext._assign(row, getFetchExpression(res));
		ifNotE1SetContains.thenBlock()._callMethodInstr(e1DoWhile, ClsBaseEntity.setLoaded, BoolExpression.TRUE);
		ifNotE1SetContains.thenBlock()._arrayPush(result, e1DoWhile);
		_return(result);
	}
	@Override
	public boolean includeIfEmpty() {
		return true;
	}
}
