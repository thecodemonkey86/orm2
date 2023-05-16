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
import cpp.entityrepository.ClsEntityRepository;
import cpp.entityrepository.expression.ThisEntityRepositoryExpression;
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
	protected EntityCls bean;
	protected Param pQuery;
	protected boolean lazyLoading;
	
	public MethodFetchOne(List<OneRelation> oneRelations,List<OneToManyRelation> manyRelations, EntityCls bean,PrimaryKey pk,boolean lazyLoading) {
		super(Public, bean.toSharedPtr(),  getMethodName(bean,lazyLoading));
		pQuery = addParam(Types.QSqlQuery.toRValueRef(), "query");	
		this.oneRelations = oneRelations;
		this.manyRelations = manyRelations;
		this.pk = pk;
		this.bean = bean;
		this.lazyLoading = lazyLoading;
	}
	
	@Override
	public ThisEntityRepositoryExpression _this() {
		return new ThisEntityRepositoryExpression((ClsEntityRepository) parent);
	}
	
	protected Expression getByRecordExpression(EntityCls bean, Expression record, QString alias) {
	//return new ThisBeanRepositoryExpression((BeanRepository) parent);
		return _this().callMethod(MethodGetFromRecord.getMethodName(bean),  record, alias);
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
					._declare(bean.toSharedPtr(), "e1", getByRecordExpression(bean, query.callMethod(ClsQSqlQuery.record) , QString.fromStringConstant("e1")));
			
			
			DoWhile doWhileQueryNext = ifInstr._doWhile();
			Var recDoWhile =doWhileQueryNext._declare(Types.QSqlRecord, "rec" ,query.callMethod(ClsQSqlQuery.record) );
			
			IfBlock ifNotCurrentPrimaryKeyMatches = doWhileQueryNext._if(recDoWhile.callMethod(ClsQSqlRecord.value, QString.fromStringConstant("e1__" + bean.getTbl().getPrimaryKey().getFirstColumn().getName())).callMethod(EntityCls.getDatabaseMapper().getQVariantConvertMethod(bean.getTbl().getPrimaryKey().getFirstColumn()))._notEquals(e1.callAttrGetter(bean.getTbl().getPrimaryKey().getFirstColumn().getCamelCaseName())) );
			ifNotCurrentPrimaryKeyMatches.thenBlock().addInstr(new BreakInstruction());
			doWhileQueryNext.setCondition(ifQueryNext.getCondition());
			

			ifInstr._assignInstruction(recDoWhile, query.callMethod("record"));
				
			
			ifInstr._callMethodInstr(e1, "setLoaded", BoolExpression.FALSE);
			ifInstr._return(e1);
			_return(Expressions.Nullptr);
		} else {
			List<OneRelation> oneRelations = bean.getOneRelations();
			
			Expression query = getExpressionQuery();
			
			ArrayList<AbstractRelation> manyRelations = new ArrayList<>();
			
			manyRelations.addAll(bean.getOneToManyRelations());
			manyRelations.addAll(bean.getManyToManyRelations());
			
			IfBlock ifQueryNext = _if(query.callMethod("next"));
			InstructionBlock ifInstr = ifQueryNext.thenBlock();
			
			Var e1 = ifInstr
					._declare(bean.toSharedPtr(), "e1", getByRecordExpression(bean, query.callMethod(ClsQSqlQuery.record) , QString.fromStringConstant("e1")));
			
			Var fkHelper = null;
			if (!manyRelations.isEmpty()) {
				fkHelper = ifInstr._declare(bean.getFetchListHelperCls(), "fkHelper");		
				ifInstr._assign(fkHelper.accessAttr("e1"), e1);
			}
			
			
			
			
			if (bean.hasRelations()) {
				
			
				DoWhile doWhileQueryNext = ifInstr._doWhile();
				Var recDoWhile =doWhileQueryNext._declare(Types.QSqlRecord, "rec" ,query.callMethod(ClsQSqlQuery.record) );
				
				IfBlock ifNotCurrentPrimaryKeyMatches = doWhileQueryNext._if(recDoWhile.callMethod(ClsQSqlRecord.value, QString.fromStringConstant("e1__" + bean.getTbl().getPrimaryKey().getFirstColumn().getName())).callMethod(EntityCls.getDatabaseMapper().getQVariantConvertMethod(bean.getTbl().getPrimaryKey().getFirstColumn()))._notEquals(e1.callAttrGetter(bean.getTbl().getPrimaryKey().getFirstColumn().getCamelCaseName())) );
				ifNotCurrentPrimaryKeyMatches.thenBlock().addInstr(new BreakInstruction());
				doWhileQueryNext.setCondition(ifQueryNext.getCondition());
				

				ifInstr._assignInstruction(recDoWhile, query.callMethod("record"));
				
			
				
				
				
				
				for(AbstractRelation r:manyRelations) {
					Type beanPk=Types.getRelationForeignPrimaryKeyType(r);
					EntityCls foreignCls = Entities.get(r.getDestTable()); 
					Expression foreignBeanExpression = getByRecordExpression(foreignCls, recDoWhile, QString.fromStringConstant(r.getAlias()));
					
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
//						if (foreignOneRelation.getDestTable().equals(bean.getTbl())) {
//							ifRecValueIsNotNull.thenBlock().addInstr(foreignBean.callMethodInstruction("set"+r.getSourceTable().getUc1stCamelCaseName()+"Internal", fkHelper.accessAttr("e1")));
//						}
//					}
				}
				

				
			 
		
			for(OneRelation r:oneRelations) {
				EntityCls foreignCls = Entities.get(r.getDestTable());
				Expression foreignBeanExpression = getByRecordExpression(foreignCls, recDoWhile, QString.fromStringConstant(r.getAlias()));
				
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
									bean.getAttrByName(OrmUtil.getOneRelationDestAttrName(r)))
							,  foreignBean);
				
			
//				for (OneRelation foreignOneRelation: foreignCls.getOneRelations()) {
//					if (foreignOneRelation.getDestTable().equals(bean.getTbl())) {
//						ifRelatedBeanIsNull.thenBlock().addInstr(foreignBean.callMethodInstruction("set"+r.getSourceTable().getUc1stCamelCaseName()+"Internal", e1));
//					}
//				}
			}
			}
			
			ifInstr._callMethodInstr(e1, "setLoaded", BoolExpression.TRUE);
			ifInstr._return(e1);
			_return(Expressions.Nullptr);
		}
		
		
	}
	

	public static String getMethodName(EntityCls bean,boolean lazyLoading) {
		return "fetchOne"+bean.getName()+(lazyLoading?"LazyLoading":"");
	}

}
