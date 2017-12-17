package php.beanrepository.method;

import java.util.ArrayList;
import java.util.List;

import database.column.Column;
import database.relation.ManyRelation;
import database.table.Table;
import php.bean.BeanCls;
import php.bean.method.MethodClearModified;
import php.bean.method.MethodHasAddedManyToMany;
import php.bean.method.MethodHasRemovedManyToMany;
import php.beanrepository.ClsBeanRepository;
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
import php.lib.ClsBaseBean;
import php.lib.ClsMysqli;
import php.lib.ClsSqlParam;
import php.lib.ClsSqlQuery;
import php.lib.ClsSqlUtil;
import php.orm.OrmUtil;
import util.CodeUtil2;

public class MethodBeanSave extends Method {
	protected BeanCls bean;

	public MethodBeanSave(BeanCls bean) {
		super(Public, Types.Void, "save"+bean.getName());
		setStatic(true);
		this.bean = bean;
		addParam(new Param(bean, "bean"));
	}

	@Override
	public void addImplementation() {
		ClsBeanRepository parent = (ClsBeanRepository) this.parent;
		Attr aSqlCon = parent.getAttrByName("sqlCon");
		Var sqlQuery = _declare(BeanCls.getSqlQueryCls(), "query",BeanCls.getSqlQueryCls().newInstance(aSqlCon));
//		TryCatchBlock tryCatch = _tryCatch();
		
		InstructionBlock mainBlock = this; //tryCatch.getTryBlock()
		
		Param pBean = getParam("bean");
	
		IfBlock ifIsInsertNew = mainBlock._if(pBean.callMethod(ClsBaseBean.METHOD_NAME_IS_INSERT_NEW));
//		ifIsInsertNew.thenBlock()._callMethodInstr(sqlQuery, ClsSqlQuery.METHOD_NAME_BEGIN_TRANSACTION);
		
		ifIsInsertNew.thenBlock()
		
			._callMethodInstr(sqlQuery,  ClsSqlQuery.insertInto,parent.callStaticMethod(ClsBeanRepository.getMethodNameGetTableName(bean)));
		
		IfBlock ifHasUpdate = ifIsInsertNew.elseBlock()._if(pBean.callMethod(ClsBaseBean.METHOD_NAME_HAS_UPDATE));
		
		
//		ifHasUpdate.thenBlock()._callMethodInstr(sqlQuery, ClsSqlQuery.METHOD_NAME_BEGIN_TRANSACTION);
		ifHasUpdate.thenBlock()._callMethodInstr(sqlQuery,  ClsSqlQuery.update,parent.callStaticMethod(ClsBeanRepository.getMethodNameGetTableName(bean)));
		
		
		
		
		Table tbl = bean.getTbl();
		List<Column> columns = tbl.getColumns(!tbl.getPrimaryKey().isAutoIncrement());
		for(Column col : columns) {
			if (col.isNullable()) {
				// begin insert
				IfBlock ifColIsNull = ifIsInsertNew.thenBlock()._if(BeanCls.accessColumnAttrOrEntity(pBean,col).isNull());
				
				ifColIsNull.thenBlock()._callMethodInstr(sqlQuery, 
						ClsSqlQuery.setValue,new PhpStringLiteral(col.getEscapedName()), 
						Types.SqlParam.callStaticMethod(ClsSqlParam.getNullMethodName(BeanCls.getTypeMapper().columnToType(col))));
				
				ifColIsNull.elseBlock()._callMethodInstr(sqlQuery, 
						ClsSqlQuery.setValue,new PhpStringLiteral(col.getEscapedName()), 
						Types.SqlParam.callStaticMethod(ClsSqlParam.getMethodName(BeanCls.getTypeMapper().columnToType(col)), BeanCls.accessAttrGetterByColumn(pBean,col,false)));
				// end insert
				
				// begin update		
				if (!col.isPartOfPk()) {

					IfBlock ifModified = ifHasUpdate.thenBlock()._if(BeanCls.accessIsColumnAttrOrEntityModified(pBean, col));
					IfBlock ifColIsNullElse = ifModified.thenBlock()._if(BeanCls.accessColumnAttrOrEntity(pBean,col).isNull());
					
					ifColIsNullElse.thenBlock()._callMethodInstr(sqlQuery, 
							ClsSqlQuery.setValue,new PhpStringLiteral(col.getEscapedName()), 
							Expressions.Null);
					
					ifColIsNullElse.elseBlock()._callMethodInstr(sqlQuery, 
							ClsSqlQuery.setValue,new PhpStringLiteral(col.getEscapedName()), 
							Types.SqlParam.callStaticMethod(ClsSqlParam.getMethodName(BeanCls.getTypeMapper().columnToType(col)), BeanCls.accessAttrGetterByColumn(pBean,col,false)));
				}
				// end update
			} else {
				ifIsInsertNew.thenBlock()._callMethodInstr(sqlQuery,  ClsSqlQuery.setValue,new PhpStringLiteral(col.getEscapedName()), Types.SqlParam.callStaticMethod(ClsSqlParam.getMethodName(BeanCls.getTypeMapper().columnToType(col)), BeanCls.accessAttrGetterByColumn(pBean,col,true)));
				
				if (!col.isPartOfPk()) {
					IfBlock ifModified = ifHasUpdate.thenBlock()._if(BeanCls.accessIsColumnAttrOrEntityModified(pBean, col));
					ifModified.thenBlock()._callMethodInstr(sqlQuery, 
							ClsSqlQuery.setValue,new PhpStringLiteral(col.getEscapedName()), 
							Types.SqlParam.callStaticMethod(ClsSqlParam.getMethodName(BeanCls.getTypeMapper().columnToType(col)), BeanCls.accessAttrGetterByColumn(pBean,col,false)));
				
				}
			}
			
		}
		
		IfBlock ifNotIsPkModified = ifHasUpdate.thenBlock()._if(_not(pBean.callMethod(ClsBaseBean.METHOD_NAME_IS_PRIMARY_KEY_MODIFIED)));
		
		
		for (Column colPk : tbl.getPrimaryKey().getColumns()) {
			ifNotIsPkModified.thenBlock()._callMethodInstr(sqlQuery,  ClsSqlQuery.where,new PhpStringLiteral(colPk.getEscapedName()+"=?"), Types.SqlParam.callStaticMethod(ClsSqlParam.getMethodName(BeanCls.getTypeMapper().columnToType(colPk)), BeanCls.accessAttrGetterByColumn(pBean,colPk,true)));
			
			
			ifNotIsPkModified.elseBlock()._callMethodInstr(sqlQuery, 
					ClsSqlQuery.setValue,new PhpStringLiteral(colPk.getEscapedName()), 
					Types.SqlParam.callStaticMethod(ClsSqlParam.getMethodName(BeanCls.getTypeMapper().columnToType(colPk)), BeanCls.accessAttrGetterByColumn(pBean,colPk,true)));
			ifNotIsPkModified.elseBlock()._callMethodInstr(sqlQuery,  ClsSqlQuery.where,new PhpStringLiteral(colPk.getEscapedName()+"=?"), Types.SqlParam.callStaticMethod(ClsSqlParam.getMethodName(BeanCls.getTypeMapper().columnToType(colPk)), BeanCls.accessPrimaryKeyPreviousAttrGetterByColumn(pBean,colPk)));
		}
		
		ifIsInsertNew.thenBlock()._callMethodInstr(sqlQuery, ClsSqlQuery.execute);
		if(tbl.isAutoIncrement()) {
			ifIsInsertNew.thenBlock()._callMethodInstr(pBean, ClsBaseBean.METHOD_NAME_SET_AUTO_INCREMENT_ID, aSqlCon.accessAttr(ClsMysqli.insert_id));
		} 
		ifHasUpdate.thenBlock()._callMethodInstr(sqlQuery, ClsSqlQuery.execute);


		
		List<ManyRelation> manyRelations = bean.getManyToManyRelations();

		
		for(ManyRelation r:manyRelations) {
			if (r.getDestColumnCount()==0) {
				throw new RuntimeException();
			}
			ArrayList<String> columnsIn=new ArrayList<>();
			for(int i=0;i<r.getDestColumnCount();i++) {
				columnsIn.add( r.getDestMappingColumn(i).getEscapedName() );
			}
			
			// "removed"
			MethodCall expressionManyToManyRemoved = pBean.callAttrGetter(bean.getAttrByName(OrmUtil.getManyRelationDestAttrName(r)+"Removed" ));
			IfBlock ifRemoveBeans = _if(pBean.callMethod(MethodHasRemovedManyToMany.getMethodName(r)));
		
			
			Var varDeleteSql = ifRemoveBeans.thenBlock()._declare(BeanCls.getSqlQueryCls(), "deleteSqlQuery",BeanCls.getSqlQueryCls().newInstance(aSqlCon));
			ifRemoveBeans.thenBlock()._callMethodInstr(varDeleteSql, ClsSqlQuery.deleteFrom, new PhpStringLiteral(r.getMappingTable().getName()));
			
			ArrayList<String> whereSourceCols = new ArrayList<>(r.getSourceColumnCount());
			Expression[] whereSourceColsParams = new Expression[r.getSourceColumnCount()+1];
			
			for(int i=0;i<r.getSourceColumnCount();i++) {
				whereSourceCols.add(r.getSourceMappingColumn(i).getEscapedName());
				whereSourceColsParams[i+1] =Types.SqlParam.callStaticMethod(ClsSqlParam.getMethodName(BeanCls.getTypeMapper().columnToType(r.getSourceEntityColumn(i))),pBean.callAttrGetter(r.getSourceEntityColumn(i).getCamelCaseName())); 
				//ifRemoveBeans.thenBlock()._callMethodInstr(varDeleteSql, ClsSqlQuery.where, PhpString.stringConstant(r.getSourceMappingColumn(i).getEscapedName()+" = ?"),Types.SqlParam.callStaticMethod(ClsSqlParam.getMethodName(BeanCls.getTypeMapper().columnToType(col)),pBean.callAttrGetter(r.getSourceEntityColumn(i).getCamelCaseName())));
			
			}
			whereSourceColsParams[0] = whereSourceCols.size() > 1 ? new PhpStringLiteral("("+ CodeUtil2.commaSep(whereSourceCols)+") = (" + CodeUtil2.strMultiply("?", ",", whereSourceCols.size())+ ")") :  new PhpStringLiteral( whereSourceCols.get(0)+ " = ?");
			ifRemoveBeans.thenBlock()._callMethodInstr(varDeleteSql, ClsSqlQuery.where, whereSourceColsParams);
			
			
			//
			//
			
			IfBlock ifCountEq1 = ifRemoveBeans.thenBlock()
					._if(expressionManyToManyRemoved.count().equalsOp(new IntExpression(1)));
			
			Expression[] whereDestColsParams = new Expression[r.getDestColumnCount()+1];
			
			
			
			if(r.getDestColumnCount() == 1) {
				whereDestColsParams[0] =new PhpStringLiteral( (columnsIn.size()>1? CodeUtil2.parentheses(CodeUtil2.concat(columnsIn, ",")):columnsIn.get(0))+" = ?");
				for(int i= 0;i<r.getDestColumnCount(); i++) {
					whereDestColsParams[i+1] = Types.SqlParam.callStaticMethod(ClsSqlParam.getMethodName(BeanCls.getTypeMapper().columnToType(r.getDestMappingColumn(i))), expressionManyToManyRemoved.arrayIndex(new IntExpression(0)));
				}
				
				ifCountEq1.elseBlock()._callMethodInstr(varDeleteSql, ClsSqlQuery.where, PhpFunctions.sprintf.call( new PhpStringLiteral( (columnsIn.size()>1? CodeUtil2.parentheses(CodeUtil2.concat(columnsIn, ",")):columnsIn.get(0))+" in (%s)"),Types.SqlUtil.callStaticMethod(ClsSqlUtil.getPlaceholders, expressionManyToManyRemoved.count())),expressionManyToManyRemoved);
				ifCountEq1.thenBlock()._callMethodInstr(varDeleteSql, ClsSqlQuery.where, whereDestColsParams);
			} else {
				whereDestColsParams[0] = new PhpStringLiteral( (columnsIn.size()>1? CodeUtil2.parentheses(CodeUtil2.concat(columnsIn, ",")):columnsIn.get(0))+" = (" + CodeUtil2.strMultiply("?", ",",r.getDestColumnCount() )+")");
				for(int i= 0;i<r.getDestColumnCount(); i++) {
					whereDestColsParams[i+1] = Types.SqlParam.callStaticMethod(ClsSqlParam.getMethodName(BeanCls.getTypeMapper().columnToType(r.getDestMappingColumn(i))), expressionManyToManyRemoved.arrayIndex( new IntExpression(0)).callAttrGetter(r.getDestEntityColumn(i).getCamelCaseName()));
				}
				
				Type foreachRemoveElementType = ((PhpArray) expressionManyToManyRemoved.getType()).getElementType();
				Var varParamsForeachRemove = ifCountEq1.elseBlock()._declareNew( Types.array(Types.SqlParam), "params",expressionManyToManyRemoved.count().binOp("+", new IntExpression(1)));
				ForeachLoop foreachAttrRemove = ifCountEq1.elseBlock()._foreach(new Var(foreachRemoveElementType.isPrimitiveType()?foreachRemoveElementType: foreachRemoveElementType, "rem"), expressionManyToManyRemoved);
				
				for(int i= 0;i<r.getDestColumnCount(); i++) {
					foreachAttrRemove._arrayPush(varParamsForeachRemove, Types.SqlParam.callStaticMethod(ClsSqlParam.getMethodName(BeanCls.getTypeMapper().columnToType(r.getDestEntityColumn(i))), foreachAttrRemove.getVar().callAttrGetter(r.getDestEntityColumn(i).getCamelCaseName())));
				}
				ifCountEq1.elseBlock()._callMethodInstr(varDeleteSql, ClsSqlQuery.where, PhpFunctions.sprintf.call( new PhpStringLiteral( (columnsIn.size()>1? CodeUtil2.parentheses(CodeUtil2.concat(columnsIn, ",")):columnsIn.get(0))+" in (%s)"),Types.SqlUtil.callStaticMethod(ClsSqlUtil.getPlaceholdersMultipleRows, expressionManyToManyRemoved.count(), new IntExpression(r.getDestColumnCount() ))),varParamsForeachRemove);
				ifCountEq1.thenBlock()._callMethodInstr(varDeleteSql, ClsSqlQuery.where, whereDestColsParams);
			}
			// end of "removed"
			// "added"
			MethodCall expressionManyToManyAdded = pBean.callAttrGetter(bean.getAttrByName(OrmUtil.getManyRelationDestAttrName(r)+"Added" ));
			IfBlock ifAddedBeans = _if(pBean.callMethod(MethodHasAddedManyToMany.getMethodName(r)));
			
			Expression[] argsInsertMultiRow = new Expression[r.getMappingTable().getPrimaryKey().getColumnCount()];
			Expression[] argsOnConflictDoNothing = new Expression[r.getMappingTable().getPrimaryKey().getColumnCount()];
			
			for(int i = 0; i < r.getMappingTable().getPrimaryKey().getColumnCount(); i++ ) {
				argsInsertMultiRow[i] = new PhpStringLiteral(r.getMappingTable().getPrimaryKey().getColumn(i).getEscapedName());
				argsOnConflictDoNothing[i] = new PhpStringLiteral(r.getMappingTable().getPrimaryKey().getColumn(i).getEscapedName());
			}
			
			Var varAddSql = ifAddedBeans.thenBlock()._declare(BeanCls.getSqlQueryCls(), "addSqlQuery",BeanCls.getSqlQueryCls().newInstance(aSqlCon));
			ifAddedBeans.thenBlock()._callMethodInstr(varAddSql, ClsSqlQuery.insertMultiRow,new PhpStringLiteral(r.getMappingTable().getName()), new ArrayInitExpression( argsInsertMultiRow));
			ifAddedBeans.thenBlock()._callMethodInstr(varAddSql, ClsSqlQuery.onConflictDoNothing, new ArrayInitExpression(argsOnConflictDoNothing));
			Type foreachAddedElementType = ((PhpArray) expressionManyToManyAdded.getType()).getElementType();
			ForeachLoop foreachAttrAdd = ifAddedBeans.thenBlock()._foreach(new Var(foreachAddedElementType, "added"), expressionManyToManyAdded);
			Expression[] addInsertRowArgs  = new Expression[r.getSourceTable().getPrimaryKey().getColumnCount()+r.getDestTable().getPrimaryKey().getColumns().size()];
			
			for(int i = 0; i < r.getSourceTable().getPrimaryKey().getColumnCount(); i++) {
				Column col = r.getSourceTable().getPrimaryKey().getColumn(i); 
				addInsertRowArgs[i] = Types.SqlParam.callStaticMethod(ClsSqlParam.getMethodName(BeanCls.getTypeMapper().columnToType(col)), pBean.callAttrGetter(new Attr(BeanCls.getTypeMapper().columnToType(col), col.getCamelCaseName()))); 
			}
			
			if (r.getDestTable().getPrimaryKey().isMultiColumn()) {
				
				for(int i = 0; i < r.getDestTable().getPrimaryKey().getColumnCount(); i++) {
					Column colPk =  r.getDestTable().getPrimaryKey().getColumn(i);
					addInsertRowArgs[i+r.getSourceTable().getPrimaryKey().getColumnCount()] = Types.SqlParam.callStaticMethod(ClsSqlParam.getMethodName(BeanCls.getTypeMapper().columnToType(colPk)), foreachAttrAdd.getVar().callAttrGetter(colPk.getCamelCaseName()));
				}
			} else {
				Column colPk = r.getDestTable().getPrimaryKey().getFirstColumn();
				addInsertRowArgs[r.getSourceTable().getPrimaryKey().getColumnCount()] = Types.SqlParam.callStaticMethod(ClsSqlParam.getMethodName(BeanCls.getTypeMapper().columnToType( colPk)), foreachAttrAdd.getVar().callAttrGetter(colPk.getCamelCaseName()));
				
			}
			
			foreachAttrAdd._callMethodInstr(varAddSql, ClsSqlQuery.addInsertRow, new ArrayInitExpression( addInsertRowArgs));
			
			
			ifAddedBeans.thenBlock()._callMethodInstr(varAddSql, ClsSqlQuery.execute);
			
			ifRemoveBeans.thenBlock()._callMethodInstr(varDeleteSql, ClsSqlQuery.execute);

			_callMethodInstr(pBean, MethodClearModified.getMethodName());
			/* example: if (!bean.getEntity1sAdded().isEmpty()){
			SqlQuery addSqlQuery  = new PgSqlQuery(sqlCon);
			addSqlQuery.insertMultiRow("e1_e3_mm", "e1_id", "e3_id");
			addSqlQuery.onConflictDoNothing("e1_id", "e3_id");
			
			for (Integer added : bean.getEntity1sAdded()){
				addSqlQuery.addInsertRow(SqlParam.get(added), SqlParam.get(bean.getId()));
				
			}
			
			addSqlQuery.execute();
		}*/
			
			/*if (bean.getTbl().getPrimaryKey().isMultiColumn()) {
				for(Column col:bean.getTbl().getPrimaryKey().getColumns()) {
					foreachAttrAdd.addInstr(varParamsForeachAdd.callMethodInstruction("append", _this().accessAttr(  col.getCamelCaseName())));
				}
			} else {
				foreachAttrAdd.addInstr(varParamsForeachAdd.callMethodInstruction("append", _this().accessAttr(  bean.getTbl().getPrimaryKey().getFirstColumn().getCamelCaseName())));
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
