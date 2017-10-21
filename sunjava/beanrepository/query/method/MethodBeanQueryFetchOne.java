package sunjava.beanrepository.query.method;

import java.util.ArrayList;
import java.util.List;

import database.column.Column;
import database.relation.AbstractRelation;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import sunjava.bean.BeanCls;
import sunjava.bean.Beans;
import sunjava.bean.method.MethodOneRelationAttrSetter;
import sunjava.bean.method.MethodOneRelationBeanIsNull;
import sunjava.beanrepository.method.MethodGetFromResultSet;
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
import sunjava.lib.ClsHashSet;
import sunjava.lib.ClsResultSet;
import sunjava.lib.ClsSqlQuery;
import util.pg.PgCppUtil;

public class MethodBeanQueryFetchOne extends Method{
	BeanCls bean;
	
	public MethodBeanQueryFetchOne(BeanCls bean) {
		super(Public, bean, "fetchOne");
		this.bean=bean;
	}

	@Override
	public void addImplementation() {
		addThrowsException(Types.SqlException);

		List<OneRelation> oneRelations = bean.getOneRelations();
		List<OneToManyRelation> oneToManyRelations = bean.getOneToManyRelations();
		List<ManyRelation> manyToManyRelations = bean.getManyToManyRelations();
		
		
		Var resultSet =_declare(Types.ResultSet, "resultSet",_this().callMethod(ClsSqlQuery.query) );
		
		Var b1 = _declare(returnType, "b1", Expressions.Null);
		IfBlock ifQSqlQueryNext =
				_if(resultSet.callMethod(ClsResultSet.METHOD_NAME_NEXT))
				
//		for(Column col:bean.getTbl().getPrimaryKey().getColumns()) {		
//		OrmUtil.addAssignValueFromResultSetInstructions(resultSet, ifQSqlQueryNext.getIfInstr(), b1, col, "b1");
//		}
					.setIfInstr(
							b1.assign(Types.BeanRepository.callStaticMethod(MethodGetFromResultSet.getMethodName(bean),  resultSet, JavaString.stringConstant("b1")))
							,
							b1.callAttrSetterMethodInstr("loaded", BoolExpression.TRUE)//_assignInstruction(b1.accessAttr("loaded"), BoolExpression.TRUE)
							)
							;
		
		
		DoWhile doWhileQSqlQueryNext = DoWhile.create();
		
		
		ArrayList<AbstractRelation> manyRelations = new ArrayList<>(oneToManyRelations.size()+manyToManyRelations.size());
		manyRelations.addAll(oneToManyRelations);
		manyRelations.addAll(manyToManyRelations);
		
		for(OneRelation r:oneRelations) {
//			BeanCls foreignCls = Beans.get(r.getDestTable()); 
			IfBlock ifBlock= doWhileQSqlQueryNext._if(b1.callMethod(new MethodOneRelationBeanIsNull(r)));
			ifBlock.thenBlock().
			_callMethodInstr(b1, new MethodOneRelationAttrSetter( b1.getClassConcreteType().getAttrByName(PgCppUtil.getOneRelationDestAttrName(r)), true,r.isPartOfPk()), 
					Types.BeanRepository.callStaticMethod(MethodGetFromResultSet.getMethodName(Beans.get(r.getDestTable())),  resultSet, JavaString.stringConstant(r.getAlias())));
		}
		for(AbstractRelation r:manyRelations) {
			BeanCls foreignCls = Beans.get(r.getDestTable());  
			if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
				Var pkSet = ifQSqlQueryNext.thenBlock()._declareInitDefaultConstructor(Types.hashset(foreignCls.getPkType()), "pkSet"+r.getAlias());
				
				Expression[] pkArgs = new Expression[ r.getDestTable().getPrimaryKey().getColumnCount()];
				for(int i = 0; i < pkArgs.length; i++) {
					Expression resultSetValueGetter = BeanCls.getTypeMapper().getResultSetValueGetter(resultSet, r.getDestTable().getPrimaryKey().getColumn(i),JavaString.stringConstant(r.getAlias()));
					pkArgs[i] = resultSetValueGetter;
				}
				
				Var pk = doWhileQSqlQueryNext._declareNew(foreignCls.getPkType(), "pk"+r.getAlias(),pkArgs);
				
				
//				IfBlock ifNotContains = 
						doWhileQSqlQueryNext._if(Expressions.not(pkSet.callMethod(ClsHashSet.contains, pk)))
						.addIfInstr(pkSet.callMethodInstruction(ClsHashSet.add, pk))
						.addIfInstr(b1.callMethodInstruction(BeanCls.getRelatedBeanMethodName(r), Types.BeanRepository.callStaticMethod(MethodGetFromResultSet.getMethodName(Beans.get(r.getDestTable())),  resultSet, JavaString.stringConstant(r.getAlias()))));
//						.addIfInstr(b1.accessAttr(CodeUtil2.plural(r.getDestTable().getCamelCaseName())).callMethodInstruction("append",  _this().callGetByRecordMethod(foreignCls, rec, JavaString.fromStringConstant(r.getAlias()))));
				
			} else {
				Column colPk = r.getDestTable().getPrimaryKey().getColumns().get(0);
				Type type = BeanCls.getTypeMapper().columnToType(colPk);

				//IfBlock ifNotRecValueIsNull = doWhileQSqlQueryNext._if(Expressions.not(  Types.BeanRepository.callStaticMethod(MethodGetFromResultSet.getMethodName(Beans.get(r.getDestTable())),  resultSet, JavaString.fromStringConstant("pk"+r.getAlias()))));
				
				Var pkSet = ifQSqlQueryNext.thenBlock()._declareInitDefaultConstructor(Types.hashset(type), "pkSet"+r.getAlias());
				/*Var pk = ifNotRecValueIsNull.getIfInstr()._declare(
						type, 
						"pk"+r.getAlias(), Types.BeanRepository.callStaticMethod(MethodGetFromResultSet.getMethodName(Beans.get(r.getDestTable())),  resultSet, JavaString.fromStringConstant("pk"+r.getAlias())))
						
						;*/
				
				Expression resultSetValueGetter = BeanCls.getTypeMapper().getResultSetValueGetter(resultSet, r.getDestTable().getPrimaryKey().getFirstColumn(),JavaString.stringConstant(r.getAlias()));
				Var pk = doWhileQSqlQueryNext._declare(
						type, 
						"pk"+r.getAlias(), 
						resultSetValueGetter);
				
				IfBlock ifNotRecValueIsNull = doWhileQSqlQueryNext._if(_not(resultSet.callMethod(ClsResultSet.wasNull)));
				ifNotRecValueIsNull.thenBlock()._if(Expressions.not(pkSet.callMethod(ClsHashSet.contains, pk)))
					.addIfInstr(pkSet.callMethodInstruction(ClsHashSet.add, pk))
					.addIfInstr(b1.callMethodInstruction(BeanCls.getRelatedBeanMethodName(r), Types.BeanRepository.callStaticMethod(MethodGetFromResultSet.getMethodName(Beans.get(r.getDestTable())),  resultSet, JavaString.stringConstant(r.getAlias()))));
			}
			
		}
		
		ifQSqlQueryNext.addIfInstr(doWhileQSqlQueryNext);
		doWhileQSqlQueryNext.setCondition(ifQSqlQueryNext.getCondition());
		_return(b1);
	}
	@Override
	public boolean includeIfEmpty() {
		return true;
	}
}
