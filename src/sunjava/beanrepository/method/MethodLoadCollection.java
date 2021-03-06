package sunjava.beanrepository.method;

import sunjava.bean.BeanCls;
import sunjava.core.JavaCls;
import sunjava.core.JavaString;
import sunjava.core.Method;
import sunjava.core.Param;
import sunjava.core.Types;
import sunjava.core.expression.Expression;
import sunjava.core.expression.Var;

public class MethodLoadCollection extends Method{
	BeanCls bean;
	
	public MethodLoadCollection(Param p,BeanCls bean) {
		super(Public, Types.Void, getMethodName(bean));
//		addParam(new Param(Types.QSqlQuery, "query"));	
		//addParam(new Param(Types.qset(cls), "collection"));
		addParam(p);
		this.bean=bean;
	}

	public static String getMethodName(BeanCls bean) {
		return "loadCollection"+bean.getName();
	}
	
	protected Var getVarSqlCon() {
		JavaCls parent = (JavaCls) this.parent;
		return parent.getAttrByName("sqlCon");
	}
	
	protected Expression getByRecordExpression(BeanCls bean, Var record, JavaString alias) {
		//return new ThisBeanRepositoryExpression((BeanRepository) parent);
		return bean.callStaticMethod("getByRecord", getVarSqlCon(), record, alias);
	}
	
