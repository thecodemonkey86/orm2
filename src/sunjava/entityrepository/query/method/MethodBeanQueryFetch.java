package sunjava.entityrepository.query.method;

import java.util.ArrayList;
import java.util.List;

import database.column.Column;
import database.relation.AbstractRelation;
import database.relation.OneRelation;
import database.relation.PrimaryKey;
import sunjava.core.JavaString;
import sunjava.core.Method;
import sunjava.core.Type;
import sunjava.core.Types;
import sunjava.core.expression.BoolExpression;
import sunjava.core.expression.Expression;
import sunjava.core.expression.Expressions;
import sunjava.core.expression.Var;
import sunjava.core.instruction.DoWhile;
import sunjava.core.instruction.IfBlock;
import sunjava.core.instruction.InstructionBlock;
import sunjava.entity.Entities;
import sunjava.entity.EntityCls;
import sunjava.entity.method.MethodAttrSetterInternal;
import sunjava.entity.method.MethodOneRelationBeanIsNull;
import sunjava.entityrepository.method.MethodGetFromResultSet;
import sunjava.lib.ClsArrayList;
import sunjava.lib.ClsBaseEntity;
import sunjava.lib.ClsHashMap;
import sunjava.lib.ClsHashSet;
import sunjava.lib.ClsResultSet;
import sunjava.lib.ClsSqlQuery;
import sunjava.orm.OrmUtil;

public class MethodBeanQueryFetch extends Method{
	EntityCls bean;
	
