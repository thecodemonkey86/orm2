package sunjava.beanrepository.method;

import java.util.ArrayList;
import java.util.List;

import codegen.CodeUtil;
import database.column.Column;
import database.relation.AbstractRelation;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import sunjava.bean.BeanCls;
import sunjava.bean.Beans;
import sunjava.bean.method.MethodOneRelationAttrSetter;
import sunjava.bean.method.MethodOneRelationBeanIsNull;
import sunjava.beanrepository.ClsBeanRepository;
import sunjava.core.Attr;
import sunjava.core.JavaString;
import sunjava.core.Method;
import sunjava.core.Param;
import sunjava.core.Type;
import sunjava.core.Types;
import sunjava.core.expression.BoolExpression;
import sunjava.core.expression.CharExpression;
import sunjava.core.expression.Expression;
import sunjava.core.expression.Expressions;
import sunjava.core.expression.Var;
import sunjava.core.instruction.DoWhile;
import sunjava.core.instruction.IfBlock;
import sunjava.lib.ClsHashSet;
import sunjava.lib.ClsResultSet;
import sunjava.lib.ClsSqlQuery;
import util.CodeUtil2;
import util.pg.PgCppUtil;

public class MethodGetById extends Method {

	protected BeanCls bean;
	public MethodGetById(BeanCls cls) {
//		super(Public, cls, "getById");
		super(Public, cls, "get"+cls.getName()+"ById");
		for(Column col:cls.getTbl().getPrimaryKey().getColumns()) {
			Type colType = BeanCls.getTypeMapper().columnToType(  col);
			addParam(new Param(colType.isPrimitiveType() ? colType : colType, col.getCamelCaseName()));
			
		}
		setStatic(true);
		addThrowsException(Types.SqlException);
		this.bean=cls;
	}

//	@Override
//	public ThisBeanRepositoryExpression _this() {
//		return new ThisBeanRepositoryExpression((ClsBeanRepository) parent);
//	}
	
