package cpp.entityrepository.method;

import java.util.ArrayList;
import java.util.List;

import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.Type;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.Expression;
import cpp.core.expression.Expressions;
import cpp.core.expression.Var;
import cpp.core.instruction.DoWhile;
import cpp.core.instruction.IfBlock;
import cpp.core.instruction.InstructionBlock;
import cpp.entity.Entities;
import cpp.entity.EntityCls;
import cpp.entity.method.MethodAttrSetterInternal;
import cpp.entity.method.MethodOneRelationEntityIsNull;
import cpp.lib.ClsQHash;
import cpp.lib.ClsQSet;
import cpp.lib.ClsQVariant;
import cpp.orm.OrmUtil;
import database.column.Column;
import database.relation.AbstractRelation;
import database.relation.OneRelation;
import database.relation.PrimaryKey;

public class MethodFetchList extends Method {

	//protected List<OneRelation> oneRelations;
	//protected List<OneToManyRelation> manyRelations;
	protected PrimaryKey pk;
	protected EntityCls entity;
	protected Param pQuery;
	protected boolean lazyLoading;
	
	public MethodFetchList(EntityCls entity,PrimaryKey pk,boolean lazyLoading) {
		super(Public, Types.qlist(entity.toSharedPtr()),getMethodName(entity,lazyLoading) );
		pQuery = addParam(Types.QSqlQuery, "query");	
		//this.oneRelations = oneRelations;
		//this.manyRelations = manyRelations;
		this.pk = pk;
		this.entity = entity;
		this.lazyLoading = lazyLoading;
		setStatic(true);
	}
	
	protected Expression getExpressionQuery() {
		return  pQuery;
	}
		