	@Override
	public void addImplementation() {
		_return();
//		PrimaryKey pk=bean.getTbl().getPrimaryKey();
////		Param query = getParam("query");
//		Param collection = getParam("collection");
//		
//		List<OneRelation> oneRelations = bean.getOneRelations();
//		List<OneToManyRelation> oneToManyRelations = bean.getOneToManyRelations();
//		List<ManyRelation> manyToManyRelations = bean.getManyToManyRelations();
//		ArrayList<AbstractRelation> manyRelations = new ArrayList<>();
//		manyRelations.addAll(oneToManyRelations);
//		manyRelations.addAll(manyToManyRelations);
//		
//		
////		query.callMethod("select");
//		
//		Var aSqlCon = getVarSqlCon();
//		Var sqlQuery = _declare(Types.SqlQuery, "sqlQuery",aSqlCon.callMethod("buildQuery"));
//		Type e1PkType = pk.isMultiColumn() ? bean.getStructPk() : BeanCls.getTypeMapper().columnToType( pk.getColumns().get(0));
//		
//		//ArrayList<Expression> selectFields = new ArrayList<>();
//		//selectFields.add(bean.callStaticMethod("getSelectFields",JavaString.fromStringConstant("e1")));
//		
//		List<OneRelation> relations = new ArrayList<>(oneRelations);
//		relations.addAll(oneRelations);
////		
////		//int //bCount = 2;
////		for(Relation r:relations) {
////			selectFields.add(Beans.get(r.getDestTable()).callStaticMethod("getSelectFields", JavaString.fromStringConstant(r.getAlias())));
////			//bCount++;
////		}
////		Expression exprQSqlQuery = sqlQuery.callMethod("select", Expressions.concat(QChar.fromChar(','), selectFields) )
////									.callMethod("from", JavaString.fromExpression(bean.accessStaticAttribute("TABLENAME")).concat(JavaString.fromStringConstant(" e1")));
//		
//		Expression exprQSqlQuery = sqlQuery.callMethod("select", bean.callStaticMethod("getAllSelectFields",JavaString.fromStringConstant("e1")))
//				.callMethod("from", JavaString.fromExpression(bean.callStaticMethod("getTableName")).concat(JavaString.fromStringConstant(" e1")));
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
//			exprQSqlQuery = exprQSqlQuery.callMethod("leftJoin", JavaString.fromExpression(Beans.get(r.getDestTable()).callStaticMethod("getTableName")),JavaString.fromStringConstant(r.getAlias()), JavaString.fromStringConstant(CodeUtil2.concat(joinConditions," AND ")));
//			//bCount++;
//		}
//		
//		
//		ArrayInitList init=new ArrayInitList();
//		
//		for (Column pkCol : pk.getColumns()) {
//			init.addElement(JavaString.fromStringConstant("e1."+ pkCol.getEscapedName()));
//		}
//		Var varColumns = _declare(Types.array(Types.String), "columns",init);
//		
//		
//		
//		
//		Var params= _declare(Types.arraylist(Types.SqlParam), "params");
////		_callMethodInstr(params, "reserve", collection.callMethod("size"));
//		Var varForeachBean = new Var(bean, "entity");
//		ForeachLoop foreach= _foreach(varForeachBean, collection);
//		for (Column pkCol : pk.getColumns()) {
//			
//			if(pkCol.hasOneRelation()){
//				//colPk.getRelation().getDestTable().getCamelCaseName()
//				foreach._callMethodInstr(params, "append",varForeachBean.callAttrGetter(PgCppUtil.getOneRelationDestAttrName(pkCol.getOneRelation())).callMethod("get"+pkCol.getOneRelationMappedColumn().getUc1stCamelCaseName()) ); 
//			}else{
//				foreach._callMethodInstr(params, "append",varForeachBean.callAttrGetter(pkCol.getCamelCaseName()));	
//			}
//			
////			foreach._callMethodInstr(params, "append", varForeachBean.callAttrGetter(pkCol.getCamelCaseName()));
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
//		Var e1Map =  ifInstr._declare((!manyRelations.isEmpty()) ? new ClsHashmap(e1PkType, bean.getFetchListHelperCls()) : new ClsHashSet(e1PkType), "e1Map");
//		
//		DoWhile doWhileQueryNext = ifQueryNext.getIfInstr()._doWhile();
//		doWhileQueryNext.setCondition(query.callMethod("next"));
//		Var recDoWhile =doWhileQueryNext._declare(Types.QSqlRecord, "rec",query.callMethod("record") );
//		
//		Var e1pk = null;
//		ArrayList<Expression> listForeachPkCompare = new ArrayList<>();
//		Var varIfNotE1SetContainsForeachBean = new Var(bean, "entity");
//		if (pk.isMultiColumn()) {
//			e1pk =doWhileQueryNext._declare( bean.getStructPk(), "e1pk" );
//			for(Column colPk:pk.getColumns()) {
//				doWhileQueryNext._assign(e1pk.accessAttr(colPk.getCamelCaseName()), recDoWhile.callMethod("value", JavaString.fromStringConstant("e1__"+ colPk.getName())).callMethod(BeanCls.getTypeMapper().getConvertMethod(colPk.getDbType())));
//				
//				listForeachPkCompare.add(
//						new BinaryOperatorExpression(
//								
//								(colPk.hasOneRelation() 
//									? varIfNotE1SetContainsForeachBean.callMethod( OrmUtil.getOneRelationDestAttrGetter(colPk.getOneRelation())).callMethod("get"+colPk.getOneRelationMappedColumn().getUc1stCamelCaseName())								
//									: varIfNotE1SetContainsForeachBean.callMethod("get"+colPk.getUc1stCamelCaseName())
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
//			e1pk =doWhileQueryNext._declare( BeanCls.getTypeMapper().columnToType( pk.getFirstColumn()), "e1pk", recDoWhile.callMethod("value", JavaString.fromStringConstant("e1__"+ pk.getFirstColumn().getName())).callMethod(BeanCls.getTypeMapper().getConvertMethod(pk.getFirstColumn().getDbType())));
//			listForeachPkCompare.add(
//					new BinaryOperatorExpression(
//							varIfNotE1SetContainsForeachBean.callMethod("get"+colPk.getUc1stCamelCaseName()),
//							new LibEqualsOperator(),
//							e1pk
//					));
//		}
//		
//		
//		IfBlock ifNotE1SetContains = doWhileQueryNext._if(Expressions.not(e1Map.callMethod("contains", e1pk)));
//		
//
//		ForeachLoop foreachIfNotE1SetContains= ifNotE1SetContains.getIfInstr()._foreach(varIfNotE1SetContainsForeachBean, collection);
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
////			Type beanPk=Types.getRelationForeignPrimaryKeyType(r);
////			ifNotE1SetContains.getIfInstr()._assign(structHelperIfNotE1SetContains.accessAttr(r.getAlias()+"Set"),  new NewOperator(Types.qset(beanPk)));
////			//bCount++;
////		}
//		//bCount = 2;
//		if (manyRelations.isEmpty()) {
//			ifForeachPkCompare.getIfInstr()._callMethodInstr(e1Map, "insert", e1pk );
//		} else {
//			Var structHelperIfNotE1SetContains = ifForeachPkCompare.getIfInstr()._declare(bean.getFetchListHelperCls(), "structHelper");
//			ifForeachPkCompare.getIfInstr()._assign(structHelperIfNotE1SetContains.accessAttr("e1"), varIfNotE1SetContainsForeachBean);
//			ifForeachPkCompare.getIfInstr()._callMethodInstr(e1Map, "insert", e1pk, structHelperIfNotE1SetContains );
//			
//			Var fkHelper = doWhileQueryNext._declare(bean.getFetchListHelperCls(), "fkHelper",e1Map.arrayIndex(e1pk));
//			
//			for(AbstractRelation r:manyRelations) {
//				Type beanPk=Types.getRelationForeignPrimaryKeyType(r);
//				BeanCls foreignCls = Beans.get(r.getDestTable()); 
//				Expression foreignBeanExpression = getByRecordExpression(foreignCls, recDoWhile, JavaString.fromStringConstant(r.getAlias()));
////				IfBlock ifRecValueIsNotNull = null;
//				Var foreignBean = null;				
//				
//				IfBlock ifNotPkForeignIsNull= doWhileQueryNext._if(Expressions.not( recDoWhile.callMethod("value", JavaString.fromStringConstant(r.getAlias()+"__"+ r.getDestTable().getPrimaryKey().getFirstColumn().getName())).callMethod("isNull")));
//				
//				Var pkForeign = null;
//				if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
//					pkForeign = ifNotPkForeignIsNull.getIfInstr()._declare(beanPk, "pkForeignB"+r.getAlias());
//					
//					for(Column colPk:r.getDestTable().getPrimaryKey().getColumns()) {
//						ifNotPkForeignIsNull.getIfInstr()._assign(
//								pkForeign.accessAttr(colPk.getCamelCaseName()), 
//								recDoWhile.callMethod("value", JavaString.fromStringConstant(r.getAlias()+"__"+ colPk.getName())).callMethod(BeanCls.getTypeMapper().getConvertMethod(colPk.getDbType())));
//					}
//				} else {
//					Column colPk=r.getDestTable().getPrimaryKey().getFirstColumn();
//					pkForeign = ifNotPkForeignIsNull.getIfInstr()._declare(beanPk, "pkForeignB"+r.getAlias(),recDoWhile.callMethod("value", JavaString.fromStringConstant(r.getAlias()+"__"+ r.getDestTable().getPrimaryKey().getFirstColumn().getName())).callMethod(BeanCls.getTypeMapper().getConvertMethod(colPk.getDbType())));
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
//				foreignBean =ifRecValueIsNotNull.getIfInstr()._declare(foreignBeanExpression.getType(), "foreignB"+r.getAlias(),foreignBeanExpression) ;
//				
//				ifRecValueIsNotNull.getIfInstr().addInstr(fkHelper.accessAttr("e1")
//						.callMethodInstruction(BeanCls.getRelatedBeanMethodName(r), foreignBean));
//				ifRecValueIsNotNull.getIfInstr().addInstr(
//						fkHelper.accessAttr(r.getAlias()+"Set")
//						.callMethod("insert", 
//								pkForeign
//									).asInstruction())
//					 ;
//				
//				for (OneRelation foreignOneRelation: foreignCls.getOneRelations()) {
//					if (foreignOneRelation.getDestTable().equals(bean.getTbl())) {
//						ifRecValueIsNotNull.getIfInstr().addInstr(foreignBean.callMethodInstruction("set"+r.getSourceTable().getUc1stCamelCaseName()+"Internal", fkHelper.accessAttr("e1")));
//					}
//				}
//				
//				
//				//bCount++;
//			}		
//		}
//		for(OneRelation r:oneRelations) {
//			BeanCls foreignCls = Beans.get(r.getDestTable());
//			Expression foreignBeanExpression = getByRecordExpression(foreignCls, recDoWhile, JavaString.fromStringConstant(r.getAlias()));
//			
//			IfBlock ifRelatedBeanIsNull= foreachIfNotE1SetContains.
//					_if(varForeachBean.callMethod(new MethodOneRelationBeanIsNull(r)));
//			
//			Var foreignBean =ifRelatedBeanIsNull.getIfInstr()._declare(foreignBeanExpression.getType(), "foreignB"+r.getAlias(),foreignBeanExpression) ;
//			ifRelatedBeanIsNull.getIfInstr()
//				._callMethodInstr(
//						varForeachBean ,
//						new MethodAttrSetterInternal(foreignCls,
//								bean.getAttrByName(PgCppUtil.getOneRelationDestAttrName(r)))
//						,  foreignBean);
//			
//		
//			for (OneRelation foreignOneRelation: foreignCls.getOneRelations()) {
//				if (foreignOneRelation.getDestTable().equals(bean.getTbl())) {
//					ifRelatedBeanIsNull.getIfInstr().addInstr(foreignBean.callMethodInstruction("set"+r.getSourceTable().getUc1stCamelCaseName()+"Internal", recDoWhile));
//				}
//			}
////			ifRelatedBeanIsNull.getIfInstr()._callMethodInstr(foreignBean, "setLoaded", BoolExpression.TRUE);
//			
//			//bCount++;
//		}
//		
//		foreachIfNotE1SetContains._callMethodInstr(varForeachBean, "setLoaded", BoolExpression.TRUE);
//				
//		for(Column col:bean.getTbl().getAllColumns()) {
//			try{
//				if (!col.hasOneRelation() && !col.isPartOfPk()) {
//					ifForeachPkCompare.getIfInstr().addInstr(varIfNotE1SetContainsForeachBean.callMethodInstruction("set"+ col.getUc1stCamelCaseName()+"Internal",recDoWhile.callMethod("value", JavaString.fromStringConstant("e1__"+ col.getName())).callMethod(BeanCls.getTypeMapper().getConvertMethod(col.getDbType()))));
//				}
////					_callMethodInstr(bean, "set"+col.getUc1stCamelCaseName(), getParam("record").callMethod("value", new JavaStringPlusOperatorExpression(getParam("alias"), JavaString.fromStringConstant("__"+ col.getName()))).callMethod(BeanCls.getTypeMapper().getConvertMethod(col.getDbType())));
//			} catch (Exception e) {
//				e.printStackTrace();
//				System.out.println(parent);
//			}
//		}
		
			
		
		
	}
}
