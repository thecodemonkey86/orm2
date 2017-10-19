package cpp.cls.bean.repo.method;

import java.util.ArrayList;
import java.util.List;

import model.AbstractRelation;
import model.Column;
import model.OneToManyRelation;
import model.PrimaryKey;
import model.OneRelation;
import cpp.Types;
import cpp.cls.Method;
import cpp.cls.Param;
import cpp.cls.QString;
import cpp.cls.Type;
import cpp.cls.bean.BeanCls;
import cpp.cls.bean.Beans;
import cpp.cls.bean.method.MethodAttrSetterInternal;
import cpp.cls.bean.method.MethodOneRelationBeanIsNull;
import cpp.cls.bean.repo.ClsBeanRepository;
import cpp.cls.bean.repo.expression.ThisBeanRepositoryExpression;
import cpp.cls.expression.BoolExpression;
import cpp.cls.expression.Expression;
import cpp.cls.expression.Expressions;
import cpp.cls.expression.Var;
import cpp.cls.instruction.IfBlock;
import cpp.cls.instruction.InstructionBlock;
import cpp.orm.OrmUtil;

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
		//_return(_this().callMethod(MethodFetchOne.getMethodName(bean),  new StdMoveExpression(getParam("query"))));
		List<OneRelation> oneRelations = bean.getOneRelations();
		//PrimaryKey pk=bean.getTbl().getPrimaryKey();
		
		Expression query = getExpressionQuery();
		//int //bCount = 2;
		//Type b1PkType = pk.isMultiColumn() ? bean.getStructPk() : BeanCls.getDatabaseMapper().columnToType(pk.getColumns().get(0));
		
		ArrayList<AbstractRelation> manyRelations = new ArrayList<>();
		
		manyRelations.addAll(bean.getOneToManyRelations());
		manyRelations.addAll(bean.getManyToManyRelations());
		
		IfBlock ifQueryNext = _if(query.callMethod("next"));
		InstructionBlock ifInstr = ifQueryNext.thenBlock();
		//Var b1Map =  ifInstr._declare((!manyRelations.isEmpty()) ? new ClsQHash(b1PkType, bean.getFetchListHelperCls()) : new ClsQSet(b1PkType), "b1Map");
		Var recDoWhile =ifInstr._declare(Types.QSqlRecord, "rec",query.callMethod("record") );
		
