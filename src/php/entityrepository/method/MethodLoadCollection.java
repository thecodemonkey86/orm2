package php.entityrepository.method;

import php.core.Param;
import php.core.PhpCls;
import php.core.Types;
import php.core.expression.Expression;
import php.core.expression.Var;
import php.core.method.Method;
import php.entity.EntityCls;

public class MethodLoadCollection extends Method{
	EntityCls entity;
	
	public MethodLoadCollection(Param p,EntityCls entity) {
		super(Public, Types.Void, getMethodName(entity));
//		addParam(new Param(Types.QSqlQuery, "query"));	
		//addParam(new Param(Types.qset(cls), "collection"));
		addParam(p);
		this.entity=entity;
	}

	public static String getMethodName(EntityCls entity) {
		return "loadCollection"+entity.getName();
	}
	
	protected Var getVarSqlCon() {
		PhpCls parent = (PhpCls) this.parent;
		return parent.getAttrByName("sqlCon");
	}
	
	protected Expression getByRecordExpression(EntityCls entity, Var record, Expression alias) {
		//return new ThisEntityRepositoryExpression((EntityRepository) parent);
		return entity.callStaticMethod("getByRecord", getVarSqlCon(), record, alias);
	}
	
	@Override
	public void addImplementation() {
		_return();
//		PrimaryKey pk=entity.getTbl().getPrimaryKey();
////		Param query = getParam("query");
//		Param collection = getParam("collection");
//		
//		List<OneRelation> oneRelations = entity.getOneRelations();
//		List<OneToManyRelation> oneToManyRelations = entity.getOneToManyRelations();
//		List<ManyRelation> manyToManyRelations = entity.getManyToManyRelations();
//		ArrayList<AbstractRelation> manyRelations = new ArrayList<>();
//		manyRelations.addAll(oneToManyRelations);
//		manyRelations.addAll(manyToManyRelations);
//		
//		
////		query.callMethod("select");
//		
//		Var aSqlCon = getVarSqlCon();
//		Var sqlQuery = _declare(Types.SqlQuery, "sqlQuery",aSqlCon.callMethod("buildQuery"));
//		Type e1PkType = pk.isMultiColumn() ? entity.getStructPk() : EntityCls.getTypeMapper().columnToType( pk.getColumns().get(0));
//		
//		//ArrayList<Expression> selectFields = new ArrayList<>();
//		//selectFields.add(entity.callStaticMethod("getSelectFields",PhpString.fromStringConstant("e1")));
//		
//		List<OneRelation> relations = new ArrayList<>(oneRelations);
//		relations.addAll(oneRelations);
////		
////		//int //bCount = 2;
////		for(Relation r:relations) {
////			selectFields.add(Entities.get(r.getDestTable()).callStaticMethod("getSelectFields", PhpString.fromStringConstant(r.getAlias())));
////			//bCount++;
////		}
////		Expression exprQSqlQuery = sqlQuery.callMethod("select", Expressions.concat(QChar.fromChar(','), selectFields) )
////									.callMethod("from", PhpString.fromExpression(entity.accessStaticAttribute("TABLENAME")).concat(PhpString.fromStringConstant(" e1")));
//		
//		Expression exprQSqlQuery = sqlQuery.callMethod("select", entity.callStaticMethod("getAllSelectFields",PhpString.fromStringConstant("e1")))
//				.callMethod("from", PhpString.fromExpression(entity.callStaticMethod("getTableName")).concat(PhpString.fromStringConstant(" e1")));
//		
//		//int //bCount = 2;
//		
//		for(OneRelation r:relations) {
//			if (parent.getName().equals("OrmTest")) {
//				System.out.println();
//			}
//			ArrayList<String> joinConditions=new ArrayList<>();
//			for(int i=0;i<r.getColumnCount();i++) {
//				joinConditions.add(CodeUtil.sp("e1."+r.getColumns(i).getValue1().getEscapedName(),'=',r.getAlias()+"."+ r.getColumns(i).getValue2().getEscapedName()));
//			}
//			
//			exprQSqlQuery = exprQSqlQuery.callMethod("leftJoin", PhpString.fromExpression(Entities.get(r.getDestTable()).callStaticMethod("getTableName")),PhpString.fromStringConstant(r.getAlias()), PhpString.fromStringConstant(CodeUtil2.concat(joinConditions," AND ")));
//			//bCount++;
//		}
//		
//		
//		ArrayInitList init=new ArrayInitList();
//		
//		for (Column pkCol : pk.getColumns()) {
//			init.addElement(PhpString.fromStringConstant("e1."+ pkCol.getEscapedName()));
//		}
//		Var varColumns = _declare(Types.array(Types.String), "columns",init);
//		
//		
//		
//		
//		Var params= _declare(Types.arraylist(Types.SqlParam), "params");
////		_callMethodInstr(params, "reserve", collection.callMethod("size"));
//		Var varForeachEntity = new Var(entity, "entity");
//		ForeachLoop foreach= _foreach(varForeachEntity, collection);
//		for (Column pkCol : pk.getColumns()) {
//			
//			if(pkCol.hasOneRelation()){
//				//colPk.getRelation().getDestTable().getCamelCaseName()
//				foreach._callMethodInstr(params, "append",varForeachEntity.callAttrGetter(PgCppUtil.getOneRelationDestAttrName(pkCol.getOneRelation())).callMethod("get"+pkCol.getOneRelationMappedColumn().getUc1stCamelCaseName()) ); 
//			}else{
//				foreach._callMethodInstr(params, "append",varForeachEntity.callAttrGetter(pkCol.getCamelCaseName()));	
//			}
//			
////			foreach._callMethodInstr(params, "append", varForeachEntity.callAttrGetter(pkCol.getCamelCaseName()));
//		}
//		
//		exprQSqlQuery = exprQSqlQuery.callMethod("whereIn", varColumns, params);
//		
//		addInstr(exprQSqlQuery.asInstruction());
//		Var query = _declare(Types.QSqlQuery, "query", sqlQuery.callMethod("execQuery"));
//		
//		
//		IfBlock ifQueryNext = _if(query.callMethod("next"));
//		InstructionBlock ifInstr = ifQueryNext.getIfInstr();
//		Var e1Map =  ifInstr._declare((!manyRelations.isEmpty()) ? new ClsHashmap(e1PkType, entity.getFetchListHelperCls()) : new ClsHashSet(e1PkType), "e1Map");
//		
//		DoWhile doWhileQueryNext = ifQueryNext.getIfInstr()._doWhile();
//		doWhileQueryNext.setCondition(query.callMethod("next"));
//		Var recDoWhile =doWhileQueryNext._declare(Types.QSqlRecord, "rec",query.callMethod("record") );
//		
//		Var e1pk = null;
//		ArrayList<Expression> listForeachPkCompare = new ArrayList<>();
//		Var varIfNotE1SetContainsForeachEntity = new Var(entity, "entity");
//		if (pk.isMultiColumn()) {
//			e1pk =doWhileQueryNext._declare( entity.getStructPk(), "e1pk" );
//			for(Column colPk:pk.getColumns()) {
//				doWhileQueryNext._assign(e1pk.accessAttr(colPk.getCamelCaseName()), recDoWhile.callMethod("value", PhpString.fromStringConstant("e1__"+ colPk.getName())).callMethod(EntityCls.getTypeMapper().getConvertMethod(colPk.getDbType())));
//				
//				listForeachPkCompare.add(
//						new BinaryOperatorExpression(
//								
//								(colPk.hasOneRelation() 
//									? varIfNotE1SetContainsForeachEntity.callMethod( OrmUtil.getOneRelationDestAttrGetter(colPk.getOneRelation())).callMethod("get"+colPk.getOneRelationMappedColumn().getUc1stCamelCaseName())								
//									: varIfNotE1SetContainsForeachEntity.callMethod("get"+colPk.getUc1stCamelCaseName())
//								),
//								new LibEqualsOperator(),
//								e1pk.accessAttr(colPk.getCamelCaseName())
//						));
//			}
//			/*if(col.hasOneRelation()){
//				//colPk.getRelation().getDestTable().getCamelCaseName()
//				addInstr(params.callMethodInstruction("append",parent.getAttrByName(PgCppUtil.getOneRelationDestAttrName(col.getOneRelation())).callMethod("get"+col.getOneRelationMappedColumn().getUc1stCamelCaseName()) )); 
//			}else{
//				addInstr(params.callMethodInstruction("append",parent.getAttrByName(col.getCamelCaseName())));	
//			}*/
//			
//		} else {
//			Column colPk=pk.getFirstColumn();
//			e1pk =doWhileQueryNext._declare( EntityCls.getTypeMapper().columnToType( pk.getFirstColumn()), "e1pk", recDoWhile.callMethod("value", PhpString.fromStringConstant("e1__"+ pk.getFirstColumn().getName())).callMethod(EntityCls.getTypeMapper().getConvertMethod(pk.getFirstColumn().getDbType())));
//			listForeachPkCompare.add(
//					new BinaryOperatorExpression(
//							varIfNotE1SetContainsForeachEntity.callMethod("get"+colPk.getUc1stCamelCaseName()),
//							new LibEqualsOperator(),
//							e1pk
//					));
//		}
//		
//		
//		IfBlock ifNotE1SetContains = doWhileQueryNext._if(Expressions.not(e1Map.callMethod("contains", e1pk)));
//		
//
//		ForeachLoop foreachIfNotE1SetContains= ifNotE1SetContains.getIfInstr()._foreach(varIfNotE1SetContainsForeachEntity, collection);
//		IfBlock ifForeachPkCompare = foreachIfNotE1SetContains._if(Expressions.and(listForeachPkCompare));
//
//		doWhileQueryNext._assignInstruction(recDoWhile, query.callMethod("record"));
//		
//		
//		
//		
////		ifForeachPkCompare.getIfInstr()._assign(structHelperIfNotE1SetContains.accessAttr("e1"), e1DoWhile);
////		//bCount = 2;
////		for(Relation r:manyRelations) {
////			Type entityPk=Types.getRelationForeignPrimaryKeyType(r);
////			ifNotE1SetContains.getIfInstr()._assign(structHelperIfNotE1SetContains.accessAttr(r.getAlias()+"Set"),  new NewOperator(Types.qset(entityPk)));
////			//bCount++;
////		}
//		//bCount = 2;
//		if (manyRelations.isEmpty()) {
//			ifForeachPkCompare.getIfInstr()._callMethodInstr(e1Map, "insert", e1pk );
//		} else {
//			Var structHelperIfNotE1SetContains = ifForeachPkCompare.getIfInstr()._declare(entity.getFetchListHelperCls(), "structHelper");
//			ifForeachPkCompare.getIfInstr()._assign(structHelperIfNotE1SetContains.accessAttr("e1"), varIfNotE1SetContainsForeachEntity);
//			ifForeachPkCompare.getIfInstr()._callMethodInstr(e1Map, "insert", e1pk, structHelperIfNotE1SetContains );
//			
//			Var fkHelper = doWhileQueryNext._declare(entity.getFetchListHelperCls(), "fkHelper",e1Map.arrayIndex(e1pk));
//			
//			for(AbstractRelation r:manyRelations) {
//				Type entityPk=Types.getRelationForeignPrimaryKeyType(r);
//				EntityCls foreignCls = Entities.get(r.getDestTable()); 
//				Expression foreignEntityExpression = getByRecordExpression(foreignCls, recDoWhile, PhpString.fromStringConstant(r.getAlias()));
////				IfBlock ifRecValueIsNotNull = null;
//				Var foreignEntity = null;				
//				
//				IfBlock ifNotPkForeignIsNull= doWhileQueryNext._if(Expressions.not( recDoWhile.callMethod("value", PhpString.fromStringConstant(r.getAlias()+"__"+ r.getDestTable().getPrimaryKey().getFirstColumn().getName())).callMethod("isNull")));
//				
//				Var pkForeign = null;
//				if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
//					pkForeign = ifNotPkForeignIsNull.getIfInstr()._declare(entityPk, "pkForeignB"+r.getAlias());
//					
//					for(Column colPk:r.getDestTable().getPrimaryKey().getColumns()) {
//						ifNotPkForeignIsNull.getIfInstr()._assign(
//								pkForeign.accessAttr(colPk.getCamelCaseName()), 
//								recDoWhile.callMethod("value", PhpString.fromStringConstant(r.getAlias()+"__"+ colPk.getName())).callMethod(EntityCls.getTypeMapper().getConvertMethod(colPk.getDbType())));
//					}
//				} else {
//					Column colPk=r.getDestTable().getPrimaryKey().getFirstColumn();
//					pkForeign = ifNotPkForeignIsNull.getIfInstr()._declare(entityPk, "pkForeignB"+r.getAlias(),recDoWhile.callMethod("value", PhpString.fromStringConstant(r.getAlias()+"__"+ r.getDestTable().getPrimaryKey().getFirstColumn().getName())).callMethod(EntityCls.getTypeMapper().getConvertMethod(colPk.getDbType())));
//				}
//				IfBlock ifRecValueIsNotNull = ifNotPkForeignIsNull.getIfInstr()._if(
//						Expressions.not(fkHelper
//										.accessAttr(r.getAlias()+"Set")
//										.callMethod("contains",
//												pkForeign
//												
//												))
//								
//						
//						
//					);
//				foreignEntity =ifRecValueIsNotNull.getIfInstr()._declare(foreignEntityExpression.getType(), "foreignB"+r.getAlias(),foreignEntityExpression) ;
//				
//				ifRecValueIsNotNull.getIfInstr().addInstr(fkHelper.accessAttr("e1")
//						.callMethodInstruction(EntityCls.getRelatedEntityMethodName(r), foreignEntity));
//				ifRecValueIsNotNull.getIfInstr().addInstr(
//						fkHelper.accessAttr(r.getAlias()+"Set")
//						.callMethod("insert", 
//								pkForeign
//									).asInstruction())
//					 ;
//				
//				for (OneRelation foreignOneRelation: foreignCls.getOneRelations()) {
//					if (foreignOneRelation.getDestTable().equals(entity.getTbl())) {
//						ifRecValueIsNotNull.getIfInstr().addInstr(foreignEntity.callMethodInstruction("set"+r.getSourceTable().getUc1stCamelCaseName()+"Internal", fkHelper.accessAttr("e1")));
//					}
//				}
//				
//				
//				//bCount++;
//			}		
//		}
//		for(OneRelation r:oneRelations) {
//			EntityCls foreignCls = Entities.get(r.getDestTable());
//			Expression foreignEntityExpression = getByRecordExpression(foreignCls, recDoWhile, PhpString.fromStringConstant(r.getAlias()));
//			
//			IfBlock ifRelatedEntityIsNull= foreachIfNotE1SetContains.
//					_if(varForeachEntity.callMethod(new MethodOneRelationEntityIsNull(r)));
//			
//			Var foreignEntity =ifRelatedEntityIsNull.getIfInstr()._declare(foreignEntityExpression.getType(), "foreignB"+r.getAlias(),foreignEntityExpression) ;
//			ifRelatedEntityIsNull.getIfInstr()
//				._callMethodInstr(
//						varForeachEntity ,
//						new MethodAttrSetterInternal(foreignCls,
//								entity.getAttrByName(PgCppUtil.getOneRelationDestAttrName(r)))
//						,  foreignEntity);
//			
//		
//			for (OneRelation foreignOneRelation: foreignCls.getOneRelations()) {
//				if (foreignOneRelation.getDestTable().equals(entity.getTbl())) {
//					ifRelatedEntityIsNull.getIfInstr().addInstr(foreignEntity.callMethodInstruction("set"+r.getSourceTable().getUc1stCamelCaseName()+"Internal", recDoWhile));
//				}
//			}
////			ifRelatedEntityIsNull.getIfInstr()._callMethodInstr(foreignEntity, "setLoaded", BoolExpression.TRUE);
//			
//			//bCount++;
//		}
//		
//		foreachIfNotE1SetContains._callMethodInstr(varForeachEntity, "setLoaded", BoolExpression.TRUE);
//				
//		for(Column col:entity.getTbl().getAllColumns()) {
//			try{
//				if (!col.hasOneRelation() && !col.isPartOfPk()) {
//					ifForeachPkCompare.getIfInstr().addInstr(varIfNotE1SetContainsForeachEntity.callMethodInstruction("set"+ col.getUc1stCamelCaseName()+"Internal",recDoWhile.callMethod("value", PhpString.fromStringConstant("e1__"+ col.getName())).callMethod(EntityCls.getTypeMapper().getConvertMethod(col.getDbType()))));
//				}
////					_callMethodInstr(entity, "set"+col.getUc1stCamelCaseName(), getParam("record").callMethod("value", new PhpStringPlusOperatorExpression(getParam("alias"), PhpString.fromStringConstant("__"+ col.getName()))).callMethod(EntityCls.getTypeMapper().getConvertMethod(col.getDbType())));
//			} catch (Exception e) {
//				e.printStackTrace();
//				System.out.println(parent);
//			}
//		}
		
			
		
		
	}
}
