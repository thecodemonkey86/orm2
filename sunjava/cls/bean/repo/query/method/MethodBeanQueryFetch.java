package sunjava.cls.bean.repo.query.method;

import java.util.ArrayList;
import java.util.List;

import model.AbstractRelation;
import model.Column;
import model.OneRelation;
import model.PrimaryKey;
import sunjava.Types;
import sunjava.cls.JavaString;
import sunjava.cls.Method;
import sunjava.cls.Type;
import sunjava.cls.bean.BeanCls;
import sunjava.cls.bean.Beans;
import sunjava.cls.bean.method.MethodAttrSetterInternal;
import sunjava.cls.bean.method.MethodOneRelationBeanIsNull;
import sunjava.cls.bean.repo.method.MethodGetFromResultSet;
import sunjava.cls.expression.BoolExpression;
import sunjava.cls.expression.Expression;
import sunjava.cls.expression.Expressions;
import sunjava.cls.expression.Var;
import sunjava.cls.instruction.DoWhile;
import sunjava.cls.instruction.IfBlock;
import sunjava.cls.instruction.InstructionBlock;
import sunjava.lib.ClsArrayList;
import sunjava.lib.ClsBaseBean;
import sunjava.lib.ClsHashMap;
import sunjava.lib.ClsHashSet;
import sunjava.lib.ClsResultSet;
import sunjava.lib.ClsSqlQuery;
import sunjava.orm.OrmUtil;

public class MethodBeanQueryFetch extends Method{
	BeanCls bean;
	
	public MethodBeanQueryFetch(BeanCls bean) {
		super(Public, Types.arraylist(bean), "fetch");
		this.bean=bean;
	}

