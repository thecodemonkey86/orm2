package sunjava.entityrepository.method;

import java.util.ArrayList;
import java.util.List;

import codegen.CodeUtil;
import database.column.Column;
import database.relation.AbstractRelation;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import sunjava.config.JavaOrmOutputConfig;
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
import sunjava.entity.Entities;
import sunjava.entity.EntityCls;
import sunjava.entity.method.MethodOneRelationAttrSetter;
import sunjava.entity.method.MethodOneRelationEntityIsNull;
import sunjava.entityrepository.ClsEntityRepository;
import sunjava.lib.ClsHashSet;
import sunjava.lib.ClsResultSet;
import sunjava.lib.ClsSqlQuery;
import util.CodeUtil2;
import util.pg.PgCppUtil;

public class MethodGetById extends Method {

	protected EntityCls entity;
	Param pSqlCon;
	
	public MethodGetById(EntityCls cls) {
//		super(Public, cls, "getById");
		super(Public, cls, "get"+cls.getName()+"ById");
		for(Column col:cls.getTbl().getPrimaryKey().getColumns()) {
			Type colType = EntityCls.getTypeMapper().columnToType(  col);
			addParam(new Param(colType.isPrimitiveType() ? colType : colType, col.getCamelCaseName()));
			
		}
		if(JavaOrmOutputConfig.isAndroid()) {
			
		} else {
			pSqlCon =addParam(new Param(Types.Connection, "sqlConnection"));
			addThrowsException(Types.SqlException);
		}
		
		setStatic(true);
		
		this.entity=cls;
	}

//	@Override
//	public ThisEntityRepositoryExpression _this() {
//		return new ThisEntityRepositoryExpression((ClsEntityRepository) parent);
//	}
	
