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
import cpp.core.instruction.BreakInstruction;
import cpp.core.instruction.DoWhile;
import cpp.core.instruction.IfBlock;
import cpp.core.instruction.InstructionBlock;
import cpp.entity.Entities;
import cpp.entity.EntityCls;
import cpp.entity.method.MethodAttrSetterInternal;
import cpp.entity.method.MethodOneRelationEntityIsNull;
import cpp.lib.ClsQSqlQuery;
import cpp.lib.ClsQSqlRecord;
import cpp.lib.ClsQVariant;
import cpp.orm.OrmUtil;
import database.column.Column;
import database.relation.AbstractRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import database.relation.PrimaryKey;

public class MethodFetchOne extends Method {

	protected List<OneRelation> oneRelations;
	protected List<OneToManyRelation> manyRelations;
	protected PrimaryKey pk;
	protected EntityCls entity;
	protected Param pQuery;
	protected boolean lazyLoading;
	
	public MethodFetchOne(List<OneRelation> oneRelations,List<OneToManyRelation> manyRelations, EntityCls entity,PrimaryKey pk,boolean lazyLoading) {
		super(Public, entity.toSharedPtr(),  getMethodName(entity,lazyLoading));
		pQuery = addParam(Types.QSqlQuery, "query");	
		this.oneRelations = oneRelations;
		this.manyRelations = manyRelations;
		this.pk = pk;
		this.entity = entity;
		this.lazyLoading = lazyLoading;
		setStatic(true);
	}

protected Expression getExpressionQuery() {
	return pQuery;
}