	@Override
	public void addImplementation() {
		addThrowsException(Types.SqlException);
//		Expression aSqlCon = _this().accessAttr("sqlCon");
//		_return(Types.BeanRepository.callStaticMethod(MethodFetchListStatic.getMethodName(bean), aSqlCon, _this().callMethod("execQuery")));
//		_return(_null());
		
		
//		_return(parent.callStaticMethod("fetchList"+bean.getName()+"Static", _this().accessAttr("sqlCon"), getParam("query")));
//		_return(_null());
		//BeanCls cls = (BeanCls) parent;
		List<OneRelation> oneRelations = bean.getOneRelations();
		PrimaryKey pk=bean.getTbl().getPrimaryKey();
		
		Var result = _declareNew(returnType, "result");
		Var resultSet =_declare(Types.ResultSet, "resultSet",_this().callMethod(ClsSqlQuery.query) );
		
		//int //bCount = 2;
		Type b1PkType = pk.isMultiColumn() ? bean.getPkType() : BeanCls.getTypeMapper().columnToType(pk.getFirstColumn());
		
		ArrayList<AbstractRelation> manyRelations = new ArrayList<>();
		
		manyRelations.addAll(bean.getOneToManyRelations());
		manyRelations.addAll(bean.getManyToManyRelations());
		
		IfBlock ifQueryNext = _if(resultSet.callMethod("next"));
		InstructionBlock ifInstr = ifQueryNext.thenBlock();
		Var b1Map =  ifInstr._declareNew((!manyRelations.isEmpty()) ? new ClsHashMap(b1PkType, bean.getFetchListHelperCls()) : new ClsHashSet(b1PkType), "b1Map");
		
		DoWhile doWhileQueryNext = ifQueryNext.thenBlock()._doWhile();
		doWhileQueryNext.setCondition(resultSet.callMethod("next"));
		
		Var b1pk = null;
		
		if (pk.isMultiColumn()) {
			Expression[] b1pkConstructorArgs = new Expression[pk.getColumnCount()]; 
			for(int i = 0; i < b1pkConstructorArgs.length; i++) {
				Column colPk = pk.getColumn(i);
				b1pkConstructorArgs[i] = BeanCls.getTypeMapper().getResultSetValueGetter(resultSet, colPk, "b1");
			}
			
			b1pk =doWhileQueryNext._declareNew( bean.getPkType(), "b1pk", b1pkConstructorArgs );
			
		} else {
			b1pk =doWhileQueryNext._declare( BeanCls.getTypeMapper().columnToType( pk.getFirstColumn()), "b1pk", BeanCls.getTypeMapper().getResultSetValueGetter(resultSet, pk.getFirstColumn(),"b1"));
		}
		
		IfBlock ifNotB1SetContains = doWhileQueryNext._if(Expressions.not(b1Map.callMethod(( (!manyRelations.isEmpty()) ?  ClsHashMap.containsKey : ClsHashSet.contains), b1pk)));
		
		
		Var b1DoWhile = ifNotB1SetContains.thenBlock()
				._declare(bean, "b1", Types.BeanRepository.callStaticMethod(MethodGetFromResultSet.getMethodName(bean), resultSet,  JavaString.stringConstant("b1")));
		//bCount = 2;
		if (!manyRelations.isEmpty()) {
			
			//Var structHelper = ifInstr._declare(bean.getFetchListHelperCls(), "structHelper", new NewOperator(bean.getFetchListHelperCls()));

//			for(Relation r:manyRelations) {
//				Type beanPk=Types.getRelationForeignPrimaryKeyType(r);
//				ifInstr._assign(structHelper.accessAttr(r.getAlias()+"Set"),  new NewOperator(Types.qset(beanPk)));
//				//bCount++;
//			}

		//	doWhileQueryNext._assignInstruction(resultSet, query.callMethod("record"));
			
			Var fkHelper = doWhileQueryNext._declare(bean.getFetchListHelperCls(), "fkHelper",b1Map.callMethod(  ClsHashMap.get,b1pk));
			
			Var fetchHelperIfNotB1SetContains = ifNotB1SetContains.thenBlock()._declareNew(bean.getFetchListHelperCls(), "fetchListHelper");
			ifNotB1SetContains.thenBlock().addInstr(fetchHelperIfNotB1SetContains.callAttrSetterMethodInstr("b1", b1DoWhile));
//			//bCount = 2;
//			for(Relation r:manyRelations) {
//				Type beanPk=Types.getRelationForeignPrimaryKeyType(r);
//				ifNotB1SetContains.thenBlock()._assign(structHelperIfNotB1SetContains.accessAttr(r.getAlias()+"Set"),  new NewOperator(Types.qset(beanPk)));
//				//bCount++;
//			}
			
			ifNotB1SetContains.thenBlock()._callMethodInstr(b1Map, ClsHashMap.put, b1pk, fetchHelperIfNotB1SetContains );
					
			
			for(AbstractRelation r:manyRelations) {
				Type beanPk=Types.getRelationForeignPrimaryKeyType(r);
				BeanCls foreignCls = Beans.get(r.getDestTable()); 
				Expression foreignBeanExpression = Types.BeanRepository.callStaticMethod(MethodGetFromResultSet.getMethodName(foreignCls),  resultSet, JavaString.stringConstant(r.getAlias()));
//				IfBlock ifRecValueIsNotNull = null;
				Var foreignBean = null;				
				
				Var pkForeign = null;
				if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
					Expression[] pkForeignConstructorArgs = new Expression[r.getDestTable().getPrimaryKey().getColumnCount()]; 
					for(int i = 0; i < r.getDestTable().getPrimaryKey().getColumnCount(); i++) {
						Column colPk =r.getDestTable().getPrimaryKey().getColumn(i);
						pkForeignConstructorArgs[i] =
								BeanCls.getTypeMapper().getResultSetValueGetter(resultSet,colPk,
										r.getAlias())
								;
					}
					pkForeign = doWhileQueryNext._declareNew(beanPk, "pkForeignB"+r.getAlias(),pkForeignConstructorArgs);
					
				} else {
					Column colPk=r.getDestTable().getPrimaryKey().getFirstColumn();
					pkForeign = doWhileQueryNext._declare(beanPk, "pkForeignB"+r.getAlias(),BeanCls.getTypeMapper().getResultSetValueGetter(resultSet,colPk, r.getAlias()));
				}
				IfBlock ifNotPkForeignIsNull= doWhileQueryNext._if(_not(resultSet.callMethod(ClsResultSet.wasNull)));
				
				IfBlock ifRecValueIsNotNull = ifNotPkForeignIsNull.thenBlock()._if(
						Expressions.not(fkHelper
										.callAttrGetter(r.getAlias()+"Set")
										.callMethod("contains",
												pkForeign
												
												))
								
						
						
					);
				foreignBean =ifRecValueIsNotNull.thenBlock()._declare(foreignBeanExpression.getType(), "foreignB"+r.getAlias(),foreignBeanExpression) ;
				
								
				ifRecValueIsNotNull.thenBlock().addInstr(fkHelper.callAttrGetter("b1")
						.callMethodInstruction(BeanCls.getRelatedBeanMethodName(r), foreignBean));
				ifRecValueIsNotNull.thenBlock().addInstr(
						fkHelper.callAttrGetter(r.getAlias()+"Set")
						.callMethod(ClsHashSet.add, 
								pkForeign
									).asInstruction())
					 ;
				
				for (OneRelation foreignOneRelation: foreignCls.getOneRelations()) {
					if (foreignOneRelation.getDestTable().equals(bean.getTbl())) {
						ifRecValueIsNotNull.thenBlock().addInstr(foreignBean.callMethodInstruction("set"+r.getSourceTable().getUc1stCamelCaseName()+"Internal", fkHelper.accessAttr("b1")));
					}
				}
				//ifRecValueIsNotNull.thenBlock()._callMethodInstr(foreignBean, "setLoaded", BoolExpression.TRUE);
				
				//bCount++;
			}
			

			
		} else {
			/* manyRelations.isEmpty() */
			ifNotB1SetContains.thenBlock()._callMethodInstr(b1Map, ClsHashSet.add, b1pk);
		}
		for(OneRelation r:oneRelations) {
			BeanCls foreignCls = Beans.get(r.getDestTable());
			Expression foreignBeanExpression = Types.BeanRepository.callStaticMethod(MethodGetFromResultSet.getMethodName(foreignCls), resultSet, JavaString.stringConstant(r.getAlias()));
			
			IfBlock ifRelatedBeanIsNull= ifNotB1SetContains.thenBlock().
					_if(b1DoWhile.callMethod(new MethodOneRelationBeanIsNull(r)));
			
			Var foreignBean =ifRelatedBeanIsNull.thenBlock()._declare(foreignBeanExpression.getType(), "foreignB"+r.getAlias(),foreignBeanExpression) ;
			ifRelatedBeanIsNull.thenBlock()
				._callMethodInstr(
						b1DoWhile ,
						new MethodAttrSetterInternal(foreignCls,
								bean.getAttrByName(OrmUtil.getOneRelationDestAttrName(r)))
						,  foreignBean);
			
		
			for (OneRelation foreignOneRelation: foreignCls.getOneRelations()) {
				if (foreignOneRelation.getDestTable().equals(bean.getTbl())) {
					ifRelatedBeanIsNull.thenBlock().addInstr(foreignBean.callMethodInstruction("set"+r.getSourceTable().getUc1stCamelCaseName()+"Internal", b1DoWhile));
				}
			}
//			ifRelatedBeanIsNull.thenBlock()._callMethodInstr(foreignBean, "setLoaded", BoolExpression.TRUE);
			
			//bCount++;
		}
		ifNotB1SetContains.thenBlock()._callMethodInstr(b1DoWhile, ClsBaseBean.setLoaded, BoolExpression.TRUE);
		ifNotB1SetContains.thenBlock()._callMethodInstr(result, ClsArrayList.add, b1DoWhile);
		_return(result);
	}
	@Override
	public boolean includeIfEmpty() {
		return true;
	}
}
