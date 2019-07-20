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
import cpp.entityrepository.ClsEntityRepository;
import cpp.entityrepository.expression.ThisEntityRepositoryExpression;
import cpp.lib.ClsQHash;
import cpp.lib.ClsQSet;
import cpp.lib.ClsQSqlQuery;
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
	protected EntityCls bean;
	protected Param pQuery;
	protected boolean lazyLoading;
	
	public MethodFetchList(EntityCls bean,PrimaryKey pk,boolean lazyLoading) {
		super(Public, Types.qvector(bean.toSharedPtr()),getMethodName(bean,lazyLoading) );
		pQuery = addParam(Types.QSqlQuery.toRValueRef(), "query");	
		//this.oneRelations = oneRelations;
		//this.manyRelations = manyRelations;
		this.pk = pk;
		this.bean = bean;
		this.lazyLoading = lazyLoading;
	}
	
	@Override
	public ThisEntityRepositoryExpression _this() {
		return new ThisEntityRepositoryExpression((ClsEntityRepository) parent);
	}
	
	protected Expression getExpressionQuery() {
		return  pQuery;
	}

	protected Expression getByRecordExpression(EntityCls bean, Var record, QString alias) {
		//return new ThisBeanRepositoryExpression((BeanRepository) parent);
		return _this().callMethod(MethodGetFromRecord.getMethodName(bean),  record, alias);
	}
		
	@Override
	public void addImplementation() {
		List<OneRelation> oneRelations = bean.getOneRelations();
		PrimaryKey pk=bean.getTbl().getPrimaryKey();
		
		Var result = _declare(returnType, "result");
		Expression query = getExpressionQuery();
		//int //bCount = 2;
		Type b1PkType = pk.isMultiColumn() ? bean.getStructPk() : EntityCls.getDatabaseMapper().columnToType( pk.getColumns().get(0));
		
		ArrayList<AbstractRelation> manyRelations = new ArrayList<>();
		
		manyRelations.addAll(bean.getOneToManyRelations());
		manyRelations.addAll(bean.getManyToManyRelations());
		
		IfBlock ifQueryNext = _if(query.callMethod("next"));
		InstructionBlock ifInstr = ifQueryNext.thenBlock();
		Var b1Map =  ifInstr._declare((!manyRelations.isEmpty()) ? new ClsQHash(b1PkType, bean.getFetchListHelperCls()) : new ClsQSet(b1PkType), "b1Map");
		
		DoWhile doWhileQueryNext = ifQueryNext.thenBlock()._doWhile();
		doWhileQueryNext.setCondition(query.callMethod("next"));
		Var recDoWhile =doWhileQueryNext._declare(Types.QSqlRecord, "rec",query.callMethod("record") );
		
		Var b1pk = null;
		
		if (pk.isMultiColumn()) {
			b1pk =doWhileQueryNext._declare( bean.getStructPk(), "b1pk" );
			for(Column colPk:pk.getColumns()) {
				doWhileQueryNext._assign(b1pk.accessAttr(colPk.getCamelCaseName()), recDoWhile.callMethod("value", QString.fromStringConstant("b1__"+ colPk.getName())).callMethod(EntityCls.getDatabaseMapper().getQVariantConvertMethod(colPk.getDbType())));
			}
			
		} else {
			b1pk =doWhileQueryNext._declare( EntityCls.getDatabaseMapper().columnToType(pk.getFirstColumn()), "b1pk", recDoWhile.callMethod("value", QString.fromStringConstant("b1__"+ pk.getFirstColumn().getName())).callMethod(EntityCls.getDatabaseMapper().getQVariantConvertMethod(pk.getFirstColumn().getDbType())));
		}
		
		IfBlock ifNotB1SetContains = doWhileQueryNext._if(Expressions.not(b1Map.callMethod("contains", b1pk)));
		
		
		Var b1DoWhile = ifNotB1SetContains.thenBlock()
				._declare(bean.toSharedPtr(), "b1", getByRecordExpression(bean, recDoWhile, QString.fromStringConstant("b1")));
		
		if(!this.lazyLoading) {
		
			//bCount = 2;
			if (!manyRelations.isEmpty()) {
				
				//Var structHelper = ifInstr._declare(bean.getFetchListHelperCls().toRawPointer(), "structHelper", new NewOperator(bean.getFetchListHelperCls()));
	
	//			for(Relation r:manyRelations) {
	//				Type beanPk=Types.getRelationForeignPrimaryKeyType(r);
	//				ifInstr._assign(structHelper.accessAttr(r.getAlias()+"Set"),  new CreateObjectExpression(Types.qset(beanPk)));
	//				//bCount++;
	//			}
	
				doWhileQueryNext._assignInstruction(recDoWhile, query.callMethod("record"));
				
				Var fkHelper = doWhileQueryNext._declare(bean.getFetchListHelperCls().toRef(), "fkHelper",b1Map.arrayIndex(b1pk));
				
				Var structHelperIfNotB1SetContains = ifNotB1SetContains.thenBlock()._declare(bean.getFetchListHelperCls(), "structHelper");
				ifNotB1SetContains.thenBlock()._assign(structHelperIfNotB1SetContains.accessAttr("b1"), b1DoWhile);
	//			//bCount = 2;
	//			for(Relation r:manyRelations) {
	//				Type beanPk=Types.getRelationForeignPrimaryKeyType(r);
	//				ifNotB1SetContains.getIfInstr()._assign(structHelperIfNotB1SetContains.accessAttr(r.getAlias()+"Set"),  new CreateObjectExpression(Types.qset(beanPk)));
	//				//bCount++;
	//			}
				
				ifNotB1SetContains.thenBlock()._callMethodInstr(b1Map, "insert", b1pk, structHelperIfNotB1SetContains );
						
				
				for(AbstractRelation r:manyRelations) {
					Type beanPk=Types.getRelationForeignPrimaryKeyType(r);
					EntityCls foreignCls = Entities.get(r.getDestTable()); 
					Expression foreignBeanExpression = getByRecordExpression(foreignCls, recDoWhile, QString.fromStringConstant(r.getAlias()));
	//				IfBlock ifRecValueIsNotNull = null;
					Var foreignBean = null;				
					
					IfBlock ifNotPkForeignIsNull= doWhileQueryNext._if(Expressions.not( recDoWhile.callMethod("value", QString.fromStringConstant(r.getAlias()+"__"+ r.getDestTable().getPrimaryKey().getFirstColumn().getName())).callMethod("isNull")));
					
					Var pkForeign = null;
					if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
						pkForeign = ifNotPkForeignIsNull.thenBlock()._declare(beanPk, "pkForeignB"+r.getAlias());
						
						for(Column colPk:r.getDestTable().getPrimaryKey().getColumns()) {
							ifNotPkForeignIsNull.thenBlock()._assign(
									pkForeign.accessAttr(colPk.getCamelCaseName()), 
									recDoWhile.callMethod("value",
											QString.fromStringConstant(r.getAlias()+"__"+ colPk.getName())).callMethod(EntityCls.getDatabaseMapper().getQVariantConvertMethod(colPk.getDbType()))
									);
						}
					} else {
						Column colPk=r.getDestTable().getPrimaryKey().getFirstColumn();
						pkForeign = ifNotPkForeignIsNull.thenBlock()._declare(beanPk, "pkForeignB"+r.getAlias(),recDoWhile.callMethod("value",QString.fromStringConstant(r.getAlias()+"__"+ colPk.getName())).callMethod(EntityCls.getDatabaseMapper().getQVariantConvertMethod(colPk.getDbType())));
					}
					IfBlock ifRecValueIsNotNull = ifNotPkForeignIsNull.thenBlock()._if(
							Expressions.not(fkHelper
											.accessAttr(r.getAlias()+"Set")
											.callMethod("contains",
													pkForeign
													
													))
									
							
							
						);
					foreignBean =ifRecValueIsNotNull.thenBlock()._declare(foreignBeanExpression.getType(), "foreignB"+r.getAlias(),foreignBeanExpression) ;
					
									
					ifRecValueIsNotNull.thenBlock().addInstr(fkHelper.accessAttr("b1")
							.callMethodInstruction(EntityCls.getRelatedBeanMethodName(r), foreignBean));
					ifRecValueIsNotNull.thenBlock().addInstr(
							fkHelper.accessAttr(r.getAlias()+"Set")
							.callMethod("insert", 
									pkForeign
										).asInstruction())
						 ;
					
//					for (OneRelation foreignOneRelation: foreignCls.getOneRelations()) {
//						if (foreignOneRelation.getDestTable().equals(bean.getTbl())) {
//							ifRecValueIsNotNull.thenBlock().addInstr(foreignBean.callMethodInstruction("set"+r.getSourceTable().getUc1stCamelCaseName()+"Internal", fkHelper.accessAttr("b1")));
//						}
//					}
					//ifRecValueIsNotNull.getIfInstr()._callMethodInstr(foreignBean, "setLoaded", BoolExpression.TRUE);
					
					//bCount++;
				}
				
	
				
			} else {
				/* manyRelations.isEmpty() */
				ifNotB1SetContains.thenBlock()._callMethodInstr(b1Map, "insert", b1pk);
			}
			for(OneRelation r:oneRelations) {
				EntityCls foreignCls = Entities.get(r.getDestTable());
				Expression foreignBeanExpression = getByRecordExpression(foreignCls, recDoWhile, QString.fromStringConstant(r.getAlias()));
				
				IfBlock ifRelatedBeanIsNull= ifNotB1SetContains.thenBlock().
						_if(Expressions.and( b1DoWhile.callMethod(new MethodOneRelationEntityIsNull(r))
								,
								Expressions.not( recDoWhile.callMethod("value", QString.fromStringConstant(r.getAlias()+"__"+ r.getDestTable().getPrimaryKey().getFirstColumn().getName())).callMethod(ClsQVariant.isNull))
								));
				
				Var foreignBean =ifRelatedBeanIsNull.thenBlock()._declare(foreignBeanExpression.getType(), "foreignB"+r.getAlias(),foreignBeanExpression) ;
				ifRelatedBeanIsNull.thenBlock()
					._callMethodInstr(
							b1DoWhile ,
							new MethodAttrSetterInternal(foreignCls,
									bean.getAttrByName(OrmUtil.getOneRelationDestAttrName(r)))
							,  foreignBean);
				
			
	//			for (OneRelation foreignOneRelation: foreignCls.getOneRelations()) {
	//				if (foreignOneRelation.getDestTable().equals(bean.getTbl())) {
	//					ifRelatedBeanIsNull.thenBlock().addInstr(foreignBean.callMethodInstruction("set"+r.getSourceTable().getUc1stCamelCaseName()+"Internal", b1DoWhile));
	//				}
	//			}
	//			ifRelatedBeanIsNull.getIfInstr()._callMethodInstr(foreignBean, "setLoaded", BoolExpression.TRUE);
				
				//bCount++;
			}
			
			ifNotB1SetContains.thenBlock()._callMethodInstr(b1DoWhile, "setLoaded", BoolExpression.TRUE);
		}
		ifNotB1SetContains.thenBlock()._callMethodInstr(result, "append", b1DoWhile);
		_callMethodInstr(query, ClsQSqlQuery.clear); 
		_return(result);
		
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	public static String getMethodName(EntityCls bean,boolean lazyLoading) {
		return  "fetchList"+bean.getName()+(lazyLoading?"LazyLoading":"");
	}

}