	public MethodBeanQueryFetch(EntityCls bean) {
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
		Type e1PkType = pk.isMultiColumn() ? bean.getPkType() : EntityCls.getTypeMapper().columnToType(pk.getFirstColumn());
		
		ArrayList<AbstractRelation> manyRelations = new ArrayList<>();
		
		manyRelations.addAll(bean.getOneToManyRelations());
		manyRelations.addAll(bean.getManyToManyRelations());
		
		IfBlock ifQueryNext = _if(resultSet.callMethod("next"));
		InstructionBlock ifInstr = ifQueryNext.thenBlock();
		Var e1Map =  ifInstr._declareNew((!manyRelations.isEmpty()) ? new ClsHashMap(e1PkType, bean.getFetchListHelperCls()) : new ClsHashSet(e1PkType), "e1Map");
		
		DoWhile doWhileQueryNext = ifQueryNext.thenBlock()._doWhile();
		doWhileQueryNext.setCondition(resultSet.callMethod("next"));
		
		Var e1pk = null;
		
		if (pk.isMultiColumn()) {
			Expression[] e1pkConstructorArgs = new Expression[pk.getColumnCount()]; 
			for(int i = 0; i < e1pkConstructorArgs.length; i++) {
				Column colPk = pk.getColumn(i);
				e1pkConstructorArgs[i] = EntityCls.getTypeMapper().getResultSetValueGetter(resultSet, colPk, "e1");
			}
			
			e1pk =doWhileQueryNext._declareNew( bean.getPkType(), "e1pk", e1pkConstructorArgs );
			
		} else {
			e1pk =doWhileQueryNext._declare( EntityCls.getTypeMapper().columnToType( pk.getFirstColumn()), "e1pk", EntityCls.getTypeMapper().getResultSetValueGetter(resultSet, pk.getFirstColumn(),"e1"));
		}
		
		IfBlock ifNotE1SetContains = doWhileQueryNext._if(Expressions.not(e1Map.callMethod(( (!manyRelations.isEmpty()) ?  ClsHashMap.containsKey : ClsHashSet.contains), e1pk)));
		
		
		Var e1DoWhile = ifNotE1SetContains.thenBlock()
				._declare(bean, "e1", Types.BeanRepository.callStaticMethod(MethodGetFromResultSet.getMethodName(bean), resultSet,  JavaString.stringConstant("e1")));
		//bCount = 2;
		if (!manyRelations.isEmpty()) {
			
			//Var structHelper = ifInstr._declare(bean.getFetchListHelperCls(), "structHelper", new NewOperator(bean.getFetchListHelperCls()));

//			for(Relation r:manyRelations) {
//				Type beanPk=Types.getRelationForeignPrimaryKeyType(r);
//				ifInstr._assign(structHelper.accessAttr(r.getAlias()+"Set"),  new NewOperator(Types.qset(beanPk)));
//				//bCount++;
//			}

		//	doWhileQueryNext._assignInstruction(resultSet, query.callMethod("record"));
			
			Var fkHelper = doWhileQueryNext._declare(bean.getFetchListHelperCls(), "fkHelper",e1Map.callMethod(  ClsHashMap.get,e1pk));
			
			Var fetchHelperIfNotE1SetContains = ifNotE1SetContains.thenBlock()._declareNew(bean.getFetchListHelperCls(), "fetchListHelper");
			ifNotE1SetContains.thenBlock().addInstr(fetchHelperIfNotE1SetContains.callAttrSetterMethodInstr("e1", e1DoWhile));
//			//bCount = 2;
//			for(Relation r:manyRelations) {
//				Type beanPk=Types.getRelationForeignPrimaryKeyType(r);
//				ifNotE1SetContains.thenBlock()._assign(structHelperIfNotE1SetContains.accessAttr(r.getAlias()+"Set"),  new NewOperator(Types.qset(beanPk)));
//				//bCount++;
//			}
			
			ifNotE1SetContains.thenBlock()._callMethodInstr(e1Map, ClsHashMap.put, e1pk, fetchHelperIfNotE1SetContains );
					
			
			for(AbstractRelation r:manyRelations) {
				Type beanPk=Types.getRelationForeignPrimaryKeyType(r);
				EntityCls foreignCls = Entities.get(r.getDestTable()); 
				Expression foreignBeanExpression = Types.BeanRepository.callStaticMethod(MethodGetFromResultSet.getMethodName(foreignCls),  resultSet, JavaString.stringConstant(r.getAlias()));
//				IfBlock ifRecValueIsNotNull = null;
				Var foreignBean = null;				
				
				Var pkForeign = null;
				if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
					Expression[] pkForeignConstructorArgs = new Expression[r.getDestTable().getPrimaryKey().getColumnCount()]; 
					for(int i = 0; i < r.getDestTable().getPrimaryKey().getColumnCount(); i++) {
						Column colPk =r.getDestTable().getPrimaryKey().getColumn(i);
						pkForeignConstructorArgs[i] =
								EntityCls.getTypeMapper().getResultSetValueGetter(resultSet,colPk,
										r.getAlias())
								;
					}
					pkForeign = doWhileQueryNext._declareNew(beanPk, "pkForeignB"+r.getAlias(),pkForeignConstructorArgs);
					
				} else {
					Column colPk=r.getDestTable().getPrimaryKey().getFirstColumn();
					pkForeign = doWhileQueryNext._declare(beanPk, "pkForeignB"+r.getAlias(),EntityCls.getTypeMapper().getResultSetValueGetter(resultSet,colPk, r.getAlias()));
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
				
								
				ifRecValueIsNotNull.thenBlock().addInstr(fkHelper.callAttrGetter("e1")
						.callMethodInstruction(EntityCls.getRelatedBeanMethodName(r), foreignBean));
				ifRecValueIsNotNull.thenBlock().addInstr(
						fkHelper.callAttrGetter(r.getAlias()+"Set")
						.callMethod(ClsHashSet.add, 
								pkForeign
									).asInstruction())
					 ;
				
				for (OneRelation foreignOneRelation: foreignCls.getOneRelations()) {
					if (foreignOneRelation.getDestTable().equals(bean.getTbl())) {
						ifRecValueIsNotNull.thenBlock().addInstr(foreignBean.callMethodInstruction("set"+r.getSourceTable().getUc1stCamelCaseName()+"Internal", fkHelper.accessAttr("e1")));
					}
				}
				//ifRecValueIsNotNull.thenBlock()._callMethodInstr(foreignBean, "setLoaded", BoolExpression.TRUE);
				
				//bCount++;
			}
			

			
		} else {
			/* manyRelations.isEmpty() */
			ifNotE1SetContains.thenBlock()._callMethodInstr(e1Map, ClsHashSet.add, e1pk);
		}
		for(OneRelation r:oneRelations) {
			EntityCls foreignCls = Entities.get(r.getDestTable());
			Expression foreignBeanExpression = Types.BeanRepository.callStaticMethod(MethodGetFromResultSet.getMethodName(foreignCls), resultSet, JavaString.stringConstant(r.getAlias()));
			
			IfBlock ifRelatedBeanIsNull= ifNotE1SetContains.thenBlock().
					_if(e1DoWhile.callMethod(new MethodOneRelationBeanIsNull(r)));
			
			Var foreignBean =ifRelatedBeanIsNull.thenBlock()._declare(foreignBeanExpression.getType(), "foreignB"+r.getAlias(),foreignBeanExpression) ;
			ifRelatedBeanIsNull.thenBlock()
				._callMethodInstr(
						e1DoWhile ,
						new MethodAttrSetterInternal(foreignCls,
								bean.getAttrByName(OrmUtil.getOneRelationDestAttrName(r)))
						,  foreignBean);
			
		
			for (OneRelation foreignOneRelation: foreignCls.getOneRelations()) {
				if (foreignOneRelation.getDestTable().equals(bean.getTbl())) {
					ifRelatedBeanIsNull.thenBlock().addInstr(foreignBean.callMethodInstruction("set"+r.getSourceTable().getUc1stCamelCaseName()+"Internal", e1DoWhile));
				}
			}
//			ifRelatedBeanIsNull.thenBlock()._callMethodInstr(foreignBean, "setLoaded", BoolExpression.TRUE);
			
			//bCount++;
		}
		ifNotE1SetContains.thenBlock()._callMethodInstr(e1DoWhile, ClsBaseEntity.setLoaded, BoolExpression.TRUE);
		ifNotE1SetContains.thenBlock()._callMethodInstr(result, ClsArrayList.add, e1DoWhile);
		_return(result);
	}
	@Override
	public boolean includeIfEmpty() {
		return true;
	}
}