	@Override
	public void addImplementation() {
		
		if(lazyLoading) {
			
			Expression query = getExpressionQuery();
			
			IfBlock ifQueryNext = _if(query.callMethod("next"));
			InstructionBlock ifInstr = ifQueryNext.thenBlock();
			
			Var e1 = ifInstr
					._declare(entity.toSharedPtr(), "e1", parent.callStaticMethod(MethodGetFromRecord.getMethodName(entity), query.callMethod(ClsQSqlQuery.record) , QString.fromStringConstant("e1")));
			
			
			DoWhile doWhileQueryNext = ifInstr._doWhile();
			Var recDoWhile =doWhileQueryNext._declare(Types.QSqlRecord, "rec" ,query.callMethod(ClsQSqlQuery.record) );
			
			IfBlock ifNotCurrentPrimaryKeyMatches = doWhileQueryNext._if(recDoWhile.callMethod(ClsQSqlRecord.value, QString.fromStringConstant("e1__" + entity.getTbl().getPrimaryKey().getFirstColumn().getName())).callMethod(EntityCls.getDatabaseMapper().getQVariantConvertMethod(entity.getTbl().getPrimaryKey().getFirstColumn()))._notEquals(e1.callAttrGetter(entity.getTbl().getPrimaryKey().getFirstColumn().getCamelCaseName())) );
			ifNotCurrentPrimaryKeyMatches.thenBlock().addInstr(new BreakInstruction());
			doWhileQueryNext.setCondition(ifQueryNext.getCondition());
			

			ifInstr._assignInstruction(recDoWhile, query.callMethod("record"));
				
			
			ifInstr._callMethodInstr(e1, "setLoaded", BoolExpression.FALSE);
			ifInstr._return(e1);
//			_callMethodInstr(query, ClsQSqlQuery.clear); 
			_return(Expressions.Nullptr);
		} else {
			List<OneRelation> oneRelations = entity.getOneRelations();
			
			Expression query = getExpressionQuery();
			
			ArrayList<AbstractRelation> manyRelations = new ArrayList<>();
			
			manyRelations.addAll(entity.getOneToManyRelations());
			manyRelations.addAll(entity.getManyToManyRelations());
			
			IfBlock ifQueryNext = _if(query.callMethod("next"));
			InstructionBlock ifInstr = ifQueryNext.thenBlock();
			
			Var e1 = ifInstr
					._declare(entity.toSharedPtr(), "e1", parent.callStaticMethod(MethodGetFromRecord.getMethodName(entity), query.callMethod(ClsQSqlQuery.record) , QString.fromStringConstant("e1")));
			
			Var fkHelper = null;
			if (!manyRelations.isEmpty()) {
				fkHelper = ifInstr._declare(entity.getFetchListHelperCls(), "fkHelper");		
				ifInstr._assign(fkHelper.accessAttr("e1"), e1);
			}
			
			
			
			
			if (entity.hasRelations()) {
				
			
				DoWhile doWhileQueryNext = ifInstr._doWhile();
				Var recDoWhile =doWhileQueryNext._declare(Types.QSqlRecord, "rec" ,query.callMethod(ClsQSqlQuery.record) );
				
				IfBlock ifNotCurrentPrimaryKeyMatches = doWhileQueryNext._if(recDoWhile.callMethod(ClsQSqlRecord.value, QString.fromStringConstant("e1__" + entity.getTbl().getPrimaryKey().getFirstColumn().getName())).callMethod(EntityCls.getDatabaseMapper().getQVariantConvertMethod(entity.getTbl().getPrimaryKey().getFirstColumn()))._notEquals(e1.callAttrGetter(entity.getTbl().getPrimaryKey().getFirstColumn().getCamelCaseName())) );
				ifNotCurrentPrimaryKeyMatches.thenBlock().addInstr(new BreakInstruction());
				doWhileQueryNext.setCondition(ifQueryNext.getCondition());
				

				ifInstr._assignInstruction(recDoWhile, query.callMethod("record"));
				
			
				
				
				
				
				for(AbstractRelation r:manyRelations) {
					Type beanPk=Types.getRelationForeignPrimaryKeyType(r);
					EntityCls foreignCls = Entities.get(r.getDestTable()); 
					Expression foreignBeanExpression = parent.callStaticMethod(MethodGetFromRecord.getMethodName(foreignCls), recDoWhile, QString.fromStringConstant(r.getAlias()));
					
					IfBlock ifNotPkForeignIsNull= doWhileQueryNext._if(Expressions.not( recDoWhile.callMethod("value", QString.fromStringConstant(r.getAlias()+"__"+ r.getDestTable().getPrimaryKey().getFirstColumn().getName())).callMethod("isNull")));
					
					Var pkForeign = null;
					if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
						pkForeign = ifNotPkForeignIsNull.thenBlock()._declare(beanPk, "pkForeignB"+r.getAlias());
						
						for(Column colPk:r.getDestTable().getPrimaryKey().getColumns()) {
							ifNotPkForeignIsNull.thenBlock()._assign(
									pkForeign.accessAttr(colPk.getCamelCaseName()), 
									recDoWhile.callMethod("value",
											QString.fromStringConstant(r.getAlias()+"__"+ colPk.getName())).callMethod(EntityCls.getDatabaseMapper().getQVariantConvertMethod(colPk))
									);
						}
					} else {
						Column colPk=r.getDestTable().getPrimaryKey().getFirstColumn();
						pkForeign = ifNotPkForeignIsNull.thenBlock()._declare(beanPk, "pkForeignB"+r.getAlias(),recDoWhile.callMethod("value",QString.fromStringConstant(r.getAlias()+"__"+ colPk.getName())).callMethod(EntityCls.getDatabaseMapper().getQVariantConvertMethod(colPk)));
					}
					IfBlock ifRecValueIsNotNull = ifNotPkForeignIsNull.thenBlock()._if(
							Expressions.not(fkHelper
											.accessAttr(r.getAlias()+"Set")
											.callMethod("contains",
													pkForeign
													
													))
									
							
							
						);
					Var foreignBean =ifRecValueIsNotNull.thenBlock()._declare(foreignBeanExpression.getType(), "foreignB"+r.getAlias(),foreignBeanExpression) ;
					
					ifRecValueIsNotNull.thenBlock().addInstr(fkHelper.accessAttr("e1")
							.callMethodInstruction(EntityCls.getRelatedBeanMethodName(r), foreignBean));
					ifRecValueIsNotNull.thenBlock().addInstr(
							fkHelper.accessAttr(r.getAlias()+"Set")
							.callMethod("insert", 
									pkForeign
										).asInstruction())
						 ;
					
//					for (OneRelation foreignOneRelation: foreignCls.getOneRelations()) {
//						if (foreignOneRelation.getDestTable().equals(entity.getTbl())) {
//							ifRecValueIsNotNull.thenBlock().addInstr(foreignBean.callMethodInstruction("set"+r.getSourceTable().getUc1stCamelCaseName()+"Internal", fkHelper.accessAttr("e1")));
//						}
//					}
				}
				

				
			 
		
			for(OneRelation r:oneRelations) {
				EntityCls foreignCls = Entities.get(r.getDestTable());
				Expression foreignBeanExpression = parent.callStaticMethod(MethodGetFromRecord.getMethodName(foreignCls), recDoWhile, QString.fromStringConstant(r.getAlias()));
				
				IfBlock ifRelatedBeanIsNull= doWhileQueryNext.
						_if(Expressions.and( e1.callMethod(new MethodOneRelationEntityIsNull(r))
								,
								Expressions.not( recDoWhile.callMethod("value", QString.fromStringConstant(r.getAlias()+"__"+ r.getDestTable().getPrimaryKey().getFirstColumn().getName())).callMethod(ClsQVariant.isNull))
								));
				
				Var foreignBean =ifRelatedBeanIsNull.thenBlock()._declare(foreignBeanExpression.getType(), "foreignB"+r.getAlias(),foreignBeanExpression) ;
				ifRelatedBeanIsNull.thenBlock()
					._callMethodInstr(
							e1 ,
							new MethodAttrSetterInternal(foreignCls,
									entity.getAttrByName(OrmUtil.getOneRelationDestAttrName(r)))
							,  foreignBean);
				
			
//				for (OneRelation foreignOneRelation: foreignCls.getOneRelations()) {
//					if (foreignOneRelation.getDestTable().equals(entity.getTbl())) {
//						ifRelatedBeanIsNull.thenBlock().addInstr(foreignBean.callMethodInstruction("set"+r.getSourceTable().getUc1stCamelCaseName()+"Internal", e1));
//					}
//				}
			}
			}
			
			ifInstr._callMethodInstr(e1, "setLoaded", BoolExpression.TRUE);
			ifInstr._return(e1);
//			_callMethodInstr(query, ClsQSqlQuery.clear); 
			_return(Expressions.Nullptr);
		}
		
		
	}
	

	public static String getMethodName(EntityCls entity,boolean lazyLoading) {
		return "fetchOne"+entity.getName()+(lazyLoading?"LazyLoading":"");
	}

}
