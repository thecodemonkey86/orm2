package cpp.entityrepository.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.Type;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.Expression;
import cpp.entity.EntityCls;
import cpp.entityquery.method.MethodEntityQueryFetchOne;
import cpp.entityquery.method.MethodEntityQueryWhereEquals;
import cpp.entityquery.method.MethodOrderBy;
import cpp.util.ClsDbPool;
import database.column.Column;

public class MethodGetById extends Method {

	protected EntityCls entity;
	protected boolean addSortingParam;
	protected Param pOrderBy;
	protected Param pOrderByDirection;
	protected Param pSqlCon;
	protected Param pLazyLoad;
	
	
	public MethodGetById(EntityCls entity,boolean addSortingParams) {
		super(Public, entity.toSharedPtr(), getMethodName(entity));
		this.entity=entity;
		for(Column col:entity.getTbl().getPrimaryKey().getColumns()) {
			Type colType =  EntityCls.getDatabaseMapper().columnToType(col);
			addParam(new Param(colType.isPrimitiveType() ? colType : colType.toConstRef(), col.getCamelCaseName()));
		}
		this.addSortingParam = addSortingParams;
		
		if(entity.hasRelations()) {
			
			if(addSortingParams) {
				pLazyLoad = addParam(Types.Bool,"lazyLoading");
				pOrderBy = addParam(Types.QString.toConstRef(), "orderBy");
				pOrderByDirection = addParam(Types.OrderDirection, "direction");
			} else {
				pLazyLoad = addParam(Types.Bool,"lazyLoading",BoolExpression.FALSE);
			}
		}
		
		
		pSqlCon = addParam(Types.QSqlDatabase.toConstRef(),"sqlCon",ClsDbPool.instance.callStaticMethod(ClsDbPool.getDatabase));
		setStatic(true);
	}
//	public MethodGetById(EntityCls entity) {
//		this(entity,false);
//	}
	public static String getMethodName(EntityCls cls) {
		return "get"+cls.getName()+"ById";
	}

	
	@Override
	public void addImplementation() {
//		List<OneRelation> oneRelations = entity.getOneRelations();
//		List<OneToManyRelation> oneToManyRelations = entity.getOneToManyRelations();
//		List<ManyRelation> manyToManyRelations = entity.getManyToManyRelations();
//		
//		Var sqlQuery = _declareInitConstructor( EntityCls.getDatabaseMapper().getSqlQueryType(),"query");
//		
//		
//		ArrayList<String> selectFields = new ArrayList<>();
//		for(Column col : entity.getTbl().getAllColumns()) {
//			selectFields.add("e1." + col.getEscapedName() + " as e1__" + col.getName());
//		}
//		List<AbstractRelation> allRelations = new ArrayList<>(oneRelations.size()+oneToManyRelations.size()+manyToManyRelations.size());
//		allRelations.addAll(oneRelations);
//		allRelations.addAll(oneToManyRelations);
//		allRelations.addAll(manyToManyRelations);
//		
//		for(AbstractRelation r:allRelations) {
//			for(Column col : r.getDestTable().getAllColumns()) {
//				selectFields.add( r.getAlias()+"." + col.getEscapedName() + " as "+ r.getAlias()+"__" + col.getName());
//			}
//		}
//		List<Expression> paramsSelect=new ArrayList<>();
//		paramsSelect.add(QString.fromStringConstant(CodeUtil.commaSep( selectFields) ));
//		if(pLazyLoad!=null) {
//			paramsSelect.add(pLazyLoad);
//		}
//		
//		Expression exprQSqlQuery = sqlQuery.callMethod("select", paramsSelect)
//									.callMethod("from", entity.callStaticMethod("getTableName",QString.fromStringConstant("e1")));
//		
//		for(OneRelation r:oneRelations) {
//			ArrayList<String> joinConditions=new ArrayList<>();
//			for(int i=0;i<r.getColumnCount();i++) {
//				joinConditions.add(CodeUtil.sp("e1."+r.getColumns(i).getValue1().getEscapedName(),'=',(r.getAlias())+"."+ r.getColumns(i).getValue2().getEscapedName()));
//			}
//			
//			exprQSqlQuery = exprQSqlQuery.callMethod("leftJoin", QString.fromExpression(Entities.get(r.getDestTable()).callStaticMethod("getTableName")),QString.fromStringConstant(r.getAlias()), QString.fromStringConstant(CodeUtil2.concat(joinConditions," AND ")));
//		}
//		for(OneToManyRelation r:oneToManyRelations) {
//			ArrayList<String> joinConditions=new ArrayList<>();
//			for(int i=0;i<r.getColumnCount();i++) {
//				joinConditions.add(CodeUtil.sp("e1."+r.getColumns(i).getValue1().getEscapedName(),'=',(r.getAlias())+"."+ r.getColumns(i).getValue2().getEscapedName()));
//			}
//			
//			exprQSqlQuery = exprQSqlQuery.callMethod("leftJoin", QString.fromExpression(Entities.get(r.getDestTable()).callStaticMethod("getTableName")),QString.fromStringConstant(r.getAlias()), QString.fromStringConstant(CodeUtil2.concat(joinConditions," AND ")));
//		}
//		for(ManyRelation r:manyToManyRelations) {
//			ArrayList<String> joinConditions=new ArrayList<>();
//			for(int i=0;i<r.getSourceColumnCount();i++) {
//				joinConditions.add(CodeUtil.sp("e1."+r.getSourceEntityColumn(i).getEscapedName(),'=',r.getAlias("mapping")+"."+ r.getSourceMappingColumn(i).getEscapedName()));
//			}
//			
//			exprQSqlQuery = exprQSqlQuery.callMethod("leftJoin", QString.fromStringConstant(r.getMappingTable().getName()),QString.fromStringConstant(r.getAlias("mapping")), QString.fromStringConstant(CodeUtil2.concat(joinConditions," AND ")));
//			
//			joinConditions.clear();
//			for(int i=0;i<r.getDestColumnCount();i++) {
//				joinConditions.add(CodeUtil.sp(r.getAlias("mapping")+"."+r.getDestMappingColumn(i).getEscapedName(),'=',r.getAlias()+"."+r.getDestEntityColumn(i).getEscapedName() ));
//			}
//			
//			exprQSqlQuery = exprQSqlQuery.callMethod("leftJoin", Entities.get(r.getDestTable()).callStaticMethod("getTableName"),QString.fromStringConstant(r.getAlias()), QString.fromStringConstant(CodeUtil2.concat(joinConditions," AND ")));
//			
//			//bCount++;
//		}
//
//		
//		for(Column col:entity.getTbl().getPrimaryKey().getColumns()) {
//			exprQSqlQuery = exprQSqlQuery.callMethod("where", QString.fromStringConstant("e1."+ col.getEscapedName()+"=?"),getParam(col.getCamelCaseName()));
//					
//		}
//		
//		if(addSortingParam) {
//		
//			exprQSqlQuery = exprQSqlQuery.callMethod(ClsSqlQuery.orderBy,pOrderBy);
//		}
//		
//		exprQSqlQuery = exprQSqlQuery.callMethod("execQuery",pSqlCon);
//		Var qSqlQuery = _declare(exprQSqlQuery.getType(),
//				"qSqlQuery", exprQSqlQuery
//				);
//		Var e1 = _declare(returnType, "e1", Expressions.Nullptr);
//		IfBlock ifQSqlQueryNext =
//				_if(qSqlQuery.callMethod("next"))
//					.setIfInstr(
//							e1.assign(parent.callStaticMethod(MethodGetFromRecord.getMethodName(entity),  qSqlQuery.callMethod("record"), QString.fromStringConstant("e1")))
//							);
//		
//		if(entity.hasRelations()) {
//			
//			
//		DoWhile doWhileQSqlQueryNext = DoWhile.create();
//		Var rec = doWhileQSqlQueryNext._declare(Types.QSqlRecord, "rec",qSqlQuery.callMethod("record") );
////		//bCount = 2;
//		
//		ArrayList<AbstractRelation> manyRelations = new ArrayList<>(oneToManyRelations.size()+manyToManyRelations.size());
//		manyRelations.addAll(oneToManyRelations);
//		manyRelations.addAll(manyToManyRelations);
//		
//		for(OneRelation r:oneRelations) {
//			ifQSqlQueryNext.addIfInstr(e1.callSetterMethodInstruction("loaded"+r.getIdentifier(), BoolExpression.TRUE)); //_assignInstruction(e1.accessAttr("loaded"), BoolExpression.TRUE))
//			
//			EntityCls foreignCls = Entities.get(r.getDestTable()); 
//			//AccessExpression acc = e1.accessAttr(PgCppUtil.getOneRelationDestAttrName(r));
//			//IfBlock ifBlock= doWhileQSqlQueryNext._if(acc.isNull());
//			IfBlock ifBlock= doWhileQSqlQueryNext._if(Expressions.and( e1.callMethod(new MethodOneRelationEntityIsNull(r))
//					,
//					Expressions.not( rec.callMethod("value", QString.fromStringConstant(r.getAlias()+"__"+ r.getDestTable().getPrimaryKey().getFirstColumn().getName())).callMethod(ClsQVariant.isNull))
//					));
//			ifBlock.thenBlock().
//			_callMethodInstr(e1, new MethodOneRelationAttrSetter( entity,r, true),  parent.callStaticMethod(MethodGetFromRecord.getMethodName(foreignCls),  rec, QString.fromStringConstant(r.getAlias())));
//			//ifBlock.getIfInstr()._assign(acc, _this().callGetByRecordMethod(foreignCls, rec, QString.fromStringConstant(r.getAlias())));
////			//bCount++;
//		}
//		for(AbstractRelation r:manyRelations) {
//			ifQSqlQueryNext.addIfInstr(e1.callSetterMethodInstruction("loaded"+r.getIdentifier(), BoolExpression.TRUE)); 
//			EntityCls foreignCls = Entities.get(r.getDestTable()); 
//			if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
//				Var pkSet = ifQSqlQueryNext.thenBlock()._declare(Types.qset(foreignCls.getStructPk()), "pkSet"+r.getAlias());
//				IfBlock ifNotPkForeignIsNull= doWhileQSqlQueryNext._if(Expressions.not( rec.callMethod("value", QString.fromStringConstant(r.getAlias()+"__"+ r.getDestTable().getPrimaryKey().getFirstColumn().getName())).callMethod(ClsQVariant.isNull)));
//				Var pk = ifNotPkForeignIsNull.thenBlock()._declare(foreignCls.getStructPk(), "pk"+r.getAlias());
//				for(Column colPk : r.getDestTable().getPrimaryKey().getColumns()) {
//					ifNotPkForeignIsNull.thenBlock()._assign(
//							pk.accessAttr(colPk.getCamelCaseName()), 
//							
//							rec.callMethod("value", QString.fromStringConstant(r.getAlias()+"__"+ colPk.getName())).callMethod(EntityCls.getDatabaseMapper().getQVariantConvertMethod(colPk)));
//				}
//				
//				
////				IfBlock ifNotContains = 
//				ifNotPkForeignIsNull.thenBlock()._if(Expressions.not(pkSet.callMethod("contains", pk)))
//						.addIfInstr(pkSet.callMethodInstruction("insert", pk))
//						.addIfInstr(e1.callMethodInstruction(EntityCls.getRelatedEntityMethodName(r),parent.callStaticMethod(MethodGetFromRecord.getMethodName(foreignCls), rec, QString.fromStringConstant(r.getAlias()))));
////						.addIfInstr(e1.accessAttr(CodeUtil2.plural(r.getDestTable().getCamelCaseName())).callMethodInstruction("append",  _this().callGetByRecordMethod(foreignCls, rec, QString.fromStringConstant(r.getAlias()))));
//				
//			} else {
//				Column colPk = r.getDestTable().getPrimaryKey().getColumns().get(0);
//				Type type = EntityCls.getDatabaseMapper().columnToType(colPk);
//
//				IfBlock ifNotPkForeignIsNull = doWhileQSqlQueryNext._if(Expressions.not( rec.callMethod("value", QString.fromStringConstant(r.getAlias()+"__"+colPk.getName())).callMethod(ClsQVariant.isNull)));
//				
//				Var pkSet = ifQSqlQueryNext.thenBlock()._declare(Types.qset(type), "pkSet"+r.getAlias());
//				Var pk = ifNotPkForeignIsNull.thenBlock()._declare(
//						type, 
//						"pk"+r.getAlias(), 
//						rec.callMethod("value", QString.fromStringConstant(r.getAlias()+"__"+colPk.getName())).callMethod(EntityCls.getDatabaseMapper().getQVariantConvertMethod(colPk))
//						
//						);
//				ifNotPkForeignIsNull.thenBlock()._if(Expressions.not(pkSet.callMethod("contains", pk)))
//					.addIfInstr(pkSet.callMethodInstruction("insert", pk))
//					.addIfInstr(e1.callMethodInstruction(EntityCls.getRelatedEntityMethodName(r),parent.callStaticMethod(MethodGetFromRecord.getMethodName(foreignCls),  rec, QString.fromStringConstant(r.getAlias()))));
////						.addIfInstr(e1.accessAttr(PgCppUtil.getManyRelationDestAttrName(r))
////								.callMethodInstruction("append",  _this().callGetByRecordMethod(foreignCls,  rec, QString.fromStringConstant(r.getAlias()))));
//			}
//			
////			Var varForeach = new Var(, name);
////			doWhileQSqlQueryNext._foreach(var, collection);
////			//bCount++;
//		}
//		
//		ifQSqlQueryNext.addIfInstr(doWhileQSqlQueryNext);
//		
//		doWhileQSqlQueryNext.setCondition(ifQSqlQueryNext.getCondition());
//		}
//		_return(e1);
		
		Expression[] paramsSelect=pLazyLoad!=null ? new Expression[] {pLazyLoad} : new Expression[0];
		
		var e=parent.callStaticMethod(MethodCreateQuerySelect.getMethodName(entity),paramsSelect);
		for(Column col:entity.getTbl().getPrimaryKey().getColumns()) {
			e=e.callMethod(MethodEntityQueryWhereEquals.getMethodName(col), getParam(col.getCamelCaseName()));
		}
		if(pOrderBy!=null) {
			e=e.callMethod(MethodOrderBy.getMethodName(), pOrderBy, pOrderByDirection);
		}
		_return(e.callMethod(MethodEntityQueryFetchOne.METHOD_NAME,pSqlCon));
	}

}