	@Override
	public void addImplementation() {
		//		StringBuilder sbSql = new StringBuilder(CodeUtil.sp("select");
		ClsBeanRepository parent = (ClsBeanRepository) this.parent;
		
		List<OneRelation> oneRelations = bean.getOneRelations();
		List<OneToManyRelation> oneToManyRelations = bean.getOneToManyRelations();
		List<ManyRelation> manyToManyRelations = bean.getManyToManyRelations();
		
		Attr aSqlCon = parent.getAttrByName("sqlCon");
		//Method mBuildQuery = aSqlCon.getClassType().getMethod("buildQuery");
		Var sqlQuery = _declare(Types.SqlQuery, "query",BeanCls.getSqlQueryCls().newInstance(aSqlCon));
		
		ArrayList<Expression> selectFields = new ArrayList<>();
		selectFields.add(Types.BeanRepository.callStaticMethod("getSelectFields"+bean.getName(), JavaString.stringConstant("e1")));
		
		List<AbstractRelation> allRelations = new ArrayList<>(oneRelations.size()+oneToManyRelations.size()+manyToManyRelations.size());
		allRelations.addAll(oneRelations);
		allRelations.addAll(oneToManyRelations);
		allRelations.addAll(manyToManyRelations);
		
		for(AbstractRelation r:allRelations) {
			selectFields.add(Types.BeanRepository.callStaticMethod("getSelectFields"+Beans.get(r.getDestTable()).getName(), JavaString.stringConstant(r.getAlias())));
		}
		
		Expression exprSqlQuery = sqlQuery.callMethod("select", Expressions.concat(CharExpression.fromChar(','), selectFields) )
									.callMethod("from", Types.BeanRepository.callStaticMethod(ClsBeanRepository.getMethodNameGetTableName(bean),JavaString.stringConstant("e1")));
		
		for(OneRelation r:oneRelations) {
			ArrayList<String> joinConditions=new ArrayList<>();
			for(int i=0;i<r.getColumnCount();i++) {
				joinConditions.add(CodeUtil.sp("e1."+r.getColumns(i).getValue1().getEscapedName(),'=',(r.getAlias())+"."+ r.getColumns(i).getValue2().getEscapedName()));
			}
			
			exprSqlQuery = exprSqlQuery.callMethod("leftJoin", Types.BeanRepository.callStaticMethod(ClsBeanRepository.getMethodNameGetTableName(Beans.get(r.getDestTable()))),JavaString.stringConstant(r.getAlias()), JavaString.stringConstant(CodeUtil2.concat(joinConditions," AND ")));
		}
		for(OneToManyRelation r:oneToManyRelations) {
			ArrayList<String> joinConditions=new ArrayList<>();
			for(int i=0;i<r.getColumnCount();i++) {
				joinConditions.add(CodeUtil.sp("e1."+r.getColumns(i).getValue1().getEscapedName(),'=',(r.getAlias())+"."+ r.getColumns(i).getValue2().getEscapedName()));
			}
			
			exprSqlQuery = exprSqlQuery.callMethod("leftJoin", Types.BeanRepository.callStaticMethod(ClsBeanRepository.getMethodNameGetTableName(Beans.get(r.getDestTable()))),JavaString.stringConstant(r.getAlias()), JavaString.stringConstant(CodeUtil2.concat(joinConditions," AND ")));
		}
		for(ManyRelation r:manyToManyRelations) {
			ArrayList<String> joinConditions=new ArrayList<>();
			for(int i=0;i<r.getSourceColumnCount();i++) {
				joinConditions.add(CodeUtil.sp("e1."+r.getSourceEntityColumn(i).getEscapedName(),'=',r.getAlias("mapping")+"."+ r.getSourceMappingColumn(i).getEscapedName()));
			}
			
			exprSqlQuery = exprSqlQuery.callMethod("leftJoin", JavaString.stringConstant(r.getMappingTable().getName()),JavaString.stringConstant(r.getAlias("mapping")), JavaString.stringConstant(CodeUtil2.concat(joinConditions," AND ")));
			
			joinConditions.clear();
			for(int i=0;i<r.getDestColumnCount();i++) {
				joinConditions.add(CodeUtil.sp(r.getAlias("mapping")+"."+r.getDestMappingColumn(i).getEscapedName(),'=',r.getAlias()+"."+r.getDestEntityColumn(i).getEscapedName() ));
			}
			
			exprSqlQuery = exprSqlQuery.callMethod("leftJoin", Types.BeanRepository.callStaticMethod(ClsBeanRepository.getMethodNameGetTableName(Beans.get(r.getDestTable())),JavaString.stringConstant(r.getAlias())), JavaString.stringConstant(CodeUtil2.concat(joinConditions," AND ")));
			
			//bCount++;
		}

		
		for(Column col:bean.getTbl().getPrimaryKey().getColumns()) {
			exprSqlQuery = exprSqlQuery.callMethod("where", JavaString.stringConstant("e1."+ col.getEscapedName()+"=?"),getParam(col.getCamelCaseName()));
					
		}
		exprSqlQuery = exprSqlQuery.callMethod(ClsSqlQuery.query);
		Var resultSet = _declare(exprSqlQuery.getType(),
				"resultSet", exprSqlQuery
				);
		Var e1 = _declare(returnType, "e1", Expressions.Null);
		IfBlock ifQSqlQueryNext =
				_if(resultSet.callMethod(ClsResultSet.METHOD_NAME_NEXT))
				
//		for(Column col:bean.getTbl().getPrimaryKey().getColumns()) {		
//		OrmUtil.addAssignValueFromResultSetInstructions(resultSet, ifQSqlQueryNext.getIfInstr(), e1, col, "e1");
//		}
					.setIfInstr(
							e1.assign(Types.BeanRepository.callStaticMethod(MethodGetFromResultSet.getMethodName(bean),  resultSet, JavaString.stringConstant("e1")))
							,
							e1.callAttrSetterMethodInstr("loaded", BoolExpression.TRUE)//_assignInstruction(e1.accessAttr("loaded"), BoolExpression.TRUE)
							)
							;
		
		
		DoWhile doWhileQSqlQueryNext = DoWhile.create();
		
		
		ArrayList<AbstractRelation> manyRelations = new ArrayList<>(oneToManyRelations.size()+manyToManyRelations.size());
		manyRelations.addAll(oneToManyRelations);
		manyRelations.addAll(manyToManyRelations);
		
		for(OneRelation r:oneRelations) {
//			BeanCls foreignCls = Beans.get(r.getDestTable()); 
			IfBlock ifBlock= doWhileQSqlQueryNext._if(e1.callMethod(new MethodOneRelationBeanIsNull(r)));
			ifBlock.thenBlock().
			_callMethodInstr(e1, new MethodOneRelationAttrSetter( e1.getClassConcreteType().getAttrByName(PgCppUtil.getOneRelationDestAttrName(r)), true,r.isPartOfPk()), 
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
						.addIfInstr(e1.callMethodInstruction(BeanCls.getRelatedBeanMethodName(r), Types.BeanRepository.callStaticMethod(MethodGetFromResultSet.getMethodName(Beans.get(r.getDestTable())),  resultSet, JavaString.stringConstant(r.getAlias()))));
//						.addIfInstr(e1.accessAttr(CodeUtil2.plural(r.getDestTable().getCamelCaseName())).callMethodInstruction("append",  _this().callGetByRecordMethod(foreignCls, rec, JavaString.fromStringConstant(r.getAlias()))));
				
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
					.addIfInstr(e1.callMethodInstruction(BeanCls.getRelatedBeanMethodName(r), Types.BeanRepository.callStaticMethod(MethodGetFromResultSet.getMethodName(Beans.get(r.getDestTable())),  resultSet, JavaString.stringConstant(r.getAlias()))));
			}
			
		}
		
		ifQSqlQueryNext.addIfInstr(doWhileQSqlQueryNext);
		doWhileQSqlQueryNext.setCondition(ifQSqlQueryNext.getCondition());
		_return(e1);
	}

}