	@Override
	public void addImplementation() {
		//		StringBuilder sbSql = new StringBuilder(CodeUtil.sp("select");
		//ClsEntityRepository parent = (ClsEntityRepository) this.parent;
		
		List<OneRelation> oneRelations = entity.getOneRelations();
		List<OneToManyRelation> oneToManyRelations = entity.getOneToManyRelations();
		List<ManyRelation> manyToManyRelations = entity.getManyToManyRelations();
		
		Expression sqlCon = pSqlCon;// parent.callStaticMethod(MethodGetSqlCon.getMethodName());
		//Method mBuildQuery = aSqlCon.getClassType().getMethod("buildQuery");
		Var sqlQuery = _declare(Types.SqlQuery, "query",EntityCls.getSqlQueryCls().newInstance(sqlCon));
		
		ArrayList<Expression> selectFields = new ArrayList<>();
		selectFields.add(Types.EntityRepository.callStaticMethod("getSelectFields"+entity.getName(), JavaString.stringConstant("e1")));
		
		List<AbstractRelation> allRelations = new ArrayList<>(oneRelations.size()+oneToManyRelations.size()+manyToManyRelations.size());
		allRelations.addAll(oneRelations);
		allRelations.addAll(oneToManyRelations);
		allRelations.addAll(manyToManyRelations);
		
		for(AbstractRelation r:allRelations) {
			selectFields.add(Types.EntityRepository.callStaticMethod("getSelectFields"+Entities.get(r.getDestTable()).getName(), JavaString.stringConstant(r.getAlias())));
		}
		
		Expression exprSqlQuery = sqlQuery.callMethod("select", Expressions.concat(CharExpression.fromChar(','), selectFields) )
									.callMethod("from", Types.EntityRepository.callStaticMethod(ClsEntityRepository.getMethodNameGetTableName(entity),JavaString.stringConstant("e1")));
		
		for(OneRelation r:oneRelations) {
			ArrayList<String> joinConditions=new ArrayList<>();
			for(int i=0;i<r.getColumnCount();i++) {
				joinConditions.add(CodeUtil.sp("e1."+r.getColumns(i).getValue1().getEscapedName(),'=',(r.getAlias())+"."+ r.getColumns(i).getValue2().getEscapedName()));
			}
			
			exprSqlQuery = exprSqlQuery.callMethod("leftJoin", Types.EntityRepository.callStaticMethod(ClsEntityRepository.getMethodNameGetTableName(Entities.get(r.getDestTable()))),JavaString.stringConstant(r.getAlias()), JavaString.stringConstant(CodeUtil2.concat(joinConditions," AND ")));
		}
		for(OneToManyRelation r:oneToManyRelations) {
			ArrayList<String> joinConditions=new ArrayList<>();
			for(int i=0;i<r.getColumnCount();i++) {
				joinConditions.add(CodeUtil.sp("e1."+r.getColumns(i).getValue1().getEscapedName(),'=',(r.getAlias())+"."+ r.getColumns(i).getValue2().getEscapedName()));
			}
			
			exprSqlQuery = exprSqlQuery.callMethod("leftJoin", Types.EntityRepository.callStaticMethod(ClsEntityRepository.getMethodNameGetTableName(Entities.get(r.getDestTable()))),JavaString.stringConstant(r.getAlias()), JavaString.stringConstant(CodeUtil2.concat(joinConditions," AND ")));
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
			
			exprSqlQuery = exprSqlQuery.callMethod("leftJoin", Types.EntityRepository.callStaticMethod(ClsEntityRepository.getMethodNameGetTableName(Entities.get(r.getDestTable())),JavaString.stringConstant(r.getAlias())), JavaString.stringConstant(CodeUtil2.concat(joinConditions," AND ")));
			
			//bCount++;
		}

		
		for(Column col:entity.getTbl().getPrimaryKey().getColumns()) {
			exprSqlQuery = exprSqlQuery.callMethod("where", JavaString.stringConstant("e1."+ col.getEscapedName()+"=?"),getParam(col.getCamelCaseName()));
					
		}
		exprSqlQuery = exprSqlQuery.callMethod(ClsSqlQuery.query);
		Var resultSet = _declare(exprSqlQuery.getType(),
				"resultSet", exprSqlQuery
				);
		Var e1 = _declare(returnType, "e1", Expressions.Null);
		IfBlock ifQSqlQueryNext =
				_if(resultSet.callMethod(ClsResultSet.METHOD_NAME_NEXT))
				
//		for(Column col:entity.getTbl().getPrimaryKey().getColumns()) {		
//		OrmUtil.addAssignValueFromResultSetInstructions(resultSet, ifQSqlQueryNext.getIfInstr(), e1, col, "e1");
//		}
					.setIfInstr(
							e1.assign(Types.EntityRepository.callStaticMethod(MethodGetFromResultSet.getMethodName(entity),  resultSet, JavaString.stringConstant("e1")))
							,
							e1.callAttrSetterMethodInstr("loaded", BoolExpression.TRUE)//_assignInstruction(e1.accessAttr("loaded"), BoolExpression.TRUE)
							)
							;
		
		if(entity.hasRelations()) {
		DoWhile doWhileQSqlQueryNext = DoWhile.create();
		
		
		ArrayList<AbstractRelation> manyRelations = new ArrayList<>(oneToManyRelations.size()+manyToManyRelations.size());
		manyRelations.addAll(oneToManyRelations);
		manyRelations.addAll(manyToManyRelations);
		
		for(OneRelation r:oneRelations) {
//			EntityCls foreignCls = Entities.get(r.getDestTable()); 
			IfBlock ifBlock= doWhileQSqlQueryNext._if(e1.callMethod(new MethodOneRelationEntityIsNull(r)));
			ifBlock.thenBlock().
			_callMethodInstr(e1, new MethodOneRelationAttrSetter( e1.getClassConcreteType().getAttrByName(PgCppUtil.getOneRelationDestAttrName(r)), true,r.isPartOfPk()), 
					Types.EntityRepository.callStaticMethod(MethodGetFromResultSet.getMethodName(Entities.get(r.getDestTable())),  resultSet, JavaString.stringConstant(r.getAlias())));
		}
		for(AbstractRelation r:manyRelations) {
			EntityCls foreignCls = Entities.get(r.getDestTable());  
			if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
				Var pkSet = ifQSqlQueryNext.thenBlock()._declareInitDefaultConstructor(Types.hashset(foreignCls.getPkType()), "pkSet"+r.getAlias());
				
				Expression[] pkArgs = new Expression[ r.getDestTable().getPrimaryKey().getColumnCount()];
				for(int i = 0; i < pkArgs.length; i++) {
					Expression resultSetValueGetter = EntityCls.getTypeMapper().getResultSetValueGetter(resultSet, r.getDestTable().getPrimaryKey().getColumn(i),JavaString.stringConstant(r.getAlias()));
					pkArgs[i] = resultSetValueGetter;
				}
				
				Var pk = doWhileQSqlQueryNext._declareNew(foreignCls.getPkType(), "pk"+r.getAlias(),pkArgs);
				
				
//				IfBlock ifNotContains = 
						doWhileQSqlQueryNext._if(Expressions.not(pkSet.callMethod(ClsHashSet.contains, pk)))
						.addIfInstr(pkSet.callMethodInstruction(ClsHashSet.add, pk))
						.addIfInstr(e1.callMethodInstruction(EntityCls.getRelatedEntityMethodName(r), Types.EntityRepository.callStaticMethod(MethodGetFromResultSet.getMethodName(Entities.get(r.getDestTable())),  resultSet, JavaString.stringConstant(r.getAlias()))));
//						.addIfInstr(e1.accessAttr(CodeUtil2.plural(r.getDestTable().getCamelCaseName())).callMethodInstruction("append",  _this().callGetByRecordMethod(foreignCls, rec, JavaString.fromStringConstant(r.getAlias()))));
				
			} else {
				Column colPk = r.getDestTable().getPrimaryKey().getColumns().get(0);
				Type type = EntityCls.getTypeMapper().columnToType(colPk);

				//IfBlock ifNotRecValueIsNull = doWhileQSqlQueryNext._if(Expressions.not(  Types.EntityRepository.callStaticMethod(MethodGetFromResultSet.getMethodName(Entities.get(r.getDestTable())),  resultSet, JavaString.fromStringConstant("pk"+r.getAlias()))));
				
				Var pkSet = ifQSqlQueryNext.thenBlock()._declareInitDefaultConstructor(Types.hashset(type), "pkSet"+r.getAlias());
				/*Var pk = ifNotRecValueIsNull.getIfInstr()._declare(
						type, 
						"pk"+r.getAlias(), Types.EntityRepository.callStaticMethod(MethodGetFromResultSet.getMethodName(Entities.get(r.getDestTable())),  resultSet, JavaString.fromStringConstant("pk"+r.getAlias())))
						
						;*/
				
				Expression resultSetValueGetter = EntityCls.getTypeMapper().getResultSetValueGetter(resultSet, r.getDestTable().getPrimaryKey().getFirstColumn(),JavaString.stringConstant(r.getAlias()));
				Var pk = doWhileQSqlQueryNext._declare(
						type, 
						"pk"+r.getAlias(), 
						resultSetValueGetter);
				
				IfBlock ifNotRecValueIsNull = doWhileQSqlQueryNext._if(_not(resultSet.callMethod(ClsResultSet.wasNull)));
				ifNotRecValueIsNull.thenBlock()._if(Expressions.not(pkSet.callMethod(ClsHashSet.contains, pk)))
					.addIfInstr(pkSet.callMethodInstruction(ClsHashSet.add, pk))
					.addIfInstr(e1.callMethodInstruction(EntityCls.getRelatedEntityMethodName(r), Types.EntityRepository.callStaticMethod(MethodGetFromResultSet.getMethodName(Entities.get(r.getDestTable())),  resultSet, JavaString.stringConstant(r.getAlias()))));
			}
			
		}
		
		ifQSqlQueryNext.addIfInstr(doWhileQSqlQueryNext);
		doWhileQSqlQueryNext.setCondition(ifQSqlQueryNext.getCondition());
		}
		_return(e1);
	}

}
