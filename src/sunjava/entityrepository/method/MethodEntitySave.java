package sunjava.entityrepository.method;

import java.util.ArrayList;
import java.util.List;

import database.column.Column;
import database.relation.ManyRelation;
import database.table.Table;
import sunjava.core.Attr;
import sunjava.core.JavaString;
import sunjava.core.Method;
import sunjava.core.MethodCall;
import sunjava.core.Param;
import sunjava.core.Type;
import sunjava.core.Types;
import sunjava.core.expression.Expression;
import sunjava.core.expression.IntExpression;
import sunjava.core.expression.Var;
import sunjava.core.instruction.ForeachLoop;
import sunjava.core.instruction.IfBlock;
import sunjava.core.instruction.InstructionBlock;
import sunjava.entity.EntityCls;
import sunjava.entity.method.MethodHasAddedManyToMany;
import sunjava.entity.method.MethodHasRemovedManyToMany;
import sunjava.entityrepository.ClsEntityRepository;
import sunjava.lib.ClsArrayList;
import sunjava.lib.ClsBaseEntity;
import sunjava.lib.ClsJavaString;
import sunjava.lib.ClsResultSet;
import sunjava.lib.ClsSqlParam;
import sunjava.lib.ClsSqlQuery;
import sunjava.lib.ClsSqlUtil;
import sunjava.orm.OrmUtil;
import util.CodeUtil2;

public class MethodEntitySave extends Method {
	protected EntityCls entity;
	Param pSqlCon;
	public MethodEntitySave(EntityCls entity) {
		super(Public, Types.Void, "save");
		setStatic(true);
		this.entity = entity;
		addParam(new Param(entity, "entity"));
		pSqlCon =addParam(new Param(Types.Connection, "sqlConnection"));
	}