/*		Var b1pk = null;
		
		if (pk.isMultiColumn()) {
			b1pk =ifInstr._declare( bean.getStructPk(), "b1pk" );
			for(Column colPk:pk.getColumns()) {
				ifInstr._assign(b1pk.accessAttr(colPk.getCamelCaseName()), recDoWhile.callMethod("value", QString.fromStringConstant("b1__"+ colPk.getName())).callMethod(BeanCls.getDatabaseMapper().getQVariantConvertMethod(colPk.getDbType())));
			}
			
		} else {
			b1pk =ifInstr._declare( BeanCls.getDatabaseMapper().columnToType(pk.getFirstColumn()), "b1pk", recDoWhile.callMethod("value", QString.fromStringConstant("b1__"+ pk.getFirstColumn().getName())).callMethod(BeanCls.getDatabaseMapper().getQVariantConvertMethod(pk.getFirstColumn().getDbType())));
		}*/
		
		//IfBlock ifNotB1SetContains = ifInstr._if(Expressions.not(b1Map.callMethod("contains", b1pk)));
		
		
		Var b1 = ifInstr
				._declare(bean.toSharedPtr(), "b1", getByRecordExpression(bean, recDoWhile, QString.fromStringConstant("b1")));
		//bCount = 2;
		if (!manyRelations.isEmpty()) {
			
		//	Var structHelper = ifInstr._declare(bean.getFetchListHelperCls().toRawPointer(), "structHelper", new NewOperator(bean.getFetchListHelperCls()));

//			for(Relation r:manyRelations) {
//				Type beanPk=Types.getRelationForeignPrimaryKeyType(r);
//				ifInstr._assign(structHelper.accessAttr(r.getAlias()+"Set"),  new CreateObjectExpression(Types.qset(beanPk)));
//				//bCount++;
//			}

			ifInstr._assignInstruction(recDoWhile, query.callMethod("record"));
			
		//	Var fkHelper = ifInstr._declare(bean.getFetchListHelperCls().toRef(), "fkHelper",b1Map.arrayIndex(b1pk));
			
		//	Var structHelperIfNotB1SetContains = ifNotB1SetContains.getIfInstr()._declare(bean.getFetchListHelperCls(), "structHelper");
		//	ifNotB1SetContains.getIfInstr()._assign(structHelperIfNotB1SetContains.accessAttr("b1"), b1);
//			//bCount = 2;
//			for(Relation r:manyRelations) {
//				Type beanPk=Types.getRelationForeignPrimaryKeyType(r);
//				ifNotB1SetContains.getIfInstr()._assign(structHelperIfNotB1SetContains.accessAttr(r.getAlias()+"Set"),  new CreateObjectExpression(Types.qset(beanPk)));
//				//bCount++;
//			}
			
			//ifNotB1SetContains.getIfInstr()._callMethodInstr(b1Map, "insert", b1pk, structHelperIfNotB1SetContains );
			Var fkHelper = ifInstr._declare(bean.getFetchListHelperCls(), "fkHelper");		
			
			for(AbstractRelation r:manyRelations) {
				Type beanPk=Types.getRelationForeignPrimaryKeyType(r);
				BeanCls foreignCls = Beans.get(r.getDestTable()); 
				Expression foreignBeanExpression = getByRecordExpression(foreignCls, recDoWhile, QString.fromStringConstant(r.getAlias()));
//				IfBlock ifRecValueIsNotNull = null;
//				Var foreignBean = null;				
				
				IfBlock ifNotPkForeignIsNull= ifInstr._if(Expressions.not( recDoWhile.callMethod("value", QString.fromStringConstant(r.getAlias()+"__"+ r.getDestTable().getPrimaryKey().getFirstColumn().getName())).callMethod("isNull")));
				
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
					pkForeign = ifNotPkForeignIsNull.thenBlock()._declare(beanPk, "pkForeignB"+r.getAlias(),QString.fromStringConstant(r.getAlias()+"__"+ colPk.getName()).callMethod(BeanCls.getDatabaseMapper().getQVariantConvertMethod(colPk.getDbType())));
				}
				IfBlock ifRecValueIsNotNull = ifNotPkForeignIsNull.thenBlock()._if(
						Expressions.not(fkHelper
										.accessAttr(r.getAlias()+"Set")
										.callMethod("contains",
												pkForeign
												
												))
								
						
						
					);
				Var foreignBean =ifRecValueIsNotNull.thenBlock()._declare(foreignBeanExpression.getType(), "foreignB"+r.getAlias(),foreignBeanExpression) ;
				//					
//					
				
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
				//ifRecValueIsNotNull.getIfInstr()._callMethodInstr(foreignBean, "setLoaded", BoolExpression.TRUE);
				
				//bCount++;
			}
			

			
		} else {
			/* manyRelations.isEmpty() */
		//	ifNotB1SetContains.getIfInstr()._callMethodInstr(b1Map, "insert", b1pk);
		}
		for(OneRelation r:oneRelations) {
			BeanCls foreignCls = Beans.get(r.getDestTable());
			Expression foreignBeanExpression = getByRecordExpression(foreignCls, recDoWhile, QString.fromStringConstant(r.getAlias()));
			
			IfBlock ifRelatedBeanIsNull= ifInstr.
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
//			ifRelatedBeanIsNull.getIfInstr()._callMethodInstr(foreignBean, "setLoaded", BoolExpression.TRUE);
			
			//bCount++;
		}
		ifInstr._callMethodInstr(b1, "setLoaded", BoolExpression.TRUE);
		ifInstr._return(b1);
		_return(Expressions.Nullptr);
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	public static String getMethodName(BeanCls bean) {
		return "fetchOne"+bean.getName();
	}

}
