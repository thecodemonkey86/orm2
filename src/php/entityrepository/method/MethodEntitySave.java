package php.entityrepository.method;

import java.util.ArrayList;
import java.util.List;

import database.column.Column;
import database.relation.ManyRelation;
import database.table.Table;
import php.core.Attr;
import php.core.Param;
import php.core.PhpArray;
import php.core.PhpFunctions;
import php.core.Type;
import php.core.Types;
import php.core.expression.ArrayInitExpression;
import php.core.expression.Expression;
import php.core.expression.Expressions;
import php.core.expression.IntExpression;
import php.core.expression.MethodCall;
import php.core.expression.PhpStringLiteral;
import php.core.expression.Var;
import php.core.instruction.ForeachLoop;
import php.core.instruction.IfBlock;
import php.core.instruction.InstructionBlock;
import php.core.method.Method;
import php.entity.EntityCls;
import php.entity.method.MethodClearModified;
import php.entity.method.MethodHasAddedManyToMany;
import php.entity.method.MethodHasRemovedManyToMany;
import php.entityrepository.ClsEntityRepository;
import php.lib.ClsBaseEntity;
import php.lib.ClsFirebirdSqlQuery;
import php.lib.ClsMysqli;
import php.lib.ClsSqlParam;
import php.lib.ClsSqlQuery;
import php.lib.ClsSqlUtil;
import php.orm.OrmUtil;
import util.CodeUtil2;

public class MethodEntitySave extends Method {
	protected EntityCls entity;
	Param pTransactionHandle ;
	public MethodEntitySave(EntityCls entity) {
		super(Public, Types.Void, getMethodName(entity));
		setStatic(true);
		this.entity = entity;
		addParam(new Param(entity, "entity"));
		if( EntityCls.getTypeMapper().hasTransactionHandle()) {
			pTransactionHandle = addParam(new Param(Types.Resource, "transactionHandle",Expressions.Null));
		
		}
	}

	public static String getMethodName(EntityCls entity) {
		return "save"+entity.getName();
	}

