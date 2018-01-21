package cpp.beanrepository.method;

import java.util.ArrayList;
import java.util.List;

import cpp.Types;
import cpp.bean.BeanCls;
import cpp.bean.Beans;
import cpp.bean.method.MethodAttrSetterInternal;
import cpp.bean.method.MethodOneRelationBeanIsNull;
import cpp.beanrepository.ClsBeanRepository;
import cpp.beanrepository.expression.ThisBeanRepositoryExpression;
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
import cpp.lib.ClsQSqlRecord;
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
	protected BeanCls bean;
	
	public MethodFetchOne(List<OneRelation> oneRelations,List<OneToManyRelation> manyRelations, BeanCls bean,PrimaryKey pk) {
		super(Public, bean.toSharedPtr(),  getMethodName(bean));
		addParam(new Param(Types.QSqlQuery, "query"));	
		this.oneRelations = oneRelations;
		this.manyRelations = manyRelations;
		this.pk = pk;
		this.bean = bean;
	}
	
	@Override
	public ThisBeanRepositoryExpression _this() {
		return new ThisBeanRepositoryExpression((ClsBeanRepository) parent);
	}
	
	protected Expression getByRecordExpression(BeanCls bean, Var record, QString alias) {
	//return new ThisBeanRepositoryExpression((BeanRepository) parent);
		return _this().callMethod(MethodGetFromRecord.getMethodName(bean),  record, alias);
}