	@Override
	public void addImplementation() {
		List<OneRelation> oneRelations = entity.getOneRelations();
		PrimaryKey pk=entity.getTbl().getPrimaryKey();
		
		Var result = _declare(returnType, "result");
		Expression query = getExpressionQuery();
		//int //bCount = 2;
		Type e1PkType = pk.isMultiColumn() ? entity.getStructPk() : EntityCls.getDatabaseMapper().columnToType( pk.getColumns().get(0));
		
		ArrayList<AbstractRelation> manyRelations = new ArrayList<>();
		
		manyRelations.addAll(entity.getOneToManyRelations());
		manyRelations.addAll(entity.getManyToManyRelations());
		
		IfBlock ifQueryNext = _if(query.callMethod("next"));
		InstructionBlock ifInstr = ifQueryNext.thenBlock();
		Var e1Map =  ifInstr._declare((!manyRelations.isEmpty()) ? new ClsQHash(e1PkType, entity.getFetchListHelperCls()) : new ClsQSet(e1PkType), "e1Map");
		
		DoWhile doWhileQueryNext = ifQueryNext.thenBlock()._doWhile();
		doWhileQueryNext.setCondition(query.callMethod("next"));
		Var recDoWhile =doWhileQueryNext._declare(Types.QSqlRecord, "rec",query.callMethod("record") );
		
		Var e1pk = null;
		
		if (pk.isMultiColumn()) {
			e1pk =doWhileQueryNext._declare( entity.getStructPk(), "e1pk" );
			for(Column colPk:pk.getColumns()) {
				doWhileQueryNext._assign(e1pk.accessAttr(colPk.getCamelCaseName()), recDoWhile.callMethod("value", QString.fromStringConstant("e1__"+ colPk.getName())).callMethod(EntityCls.getDatabaseMapper().getQVariantConvertMethod(colPk.getDbType())));
			}
			
		} else {
			e1pk =doWhileQueryNext._declare( EntityCls.getDatabaseMapper().columnToType(pk.getFirstColumn()), "e1pk", recDoWhile.callMethod("value", QString.fromStringConstant("e1__"+ pk.getFirstColumn().getName())).callMethod(EntityCls.getDatabaseMapper().getQVariantConvertMethod(pk.getFirstColumn().getDbType())));
		}
		
		IfBlock ifNotE1SetContains = doWhileQueryNext._if(Expressions.not(e1Map.callMethod("contains", e1pk)));
		
		
		Var e1DoWhile = ifNotE1SetContains.thenBlock()
				._declare(entity.toSharedPtr(), "e1", parent.callStaticMethod(MethodGetFromRecord.getMethodName(entity), recDoWhile, QString.fromStringConstant("e1")));
		
		if(!this.lazyLoading) {
		
			if (!manyRelations.isEmpty()) {
				
				doWhileQueryNext._assignInstruction(recDoWhile, query.callMethod("record"));
				
				Var fkHelper = doWhileQueryNext._declare(entity.getFetchListHelperCls().toRef(), "fkHelper",e1Map.arrayIndex(e1pk));
				
				Var structHelperIfNotE1SetContains = ifNotE1SetContains.thenBlock()._declare(entity.getFetchListHelperCls(), "structHelper");
				ifNotE1SetContains.thenBlock()._assign(structHelperIfNotE1SetContains.accessAttr("e1"), e1DoWhile);
				
				ifNotE1SetContains.thenBlock()._callMethodInstr(e1Map, "insert", e1pk, structHelperIfNotE1SetContains );
						
				
				for(AbstractRelation r:manyRelations) {
					Type entityPk=Types.getRelationForeignPrimaryKeyType(r);
					EntityCls foreignCls = Entities.get(r.getDestTable()); 
					Expression foreignEntityExpression = parent.callStaticMethod(MethodGetFromRecord.getMethodName(foreignCls), recDoWhile, QString.fromStringConstant(r.getAlias()));
	//				IfBlock ifRecValueIsNotNull = null;
					Var foreignEntity = null;				
					
					IfBlock ifNotPkForeignIsNull= doWhileQueryNext._if(Expressions.not( recDoWhile.callMethod("value", QString.fromStringConstant(r.getAlias()+"__"+ r.getDestTable().getPrimaryKey().getFirstColumn().getName())).callMethod("isNull")));
					
					Var pkForeign = null;
					if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
						pkForeign = ifNotPkForeignIsNull.thenBlock()._declare(entityPk, "pkForeignB"+r.getAlias());
						
						for(Column colPk:r.getDestTable().getPrimaryKey().getColumns()) {
							ifNotPkForeignIsNull.thenBlock()._assign(
									pkForeign.accessAttr(colPk.getCamelCaseName()), 
									recDoWhile.callMethod("value",
											QString.fromStringConstant(r.getAlias()+"__"+ colPk.getName())).callMethod(EntityCls.getDatabaseMapper().getQVariantConvertMethod(colPk.getDbType()))
									);
						}
					} else {
						Column colPk=r.getDestTable().getPrimaryKey().getFirstColumn();
						pkForeign = ifNotPkForeignIsNull.thenBlock()._declare(entityPk, "pkForeignB"+r.getAlias(),recDoWhile.callMethod("value",QString.fromStringConstant(r.getAlias()+"__"+ colPk.getName())).callMethod(EntityCls.getDatabaseMapper().getQVariantConvertMethod(colPk.getDbType())));
					}
					IfBlock ifRecValueIsNotNull = ifNotPkForeignIsNull.thenBlock()._if(
							Expressions.not(fkHelper
											.accessAttr(r.getAlias()+"Set")
											.callMethod("contains",
													pkForeign
													
													))
									
							
							
						);
					foreignEntity =ifRecValueIsNotNull.thenBlock()._declare(foreignEntityExpression.getType(), "foreignB"+r.getAlias(),foreignEntityExpression) ;
					
									
					ifRecValueIsNotNull.thenBlock().addInstr(fkHelper.accessAttr("e1")
							.callMethodInstruction(EntityCls.getRelatedEntityMethodName(r), foreignEntity));
					ifRecValueIsNotNull.thenBlock().addInstr(
							fkHelper.accessAttr(r.getAlias()+"Set")
							.callMethod("insert", 
									pkForeign
										).asInstruction())
						 ;
					
//					for (OneRelation foreignOneRelation: foreignCls.getOneRelations()) {
//						if (foreignOneRelation.getDestTable().equals(entity.getTbl())) {
//							ifRecValueIsNotNull.thenBlock().addInstr(foreignEntity.callMethodInstruction("set"+r.getSourceTable().getUc1stCamelCaseName()+"Internal", fkHelper.accessAttr("e1")));
//						}
//					}
					//ifRecValueIsNotNull.getIfInstr()._callMethodInstr(foreignEntity, "setLoaded", BoolExpression.TRUE);
					
					//bCount++;
					ifNotE1SetContains.thenBlock().addInstr(e1DoWhile.callSetterMethodInstruction("loaded"+  r.getIdentifier(), BoolExpression.TRUE));
				}
				
				
				
			} else {
				/* manyRelations.isEmpty() */
				ifNotE1SetContains.thenBlock()._callMethodInstr(e1Map, "insert", e1pk);
			}
			for(OneRelation r:oneRelations) {
				EntityCls foreignCls = Entities.get(r.getDestTable());
				Expression foreignEntityExpression = parent.callStaticMethod(MethodGetFromRecord.getMethodName(foreignCls), recDoWhile, QString.fromStringConstant(r.getAlias()));
				
				IfBlock ifRelatedEntityIsNull= ifNotE1SetContains.thenBlock().
						_if(Expressions.and( e1DoWhile.callMethod(new MethodOneRelationEntityIsNull(r))
								,
								Expressions.not( recDoWhile.callMethod("value", QString.fromStringConstant(r.getAlias()+"__"+ r.getDestTable().getPrimaryKey().getFirstColumn().getName())).callMethod(ClsQVariant.isNull))
								));
				
				Var foreignEntity =ifRelatedEntityIsNull.thenBlock()._declare(foreignEntityExpression.getType(), "foreignB"+r.getAlias(),foreignEntityExpression) ;
				ifRelatedEntityIsNull.thenBlock()
					._callMethodInstr(
							e1DoWhile ,
							new MethodAttrSetterInternal(foreignCls,
									entity.getAttrByName(OrmUtil.getOneRelationDestAttrName(r)))
							,  foreignEntity);
				
			
				ifNotE1SetContains.thenBlock().addInstr(e1DoWhile.callSetterMethodInstruction("loaded"+  r.getIdentifier(), BoolExpression.TRUE));
			}
			

		}
		ifNotE1SetContains.thenBlock()._callMethodInstr(result, "append", e1DoWhile);
//		_callMethodInstr(query, ClsQSqlQuery.clear); 
		_return(result);
		
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	public static String getMethodName(EntityCls entity,boolean lazyLoading) {
		return  "fetchList"+entity.getName()+(lazyLoading?"LazyLoading":"");
	}

}