	@Override
	public void addImplementation() {
		ClsEntityRepository parent = (ClsEntityRepository) this.parent;
		Attr aSqlCon = parent.getAttrByName("sqlCon");
		Var sqlQuery = _declare(EntityCls.getSqlQueryCls(), "query",EntityCls.getSqlQueryCls().newInstance(aSqlCon));
//		TryCatchBlock tryCatch = _tryCatch();
		if(pTransactionHandle != null ) {
			_callMethodInstr(sqlQuery, ClsFirebirdSqlQuery.setTransactionHandle, pTransactionHandle);
		}
		InstructionBlock mainBlock = this; //tryCatch.getTryBlock()
		
		Param pEntity = getParam("entity");
	
		IfBlock ifIsInsertNew = mainBlock._if(pEntity.callMethod(ClsBaseEntity.METHOD_NAME_IS_INSERT_NEW));
//		ifIsInsertNew.thenBlock()._callMethodInstr(sqlQuery, ClsSqlQuery.METHOD_NAME_BEGIN_TRANSACTION);
		
		ifIsInsertNew.thenBlock()
		
			._callMethodInstr(sqlQuery,  ClsSqlQuery.insertInto,parent.callStaticMethod(ClsEntityRepository.getMethodNameGetTableName(entity)));
		
		IfBlock ifHasUpdate = ifIsInsertNew.elseBlock()._if(pEntity.callMethod(ClsBaseEntity.METHOD_NAME_HAS_UPDATE));
		
		
//		ifHasUpdate.thenBlock()._callMethodInstr(sqlQuery, ClsSqlQuery.METHOD_NAME_BEGIN_TRANSACTION);
		ifHasUpdate.thenBlock()._callMethodInstr(sqlQuery,  ClsSqlQuery.update,parent.callStaticMethod(ClsEntityRepository.getMethodNameGetTableName(entity)));
		
		
		
		
		Table tbl = entity.getTbl();
		List<Column> columns = tbl.getColumns(!tbl.getPrimaryKey().isAutoIncrement());
		for(Column col : columns) {
			if(!col.isRawValueEnabled()) {
			
				if (col.isNullable()) {
					// begin insert
					IfBlock ifColIsNull = ifIsInsertNew.thenBlock()._if(EntityCls.accessColumnAttrOrEntity(pEntity,col).isNull());
					
					ifColIsNull.thenBlock()._callMethodInstr(sqlQuery, 
							ClsSqlQuery.setValue,new PhpStringLiteral(col.getEscapedName()), 
							EntityCls.getTypeMapper().getNullInsertUpdateValueExpression( col));
					
					ifColIsNull.elseBlock()._callMethodInstr(sqlQuery, 
							ClsSqlQuery.setValue,new PhpStringLiteral(col.getEscapedName()), 
							EntityCls.getTypeMapper().getInsertUpdateValueExpression( EntityCls.accessAttrGetterByColumn(pEntity,col,false),col));
					// end insert
					
					// begin update		
					if (!col.isPartOfPk()) {
	
						IfBlock ifModified = ifHasUpdate.thenBlock()._if(EntityCls.accessIsColumnAttrOrEntityModified(pEntity, col));
						IfBlock ifColIsNullElse = ifModified.thenBlock()._if(EntityCls.accessColumnAttrOrEntity(pEntity,col).isNull());
						
						ifColIsNullElse.thenBlock()._callMethodInstr(sqlQuery, 
								ClsSqlQuery.setValue,new PhpStringLiteral(col.getEscapedName()), 
								EntityCls.getTypeMapper().getNullInsertUpdateValueExpression( col));
						
						ifColIsNullElse.elseBlock()._callMethodInstr(sqlQuery, 
								ClsSqlQuery.setValue,new PhpStringLiteral(col.getEscapedName()), 
								EntityCls.getTypeMapper().getInsertUpdateValueExpression(EntityCls.accessAttrGetterByColumn(pEntity,col,false),col));
					}
					// end update
				} else {
					ifIsInsertNew.thenBlock()._callMethodInstr(sqlQuery,  ClsSqlQuery.setValue,new PhpStringLiteral(col.getEscapedName()), Types.SqlParam.callStaticMethod(ClsSqlParam.getMethodName(EntityCls.getTypeMapper().columnToType(col)), EntityCls.accessAttrGetterByColumn(pEntity,col,true)));
					
					if (!col.isPartOfPk()) {
						IfBlock ifModified = ifHasUpdate.thenBlock()._if(EntityCls.accessIsColumnAttrOrEntityModified(pEntity, col));
						ifModified.thenBlock()._callMethodInstr(sqlQuery, 
								ClsSqlQuery.setValue,new PhpStringLiteral(col.getEscapedName()), 
								EntityCls.getTypeMapper().getInsertUpdateValueExpression( EntityCls.accessAttrGetterByColumn(pEntity,col,false),col));
					
					}
				}
			} else {
				ifIsInsertNew.thenBlock()._callMethodInstr(sqlQuery, ClsSqlQuery.addInsertRawExpression,new PhpStringLiteral(col.getEscapedName()), pEntity.callAttrGetter("insertExpression"+col.getUc1stCamelCaseName()));
			}
		}
		
		IfBlock ifNotIsPkModified = ifHasUpdate.thenBlock()._if(_not(pEntity.callMethod(ClsBaseEntity.METHOD_NAME_IS_PRIMARY_KEY_MODIFIED)));
		
		
		for (Column colPk : tbl.getPrimaryKey().getColumns()) {
			ifNotIsPkModified.thenBlock()._callMethodInstr(sqlQuery,  ClsSqlQuery.where,new PhpStringLiteral(colPk.getEscapedName()+"=?"), EntityCls.getTypeMapper().getInsertUpdateValueExpression( EntityCls.accessAttrGetterByColumn(pEntity,colPk,true),colPk));
			
			
			ifNotIsPkModified.elseBlock()._callMethodInstr(sqlQuery, 
					ClsSqlQuery.setValue,new PhpStringLiteral(colPk.getEscapedName()), 
					EntityCls.getTypeMapper().getInsertUpdateValueExpression(EntityCls.accessAttrGetterByColumn(pEntity,colPk,true),colPk));
			ifNotIsPkModified.elseBlock()._callMethodInstr(sqlQuery,  ClsSqlQuery.where,new PhpStringLiteral(colPk.getEscapedName()+"=?"), EntityCls.getTypeMapper().getInsertUpdateValueExpression(EntityCls.accessPrimaryKeyPreviousAttrGetterByColumn(pEntity,colPk),colPk));
		}
		
		ifIsInsertNew.thenBlock()._callMethodInstr(sqlQuery, ClsSqlQuery.execute);
		if(tbl.isAutoIncrement()) {
			ifIsInsertNew.thenBlock()._callMethodInstr(pEntity, ClsBaseEntity.METHOD_NAME_SET_AUTO_INCREMENT_ID, aSqlCon.accessAttr(ClsMysqli.insert_id));
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
			MethodCall expressionManyToManyRemoved = pEntity.callAttrGetter(entity.getAttrByName(OrmUtil.getManyRelationDestAttrName(r)+"Removed" ));
			IfBlock ifRemoveEntities = _if(pEntity.callMethod(MethodHasRemovedManyToMany.getMethodName(r)));
		
			
			Var varDeleteSql = ifRemoveEntities.thenBlock()._declare(EntityCls.getSqlQueryCls(), "deleteSqlQuery",EntityCls.getSqlQueryCls().newInstance(aSqlCon));
			ifRemoveEntities.thenBlock()._callMethodInstr(varDeleteSql, ClsSqlQuery.deleteFrom, new PhpStringLiteral(r.getMappingTable().getName()));
			
			ArrayList<String> whereSourceCols = new ArrayList<>(r.getSourceColumnCount());
			Expression[] whereSourceColsParams = new Expression[r.getSourceColumnCount()+1];
			
			for(int i=0;i<r.getSourceColumnCount();i++) {
				whereSourceCols.add(r.getSourceMappingColumn(i).getEscapedName());
				whereSourceColsParams[i+1] = EntityCls.getTypeMapper().getInsertUpdateValueGetterExpression(pEntity,r.getSourceEntityColumn(i)); 
				//ifRemoveEntities.thenBlock()._callMethodInstr(varDeleteSql, ClsSqlQuery.where, PhpString.stringConstant(r.getSourceMappingColumn(i).getEscapedName()+" = ?"),Types.SqlParam.callStaticMethod(ClsSqlParam.getMethodName(EntityCls.getTypeMapper().columnToType(col)),pEntity.callAttrGetter(r.getSourceEntityColumn(i).getCamelCaseName())));
			
			}
			whereSourceColsParams[0] = whereSourceCols.size() > 1 ? new PhpStringLiteral("("+ CodeUtil2.commaSep(whereSourceCols)+") = (" + CodeUtil2.strMultiply("?", ",", whereSourceCols.size())+ ")") :  new PhpStringLiteral( whereSourceCols.get(0)+ " = ?");
			ifRemoveEntities.thenBlock()._callMethodInstr(varDeleteSql, ClsSqlQuery.where, whereSourceColsParams);
			
						
			IfBlock ifCountEq1 = ifRemoveEntities.thenBlock()
					._if(expressionManyToManyRemoved.count().equalsOp(new IntExpression(1)));
			
			Expression[] whereDestColsParams = new Expression[r.getDestColumnCount()+1];
			
			
			
			if(r.getDestColumnCount() == 1) {
				whereDestColsParams[0] =new PhpStringLiteral( (columnsIn.size()>1? CodeUtil2.parentheses(CodeUtil2.concat(columnsIn, ",")):columnsIn.get(0))+" = ?");
				for(int i= 0;i<r.getDestColumnCount(); i++) {
					whereDestColsParams[i+1] = Types.SqlParam.callStaticMethod(ClsSqlParam.getMethodName(EntityCls.getTypeMapper().columnToType(r.getDestMappingColumn(i))), expressionManyToManyRemoved.arrayIndex(new IntExpression(0)));
				}
				
				ifCountEq1.elseBlock()._callMethodInstr(varDeleteSql, ClsSqlQuery.where, PhpFunctions.sprintf.call( new PhpStringLiteral( (columnsIn.size()>1? CodeUtil2.parentheses(CodeUtil2.concat(columnsIn, ",")):columnsIn.get(0))+" in (%s)"),Types.SqlUtil.callStaticMethod(ClsSqlUtil.getPlaceholders, expressionManyToManyRemoved.count())),expressionManyToManyRemoved);
				ifCountEq1.thenBlock()._callMethodInstr(varDeleteSql, ClsSqlQuery.where, whereDestColsParams);
			} else {
				whereDestColsParams[0] = new PhpStringLiteral( (columnsIn.size()>1? CodeUtil2.parentheses(CodeUtil2.concat(columnsIn, ",")):columnsIn.get(0))+" = (" + CodeUtil2.strMultiply("?", ",",r.getDestColumnCount() )+")");
				for(int i= 0;i<r.getDestColumnCount(); i++) {
					whereDestColsParams[i+1] = Types.SqlParam.callStaticMethod(ClsSqlParam.getMethodName(EntityCls.getTypeMapper().columnToType(r.getDestMappingColumn(i))), expressionManyToManyRemoved.arrayIndex( new IntExpression(0)).callAttrGetter(r.getDestEntityColumn(i).getCamelCaseName()));
				}
				
				Type foreachRemoveElementType = ((PhpArray) expressionManyToManyRemoved.getType()).getElementType();
				Var varParamsForeachRemove = ifCountEq1.elseBlock()._declareNew( Types.array(Types.Mixed), "params",expressionManyToManyRemoved.count().binOp("+", new IntExpression(1)));
				ForeachLoop foreachAttrRemove = ifCountEq1.elseBlock()._foreach(new Var(foreachRemoveElementType.isPrimitiveType()?foreachRemoveElementType: foreachRemoveElementType, "rem"), expressionManyToManyRemoved);
				
				for(int i= 0;i<r.getDestColumnCount(); i++) {
					foreachAttrRemove._arrayPush(varParamsForeachRemove, Types.SqlParam.callStaticMethod(ClsSqlParam.getMethodName(EntityCls.getTypeMapper().columnToType(r.getDestEntityColumn(i))), foreachAttrRemove.getVar().callAttrGetter(r.getDestEntityColumn(i).getCamelCaseName())));
				}
				ifCountEq1.elseBlock()._callMethodInstr(varDeleteSql, ClsSqlQuery.where, PhpFunctions.sprintf.call( new PhpStringLiteral( (columnsIn.size()>1? CodeUtil2.parentheses(CodeUtil2.concat(columnsIn, ",")):columnsIn.get(0))+" in (%s)"),Types.SqlUtil.callStaticMethod(ClsSqlUtil.getPlaceholdersMultipleRows, expressionManyToManyRemoved.count(), new IntExpression(r.getDestColumnCount() ))),varParamsForeachRemove);
				ifCountEq1.thenBlock()._callMethodInstr(varDeleteSql, ClsSqlQuery.where, whereDestColsParams);
			}
			// end of "removed"
			// "added"
			MethodCall expressionManyToManyAdded = pEntity.callAttrGetter(entity.getAttrByName(OrmUtil.getManyRelationDestAttrName(r)+"Added" ));
			IfBlock ifAddedEntities = _if(pEntity.callMethod(MethodHasAddedManyToMany.getMethodName(r)));
			
			Expression[] argsInsertMultiRow = new Expression[r.getMappingTable().getPrimaryKey().getColumnCount()];
			Expression[] argsOnConflictDoNothing = new Expression[r.getMappingTable().getPrimaryKey().getColumnCount()];
			
			/*for(int i = 0; i < r.getMappingTable().getPrimaryKey().getColumnCount(); i++ ) {
				argsInsertMultiRow[i] = new PhpStringLiteral(r.getMappingTable().getPrimaryKey().getColumn(i).getEscapedName());
				argsOnConflictDoNothing[i] = new PhpStringLiteral(r.getMappingTable().getPrimaryKey().getColumn(i).getEscapedName());
			}*/
			
			for(int i = 0; i < r.getSourceTable().getPrimaryKey().getColumnCount(); i++) {
				PhpStringLiteral colName = new PhpStringLiteral(r.getSourceMappingColumn(i).getEscapedName());
				argsInsertMultiRow[i] = colName;
				argsOnConflictDoNothing[i] = colName;
			}

			for(int i = 0; i < r.getDestTable().getPrimaryKey().getColumnCount(); i++) {
				PhpStringLiteral colName = new PhpStringLiteral(r.getDestMappingColumn(i).getEscapedName());
				argsInsertMultiRow[i+r.getSourceTable().getPrimaryKey().getColumnCount()] = colName;
				argsOnConflictDoNothing[i+r.getSourceTable().getPrimaryKey().getColumnCount()] = colName;
			}
			
			Var varAddSql = ifAddedEntities.thenBlock()._declare(EntityCls.getSqlQueryCls(), "addSqlQuery",EntityCls.getSqlQueryCls().newInstance(aSqlCon));
			ifAddedEntities.thenBlock()._callMethodInstr(varAddSql, ClsSqlQuery.insertMultiRow,new PhpStringLiteral(r.getMappingTable().getName()), new ArrayInitExpression( argsInsertMultiRow));
			ifAddedEntities.thenBlock()._callMethodInstr(varAddSql, ClsSqlQuery.onConflictDoNothing, new ArrayInitExpression(argsOnConflictDoNothing));
			Type foreachAddedElementType = ((PhpArray) expressionManyToManyAdded.getType()).getElementType();
			ForeachLoop foreachAttrAdd = ifAddedEntities.thenBlock()._foreach(new Var(foreachAddedElementType, "added"), expressionManyToManyAdded);
			Expression[] addInsertRowArgs  = new Expression[r.getSourceTable().getPrimaryKey().getColumnCount()+r.getDestTable().getPrimaryKey().getColumns().size()];
			
			for(int i = 0; i < r.getSourceTable().getPrimaryKey().getColumnCount(); i++) {
				Column col = r.getSourceTable().getPrimaryKey().getColumn(i); 
				addInsertRowArgs[i] = EntityCls.getTypeMapper().getInsertUpdateValueGetterExpression(pEntity, col); 
			}
			
			for(int i = 0; i < r.getDestTable().getPrimaryKey().getColumnCount(); i++) {
				Column colPk =  r.getDestTable().getPrimaryKey().getColumn(i);
				addInsertRowArgs[i+r.getSourceTable().getPrimaryKey().getColumnCount()] = EntityCls.getTypeMapper().getInsertUpdateValueGetterExpression( foreachAttrAdd.getVar(),colPk);
			}
			
			foreachAttrAdd._callMethodInstr(varAddSql, ClsSqlQuery.addInsertRow, new ArrayInitExpression( addInsertRowArgs));
			
			
			ifAddedEntities.thenBlock()._callMethodInstr(varAddSql, ClsSqlQuery.execute);
			
			ifRemoveEntities.thenBlock()._callMethodInstr(varDeleteSql, ClsSqlQuery.execute);

			_callMethodInstr(pEntity, MethodClearModified.getMethodName());
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