protected Expression getExpressionQuery() {
	return  getParam("query");
}

	@Override
	public void addImplementation() {
		List<OneRelation> oneRelations = bean.getOneRelations();
		
		Expression query = getExpressionQuery();
		
		ArrayList<AbstractRelation> manyRelations = new ArrayList<>();
		
		manyRelations.addAll(bean.getOneToManyRelations());
		manyRelations.addAll(bean.getManyToManyRelations());
		
		IfBlock ifQueryNext = _if(query.callMethod("next"));
		InstructionBlock ifInstr = ifQueryNext.thenBlock();
		Var recDoWhile =ifInstr._declare(Types.QSqlRecord, "rec",query.callMethod("record") );
		Var b1 = ifInstr
				._declare(bean.toSharedPtr(), "b1", getByRecordExpression(bean, recDoWhile, QString.fromStringConstant("b1")));
		
		Var fkHelper = null;
		if (!manyRelations.isEmpty()) {
			fkHelper = ifInstr._declare(bean.getFetchListHelperCls(), "fkHelper");		
			
		}
		
		
		DoWhile doWhileQueryNext = ifInstr._doWhile();
		
		doWhileQueryNext.setCondition(Expressions.and(ifQueryNext.getCondition(),recDoWhile.callMethod(ClsQSqlRecord.value, QString.fromStringConstant("b1__" + bean.getTbl().getPrimaryKey().getFirstColumn().getName())).callMethod(BeanCls.getDatabaseMapper().getQVariantConvertMethod(bean.getTbl().getPrimaryKey().getFirstColumn().getDbType()))._equals(b1.callAttrGetter(bean.getTbl().getPrimaryKey().getFirstColumn().getCamelCaseName())) ));
		
		
		
		if (!manyRelations.isEmpty()) {
			
		

			ifInstr._assignInstruction(recDoWhile, query.callMethod("record"));
			
		
			
			
			
			
			for(AbstractRelation r:manyRelations) {
				Type beanPk=Types.getRelationForeignPrimaryKeyType(r);
				BeanCls foreignCls = Beans.get(r.getDestTable()); 
				Expression foreignBeanExpression = getByRecordExpression(foreignCls, recDoWhile, QString.fromStringConstant(r.getAlias()));
				
				IfBlock ifNotPkForeignIsNull= doWhileQueryNext._if(Expressions.not( recDoWhile.callMethod("value", QString.fromStringConstant(r.getAlias()+"__"+ r.getDestTable().getPrimaryKey().getFirstColumn().getName())).callMethod("isNull")));
				
				Var pkForeign = null;
				if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
					pkForeign = ifNotPkForeignIsNull.thenBlock()._declare(beanPk, "pkForeignB"+r.getAlias());
					
					for(Column colPk:r.getDestTable().getPrimaryKey().getColumns()) {
						ifNotPkForeignIsNull.thenBlock()._assign(
								pkForeign.accessAttr(colPk.getCamelCaseName()), 
								recDoWhile.callMethod("value",
										QString.fromStringConstant(r.getAlias()+"__"+ colPk.getName())).callMethod(BeanCls.getDatabaseMapper().getQVariantConvertMethod(colPk.getDbType()))
								);
					}
				} else {
					Column colPk=r.getDestTable().getPrimaryKey().getFirstColumn();
					pkForeign = ifNotPkForeignIsNull.thenBlock()._declare(beanPk, "pkForeignB"+r.getAlias(),recDoWhile.callMethod("value",QString.fromStringConstant(r.getAlias()+"__"+ colPk.getName())).callMethod(BeanCls.getDatabaseMapper().getQVariantConvertMethod(colPk.getDbType())));
				}
				IfBlock ifRecValueIsNotNull = ifNotPkForeignIsNull.thenBlock()._if(
						Expressions.not(fkHelper
										.accessAttr(r.getAlias()+"Set")
										.callMethod("contains",
												pkForeign
												
												))
								
						
						
					);
				Var foreignBean =ifRecValueIsNotNull.thenBlock()._declare(foreignBeanExpression.getType(), "foreignB"+r.getAlias(),foreignBeanExpression) ;
				
				ifRecValueIsNotNull.thenBlock().addInstr(fkHelper.accessAttr("b1")
						.callMethodInstruction(BeanCls.getRelatedBeanMethodName(r), foreignBean));
				ifRecValueIsNotNull.thenBlock().addInstr(
						fkHelper.accessAttr(r.getAlias()+"Set")
						.callMethod("insert", 
								pkForeign
									).asInstruction())
					 ;
				
				for (OneRelation foreignOneRelation: foreignCls.getOneRelations()) {
					if (foreignOneRelation.getDestTable().equals(bean.getTbl())) {
						ifRecValueIsNotNull.thenBlock().addInstr(foreignBean.callMethodInstruction("set"+r.getSourceTable().getUc1stCamelCaseName()+"Internal", fkHelper.accessAttr("b1")));
					}
				}
			}
			

			
		} else {
			/* manyRelations.isEmpty() */
		//	ifNotB1SetContains.getIfInstr()._callMethodInstr(b1Map, "insert", b1pk);
		}
		for(OneRelation r:oneRelations) {
			BeanCls foreignCls = Beans.get(r.getDestTable());
			Expression foreignBeanExpression = getByRecordExpression(foreignCls, recDoWhile, QString.fromStringConstant(r.getAlias()));
			
			IfBlock ifRelatedBeanIsNull= doWhileQueryNext.
					_if(b1.callMethod(new MethodOneRelationBeanIsNull(r)));
			
			Var foreignBean =ifRelatedBeanIsNull.thenBlock()._declare(foreignBeanExpression.getType(), "foreignB"+r.getAlias(),foreignBeanExpression) ;
			ifRelatedBeanIsNull.thenBlock()
				._callMethodInstr(
						b1 ,
						new MethodAttrSetterInternal(foreignCls,
								bean.getAttrByName(OrmUtil.getOneRelationDestAttrName(r)))
						,  foreignBean);
			
		
			for (OneRelation foreignOneRelation: foreignCls.getOneRelations()) {
				if (foreignOneRelation.getDestTable().equals(bean.getTbl())) {
					ifRelatedBeanIsNull.thenBlock().addInstr(foreignBean.callMethodInstruction("set"+r.getSourceTable().getUc1stCamelCaseName()+"Internal", b1));
				}
			}
		}
		
		doWhileQueryNext.addInstr(recDoWhile.assign(query.callMethod("record")));
		ifInstr._callMethodInstr(b1, "setLoaded", BoolExpression.TRUE);
		ifInstr._return(b1);
		_return(Expressions.Nullptr);
	}
	

	public static String getMethodName(BeanCls bean) {
		return "fetchOne"+bean.getName();
	}

}