	@Override
	public void addImplementation() {
		addThrowsException(Types.SqlException);
		ClsEntityRepository parent = (ClsEntityRepository) this.parent;
		Var sqlQuery = _declare(Types.SqlQuery, "query",EntityCls.getSqlQueryCls().newInstance(pSqlCon));
//		TryCatchBlock tryCatch = _tryCatch();
		
		InstructionBlock mainBlock = this; //tryCatch.getTryBlock()
		
		Param pBean = getParam("entity");
	
		IfBlock ifIsInsertNew = mainBlock._if(pBean.callMethod(ClsBaseEntity.METHOD_NAME_IS_INSERT_NEW));
//		ifIsInsertNew.thenBlock()._callMethodInstr(sqlQuery, ClsSqlQuery.METHOD_NAME_BEGIN_TRANSACTION);
		
		ifIsInsertNew.thenBlock()
		
			._callMethodInstr(sqlQuery,  ClsSqlQuery.insertInto,parent.callStaticMethod(ClsEntityRepository.getMethodNameGetTableName(entity)));
		
		IfBlock ifHasUpdate = ifIsInsertNew.elseBlock()._if(pBean.callMethod(ClsBaseEntity.METHOD_NAME_HAS_UPDATE));
		
		
//		ifHasUpdate.thenBlock()._callMethodInstr(sqlQuery, ClsSqlQuery.METHOD_NAME_BEGIN_TRANSACTION);
		ifHasUpdate.thenBlock()._callMethodInstr(sqlQuery,  ClsSqlQuery.update,parent.callStaticMethod(ClsEntityRepository.getMethodNameGetTableName(entity)));
		
		
		
		
		Table tbl = entity.getTbl();
		List<Column> columns = tbl.getColumns(!tbl.getPrimaryKey().isAutoIncrement());
		for(Column col : columns) {
			if (col.isNullable()) {
				// begin insert
				IfBlock ifColIsNull = ifIsInsertNew.thenBlock()._if(EntityCls.accessColumnAttrOrEntity(pBean,col).isNull());
				
				ifColIsNull.thenBlock()._callMethodInstr(sqlQuery, 
						ClsSqlQuery.setValue,JavaString.stringConstant(col.getName()), 
						Types.SqlParam.callStaticMethod(ClsSqlParam.getNullMethodName(EntityCls.getTypeMapper().columnToType(col))));
				
				ifColIsNull.elseBlock()._callMethodInstr(sqlQuery, 
						ClsSqlQuery.setValue,JavaString.stringConstant(col.getName()), 
						Types.SqlParam.callStaticMethod(ClsSqlParam.get, EntityCls.accessAttrGetterByColumn(pBean,col,false)));
				// end insert
				
				// begin update		
				if (!col.isPartOfPk()) {

					IfBlock ifModified = ifHasUpdate.thenBlock()._if(EntityCls.accessIsColumnAttrOrEntityModified(pBean, col));
					IfBlock ifColIsNullElse = ifModified.thenBlock()._if(EntityCls.accessColumnAttrOrEntity(pBean,col).isNull());
					
					ifColIsNullElse.thenBlock()._callMethodInstr(sqlQuery, 
							ClsSqlQuery.setValue,JavaString.stringConstant(col.getName()), 
							Types.SqlParam.callStaticMethod(ClsSqlParam.getNullMethodName(EntityCls.getTypeMapper().columnToType(col))));
					
					ifColIsNullElse.elseBlock()._callMethodInstr(sqlQuery, 
							ClsSqlQuery.setValue,JavaString.stringConstant(col.getName()), 
							Types.SqlParam.callStaticMethod(ClsSqlParam.get, EntityCls.accessAttrGetterByColumn(pBean,col,false)));
				}
				// end update
			} else {
				ifIsInsertNew.thenBlock()._callMethodInstr(sqlQuery,  ClsSqlQuery.setValue,JavaString.stringConstant(col.getName()), Types.SqlParam.callStaticMethod(ClsSqlParam.get, EntityCls.accessAttrGetterByColumn(pBean,col,true)));
				
				if (!col.isPartOfPk()) {
					IfBlock ifModified = ifHasUpdate.thenBlock()._if(EntityCls.accessIsColumnAttrOrEntityModified(pBean, col));
					ifModified.thenBlock()._callMethodInstr(sqlQuery, 
							ClsSqlQuery.setValue,JavaString.stringConstant(col.getName()), 
							Types.SqlParam.callStaticMethod(ClsSqlParam.get, EntityCls.accessAttrGetterByColumn(pBean,col,false)));
				
				}
			}
			
		}
		
		IfBlock ifNotIsPkModified = ifHasUpdate.thenBlock()._if(_not(pBean.callMethod(ClsBaseEntity.METHOD_NAME_IS_PRIMARY_KEY_MODIFIED)));
		
		
		for (Column colPk : tbl.getPrimaryKey().getColumns()) {
			ifNotIsPkModified.thenBlock()._callMethodInstr(sqlQuery,  ClsSqlQuery.where,JavaString.stringConstant(colPk.getName()+"=?"), Types.SqlParam.callStaticMethod(ClsSqlParam.get, EntityCls.accessAttrGetterByColumn(pBean,colPk,true)));
			
			
			ifNotIsPkModified.elseBlock()._callMethodInstr(sqlQuery, 
					ClsSqlQuery.setValue,JavaString.stringConstant(colPk.getName()), 
					Types.SqlParam.callStaticMethod(ClsSqlParam.get, EntityCls.accessAttrGetterByColumn(pBean,colPk,true)));
			ifNotIsPkModified.elseBlock()._callMethodInstr(sqlQuery,  ClsSqlQuery.where,JavaString.stringConstant(colPk.getName()+"=?"), Types.SqlParam.callStaticMethod(ClsSqlParam.get, EntityCls.accessPrimaryKeyPreviousAttrGetterByColumn(pBean,colPk)));
		}
		/*Var catchSqlException = new Var(Types.SqlException, "exception");
		tryCatch.addCatch(catchSqlException, sqlQuery.callMethodInstruction(ClsSqlQuery.METHOD_NAME_ROLLBACK_TRANSACTION),
				new ThrowInstruction(catchSqlException)
				);
		*/
		
		/*if (entity.isAutoIncrement()) {
				ResultSet rsAutoIncrement = query.executeAndGetGeneratedKeys();
				if(rsAutoIncrement.next()) {
					entity.setAutoIncrementId(rsAutoIncrement.getInt(""));
				}
			} else {
				query.execute();
			}*/
		
		//
		if(tbl.isAutoIncrement()) {
			Var rsAutoIncrement = ifIsInsertNew.thenBlock()._declare(Types.ResultSet, "rsAutoIncrement", sqlQuery.callMethod(ClsSqlQuery.executeAndGetGeneratedKeys));
			IfBlock ifRsAutoIncrementNext = ifIsInsertNew.thenBlock()._if(rsAutoIncrement.callMethod(ClsResultSet.METHOD_NAME_NEXT));
			ifRsAutoIncrementNext.thenBlock()._callMethodInstr(pBean, ClsBaseEntity.METHOD_NAME_SET_AUTO_INCREMENT_ID, rsAutoIncrement.callMethod(ClsResultSet.METHOD_NAME_GET_INT, JavaString.stringConstant(tbl.getPrimaryKey().getFirstColumn().getName())));
		
		} else {
			ifIsInsertNew.thenBlock()._callMethodInstr(sqlQuery, ClsSqlQuery.execute);
		}
		ifHasUpdate.thenBlock()._callMethodInstr(sqlQuery, ClsSqlQuery.execute);


		
		List<ManyRelation> manyRelations = entity.getManyToManyRelations();

		
		for(ManyRelation r:manyRelations) {
			if (r.getDestColumnCount()==0) {
				throw new RuntimeException();
			}
			ArrayList<String> columnsIn=new ArrayList<>();
			for(int i=0;i<r.getDestColumnCount();i++) {
				columnsIn.add( r.getDestMappingColumn(i).getEscapedName() );
			}
			
			// "removed"
			MethodCall expressionManyToManyRemoved = pBean.callAttrGetter(entity.getAttrByName(OrmUtil.getManyRelationDestAttrName(r)+"Removed" ));
			IfBlock ifRemoveBeans = _if(pBean.callMethod(MethodHasRemovedManyToMany.getMethodName(r)));
		
			
			Var varDeleteSql = ifRemoveBeans.thenBlock()._declare(Types.SqlQuery, "deleteSqlQuery",EntityCls.getSqlQueryCls().newInstance(pSqlCon));
			ifRemoveBeans.thenBlock()._callMethodInstr(varDeleteSql, ClsSqlQuery.deleteFrom, JavaString.stringConstant(r.getMappingTable().getName()));
			
			ArrayList<String> whereSourceCols = new ArrayList<>(r.getSourceColumnCount());
			Expression[] whereSourceColsParams = new Expression[r.getSourceColumnCount()+1];
			
			for(int i=0;i<r.getSourceColumnCount();i++) {
				whereSourceCols.add(r.getSourceMappingColumn(i).getEscapedName());
				whereSourceColsParams[i+1] =Types.SqlParam.callStaticMethod(ClsSqlParam.get,pBean.callAttrGetter(r.getSourceEntityColumn(i).getCamelCaseName())); 
				//ifRemoveBeans.thenBlock()._callMethodInstr(varDeleteSql, ClsSqlQuery.where, JavaString.stringConstant(r.getSourceMappingColumn(i).getEscapedName()+" = ?"),Types.SqlParam.callStaticMethod(ClsSqlParam.get,pBean.callAttrGetter(r.getSourceEntityColumn(i).getCamelCaseName())));
			
			}
			whereSourceColsParams[0] = whereSourceCols.size() > 1 ? JavaString.stringConstant("("+ CodeUtil2.commaSep(whereSourceCols)+") = (" + CodeUtil2.strMultiply("?", ",", whereSourceCols.size())+ ")") :  JavaString.stringConstant( whereSourceCols.get(0)+ " = ?");
			ifRemoveBeans.thenBlock()._callMethodInstr(varDeleteSql, ClsSqlQuery.where, whereSourceColsParams);
			
			
			//
			//
			
			IfBlock ifCountEq1 = ifRemoveBeans.thenBlock()
					._if(expressionManyToManyRemoved.callMethod(ClsArrayList.size).equalsOp(new IntExpression(1)));
			
			Expression[] whereDestColsParams = new Expression[r.getDestColumnCount()+1];
			
			
			
			if(r.getDestColumnCount() == 1) {
				whereDestColsParams[0] =JavaString.stringConstant( (columnsIn.size()>1? CodeUtil2.parentheses(CodeUtil2.concat(columnsIn, ",")):columnsIn.get(0))+" = ?");
				for(int i= 0;i<r.getDestColumnCount(); i++) {
					whereDestColsParams[i+1] = Types.SqlParam.callStaticMethod(ClsSqlParam.get, expressionManyToManyRemoved.callMethod(ClsArrayList.get, new IntExpression(0)));
				}
				
				ifCountEq1.elseBlock()._callMethodInstr(varDeleteSql, ClsSqlQuery.where, Types.String.callStaticMethod(ClsJavaString.format, JavaString.stringConstant( (columnsIn.size()>1? CodeUtil2.parentheses(CodeUtil2.concat(columnsIn, ",")):columnsIn.get(0))+" in (%s)"),Types.SqlUtil.callStaticMethod(ClsSqlUtil.getPlaceholders, expressionManyToManyRemoved.callMethod(ClsArrayList.size))),expressionManyToManyRemoved.callMethod(ClsArrayList.get, new IntExpression(0)));
				ifCountEq1.thenBlock()._callMethodInstr(varDeleteSql, ClsSqlQuery.where, whereDestColsParams);
			} else {
				whereDestColsParams[0] = JavaString.stringConstant( (columnsIn.size()>1? CodeUtil2.parentheses(CodeUtil2.concat(columnsIn, ",")):columnsIn.get(0))+" = (" + CodeUtil2.strMultiply("?", ",",r.getDestColumnCount() )+")");
				for(int i= 0;i<r.getDestColumnCount(); i++) {
					whereDestColsParams[i+1] = Types.SqlParam.callStaticMethod(ClsSqlParam.get, expressionManyToManyRemoved.callMethod(ClsArrayList.get, new IntExpression(0)).callAttrGetter(r.getDestEntityColumn(i).getCamelCaseName()));
				}
				
				Type foreachRemoveElementType = ((ClsArrayList) expressionManyToManyRemoved.getType()).getElementType();
				Var varParamsForeachRemove = ifCountEq1.elseBlock()._declareNew( Types.arraylist(Types.SqlParam), "params",expressionManyToManyRemoved.callMethod(ClsArrayList.size).binOp("+", new IntExpression(1)));
				ForeachLoop foreachAttrRemove = ifCountEq1.elseBlock()._foreach(new Var(foreachRemoveElementType.isPrimitiveType()?foreachRemoveElementType: foreachRemoveElementType, "rem"), expressionManyToManyRemoved);
				
				for(int i= 0;i<r.getDestColumnCount(); i++) {
					foreachAttrRemove._callMethodInstr(varParamsForeachRemove, ClsArrayList.add, Types.SqlParam.callStaticMethod(ClsSqlParam.get, foreachAttrRemove.getVar().callAttrGetter(r.getDestEntityColumn(i).getCamelCaseName())));
				}
				ifCountEq1.elseBlock()._callMethodInstr(varDeleteSql, ClsSqlQuery.where, Types.String.callStaticMethod(ClsJavaString.format, JavaString.stringConstant( (columnsIn.size()>1? CodeUtil2.parentheses(CodeUtil2.concat(columnsIn, ",")):columnsIn.get(0))+" in (%s)"),Types.SqlUtil.callStaticMethod(ClsSqlUtil.getPlaceholdersMultipleRows, expressionManyToManyRemoved.callMethod(ClsArrayList.size), new IntExpression(r.getDestColumnCount() ))),varParamsForeachRemove);
				ifCountEq1.thenBlock()._callMethodInstr(varDeleteSql, ClsSqlQuery.where, whereDestColsParams);
			}
			// end of "removed"
			// "added"
			MethodCall expressionManyToManyAdded = pBean.callAttrGetter(entity.getAttrByName(OrmUtil.getManyRelationDestAttrName(r)+"Added" ));
			IfBlock ifAddedBeans = _if(pBean.callMethod(MethodHasAddedManyToMany.getMethodName(r)));
			
			Expression[] argsInsertMultiRow = new Expression[r.getMappingTable().getPrimaryKey().getColumnCount()+1];
			Expression[] argsOnConflictDoNothing = new Expression[r.getMappingTable().getPrimaryKey().getColumnCount()];
			argsInsertMultiRow[0] = JavaString.stringConstant(r.getMappingTable().getName());
			
			for(int i = 0; i < r.getMappingTable().getPrimaryKey().getColumnCount(); i++ ) {
				argsInsertMultiRow[i+1] = JavaString.stringConstant(r.getMappingTable().getPrimaryKey().getColumn(i).getName());
				argsOnConflictDoNothing[i] = JavaString.stringConstant(r.getMappingTable().getPrimaryKey().getColumn(i).getName());
			}
			
			Var varAddSql = ifAddedBeans.thenBlock()._declare(Types.SqlQuery, "addSqlQuery",EntityCls.getSqlQueryCls().newInstance(pSqlCon));
			ifAddedBeans.thenBlock()._callMethodInstr(varAddSql, ClsSqlQuery.insertMultiRow, argsInsertMultiRow);
			ifAddedBeans.thenBlock()._callMethodInstr(varAddSql, ClsSqlQuery.onConflictDoNothing, argsOnConflictDoNothing);
			Type foreachAddedElementType = ((ClsArrayList) expressionManyToManyAdded.getType()).getElementType();
			ForeachLoop foreachAttrAdd = ifAddedBeans.thenBlock()._foreach(new Var(foreachAddedElementType, "added"), expressionManyToManyAdded);
			Expression[] addInsertRowArgs  = new Expression[r.getSourceTable().getPrimaryKey().getColumnCount()+r.getDestTable().getPrimaryKey().getColumns().size()];
			
			for(int i = 0; i < r.getSourceTable().getPrimaryKey().getColumnCount(); i++) {
				Column col = r.getSourceTable().getPrimaryKey().getColumn(i); 
				addInsertRowArgs[i] = Types.SqlParam.callStaticMethod(ClsSqlParam.get, pBean.callAttrGetter(new Attr(EntityCls.getTypeMapper().columnToType(col), col.getCamelCaseName()))); 
			}
			
			if (r.getDestTable().getPrimaryKey().isMultiColumn()) {
				
				for(int i = 0; i < r.getDestTable().getPrimaryKey().getColumnCount(); i++) {
					Column colPk =  r.getDestTable().getPrimaryKey().getColumn(i);
					addInsertRowArgs[i+r.getSourceTable().getPrimaryKey().getColumnCount()] = Types.SqlParam.callStaticMethod(ClsSqlParam.get, foreachAttrAdd.getVar().callAttrGetter(colPk.getCamelCaseName()));
				}
			} else {
				
				addInsertRowArgs[r.getSourceTable().getPrimaryKey().getColumnCount()] = Types.SqlParam.callStaticMethod(ClsSqlParam.get, foreachAttrAdd.getVar());
				
			}
			
			foreachAttrAdd._callMethodInstr(varAddSql, ClsSqlQuery.addInsertRow, addInsertRowArgs);
			
			
			ifAddedBeans.thenBlock()._callMethodInstr(varAddSql, ClsSqlQuery.execute);
			ifRemoveBeans.thenBlock()._callMethodInstr(varDeleteSql, ClsSqlQuery.execute);
			/* example: if (!entity.getEntity1sAdded().isEmpty()){
			SqlQuery addSqlQuery  = new PgSqlQuery(sqlCon);
			addSqlQuery.insertMultiRow("e1_e3_mm", "e1_id", "e3_id");
			addSqlQuery.onConflictDoNothing("e1_id", "e3_id");
			
			for (Integer added : entity.getEntity1sAdded()){
				addSqlQuery.addInsertRow(SqlParam.get(added), SqlParam.get(entity.getId()));
				
			}
			
			addSqlQuery.execute();
		}*/
			
			/*if (entity.getTbl().getPrimaryKey().isMultiColumn()) {
				for(Column col:entity.getTbl().getPrimaryKey().getColumns()) {
					foreachAttrAdd.addInstr(varParamsForeachAdd.callMethodInstruction("append", _this().accessAttr(  col.getCamelCaseName())));
				}
			} else {
				foreachAttrAdd.addInstr(varParamsForeachAdd.callMethodInstruction("append", _this().accessAttr(  entity.getTbl().getPrimaryKey().getFirstColumn().getCamelCaseName())));
			}
			if (r.getDestTable().getPrimaryKey().isMultiColumn()) {
				for(Column col:r.getDestTable().getPrimaryKey().getColumns()) {
					foreachAttrAdd.addInstr(varParamsForeachAdd.callMethodInstruction("append", foreachAttrAdd.getVar().accessAttr(col.getCamelCaseName())));
				}
			} else {
				foreachAttrAdd.addInstr(varParamsForeachAdd.callMethodInstruction("append", foreachAttrAdd.getVar()));
			}*/
			
			// end of "added"
			

		}

	}

}
