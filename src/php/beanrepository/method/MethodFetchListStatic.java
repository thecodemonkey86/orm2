package php.beanrepository.method;
//
//import php.Types;
//import php.cls.Method;
//import php.cls.Param;
//import php.cls.bean.BeanCls;
//import php.cls.expression.Expression;
//
//import php.cls.expression.Var;
//
//public class MethodFetchListStatic extends Method {
//
//
//	protected BeanCls bean;
//	
//	public MethodFetchListStatic(BeanCls bean) {
//		super(Public, Types.array(bean), getMethodName(bean) );
//		addParam(new Param(Types.mysqli, "sqlCon"));
////		addParam(new Param(Types.QSqlQuery, "query"));	
//	
//		setStatic(true);
//		this.bean = bean;
//	}
//	
////	@Override
////	public ThisBeanRepositoryExpression getByRecordExpression() {
//		//return new ThisBeanRepositoryExpression((BeanRepository) parent);
////	}
//
//	protected Expression getByRecordExpression(BeanCls bean, Var record, Expression alias) {
//		//return new ThisBeanRepositoryExpression((BeanRepository) parent);
//		return bean.callStaticMethod("getByRecord", getVarSqlCon(), record, alias);
//	}
//	
//	protected Expression getExpressionQuery() {
//		return  getParam("query");
//	}
//	
//	protected Var getVarSqlCon() {
//		return getParam("sqlCon");
//	}
//	
//	@Override
//	public void addImplementation() {
////		//BeanCls cls = (BeanCls) parent;
////		List<OneRelation> oneRelations = bean.getOneRelations();
////		PrimaryKey pk=bean.getTbl().getPrimaryKey();
////		
////		Var result = _declare(returnType, "result");
////		Expression query = getExpressionQuery();
////		//int //bCount = 2;
////		Type b1PkType = pk.isMultiColumn() ? bean.getStructPk() : BeanCls.getTypeMapper().columnToType(pk.getFirstColumn());
////		
////		ArrayList<AbstractRelation> manyRelations = new ArrayList<>();
////		
////		manyRelations.addAll(bean.getOneToManyRelations());
////		manyRelations.addAll(bean.getManyToManyRelations());
////		
////		IfBlock ifQueryNext = _if(query.callMethod("next"));
////		InstructionBlock ifInstr = ifQueryNext.getIfInstr();
////		Var b1Map =  ifInstr._declare((!manyRelations.isEmpty()) ? new ClsHashmap(b1PkType, bean.getFetchListHelperCls()) : new ClsHashSet(b1PkType), "b1Map");
////		
////		DoWhile doWhileQueryNext = ifQueryNext.getIfInstr()._doWhile();
////		doWhileQueryNext.setCondition(query.callMethod("next"));
////		Var recDoWhile =doWhileQueryNext._declare(Types.QSqlRecord, "rec",query.callMethod("record") );
////		
////		Var b1pk = null;
////		
////		if (pk.isMultiColumn()) {
////			b1pk =doWhileQueryNext._declare( bean.getStructPk(), "b1pk" );
////			for(Column colPk:pk.getColumns()) {
////				doWhileQueryNext._assign(b1pk.accessAttr(colPk.getCamelCaseName()), recDoWhile.callMethod("value", PhpString.fromStringConstant("b1__"+ colPk.getName())).callMethod(BeanCls.getTypeMapper().getConvertMethod(colPk.getDbType())));
////			}
////			
////		} else {
////			b1pk =doWhileQueryNext._declare( BeanCls.getTypeMapper().columnToType( pk.getFirstColumn()), "b1pk", recDoWhile.callMethod("value", PhpString.fromStringConstant("b1__"+ pk.getFirstColumn().getName())).callMethod(BeanCls.getTypeMapper().getConvertMethod(pk.getFirstColumn().getDbType())));
////		}
////		
////		IfBlock ifNotB1SetContains = doWhileQueryNext._if(Expressions.not(b1Map.callMethod("contains", b1pk)));
////		
////		
////		Var b1DoWhile = ifNotB1SetContains.getIfInstr()
////				._declare(bean, "b1", getByRecordExpression(bean, recDoWhile, PhpString.fromStringConstant("b1")));
////		//bCount = 2;
////		if (!manyRelations.isEmpty()) {
////			
////			//Var structHelper = ifInstr._declare(bean.getFetchListHelperCls(), "structHelper", new NewOperator(bean.getFetchListHelperCls()));
////
//////			for(Relation r:manyRelations) {
//////				Type beanPk=Types.getRelationForeignPrimaryKeyType(r);
//////				ifInstr._assign(structHelper.accessAttr(r.getAlias()+"Set"),  new NewOperator(Types.qset(beanPk)));
//////				//bCount++;
//////			}
////
////			doWhileQueryNext._assignInstruction(recDoWhile, query.callMethod("record"));
////			
////			Var fkHelper = doWhileQueryNext._declare(bean.getFetchListHelperCls(), "fkHelper",b1Map.arrayIndex(b1pk));
////			
////			Var structHelperIfNotB1SetContains = ifNotB1SetContains.getIfInstr()._declare(bean.getFetchListHelperCls(), "structHelper");
////			ifNotB1SetContains.getIfInstr()._assign(structHelperIfNotB1SetContains.accessAttr("b1"), b1DoWhile);
//////			//bCount = 2;
//////			for(Relation r:manyRelations) {
//////				Type beanPk=Types.getRelationForeignPrimaryKeyType(r);
//////				ifNotB1SetContains.getIfInstr()._assign(structHelperIfNotB1SetContains.accessAttr(r.getAlias()+"Set"),  new NewOperator(Types.qset(beanPk)));
//////				//bCount++;
//////			}
////			
////			ifNotB1SetContains.getIfInstr()._callMethodInstr(b1Map, "insert", b1pk, structHelperIfNotB1SetContains );
////					
////			
////			for(AbstractRelation r:manyRelations) {
////				Type beanPk=Types.getRelationForeignPrimaryKeyType(r);
////				BeanCls foreignCls = Beans.get(r.getDestTable()); 
////				Expression foreignBeanExpression = getByRecordExpression(foreignCls, recDoWhile, PhpString.fromStringConstant(r.getAlias()));
//////				IfBlock ifRecValueIsNotNull = null;
////				Var foreignBean = null;				
////				
////				IfBlock ifNotPkForeignIsNull= doWhileQueryNext._if(Expressions.not( recDoWhile.callMethod("value", PhpString.fromStringConstant(r.getAlias()+"__"+ r.getDestTable().getPrimaryKey().getFirstColumn().getName())).callMethod("isNull")));
////				
////				Var pkForeign = null;
////				if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
////					pkForeign = ifNotPkForeignIsNull.getIfInstr()._declare(beanPk, "pkForeignB"+r.getAlias());
////					
////					for(Column colPk:r.getDestTable().getPrimaryKey().getColumns()) {
////						ifNotPkForeignIsNull.getIfInstr()._assign(
////								pkForeign.accessAttr(colPk.getCamelCaseName()), 
////								recDoWhile.callMethod("value",
////										PhpString.fromStringConstant(r.getAlias()+"__"+ colPk.getName())).callMethod(BeanCls.getTypeMapper().getConvertMethod(colPk.getDbType()))
////								);
////					}
////				} else {
////					Column colPk=r.getDestTable().getPrimaryKey().getFirstColumn();
////					pkForeign = ifNotPkForeignIsNull.getIfInstr()._declare(beanPk, "pkForeignB"+r.getAlias(),PhpString.fromStringConstant(r.getAlias()+"__"+ colPk.getName()).callMethod(BeanCls.getTypeMapper().getConvertMethod(colPk.getDbType())));
////				}
////				IfBlock ifRecValueIsNotNull = ifNotPkForeignIsNull.getIfInstr()._if(
////						Expressions.not(fkHelper
////										.accessAttr(r.getAlias()+"Set")
////										.callMethod("contains",
////												pkForeign
////												
////												))
////								
////						
////						
////					);
////				foreignBean =ifRecValueIsNotNull.getIfInstr()._declare(foreignBeanExpression.getType(), "foreignB"+r.getAlias(),foreignBeanExpression) ;
////				
////								
////				ifRecValueIsNotNull.getIfInstr().addInstr(fkHelper.accessAttr("b1")
////						.callMethodInstruction(BeanCls.getRelatedBeanMethodName(r), foreignBean));
////				ifRecValueIsNotNull.getIfInstr().addInstr(
////						fkHelper.accessAttr(r.getAlias()+"Set")
////						.callMethod("insert", 
////								pkForeign
////									).asInstruction())
////					 ;
////				
////				for (OneRelation foreignOneRelation: foreignCls.getOneRelations()) {
////					if (foreignOneRelation.getDestTable().equals(bean.getTbl())) {
////						ifRecValueIsNotNull.getIfInstr().addInstr(foreignBean.callMethodInstruction("set"+r.getSourceTable().getUc1stCamelCaseName()+"Internal", fkHelper.accessAttr("b1")));
////					}
////				}
////				//ifRecValueIsNotNull.getIfInstr()._callMethodInstr(foreignBean, "setLoaded", BoolExpression.TRUE);
////				
////				//bCount++;
////			}
////			
////
////			
////		} else {
////			/* manyRelations.isEmpty() */
////			ifNotB1SetContains.getIfInstr()._callMethodInstr(b1Map, "insert", b1pk);
////		}
////		for(OneRelation r:oneRelations) {
////			BeanCls foreignCls = Beans.get(r.getDestTable());
////			Expression foreignBeanExpression = getByRecordExpression(foreignCls, recDoWhile, PhpString.fromStringConstant(r.getAlias()));
////			
////			IfBlock ifRelatedBeanIsNull= ifNotB1SetContains.getIfInstr().
////					_if(b1DoWhile.callMethod(new MethodOneRelationBeanIsNull(r)));
////			
////			Var foreignBean =ifRelatedBeanIsNull.getIfInstr()._declare(foreignBeanExpression.getType(), "foreignB"+r.getAlias(),foreignBeanExpression) ;
////			ifRelatedBeanIsNull.getIfInstr()
////				._callMethodInstr(
////						b1DoWhile ,
////						new MethodAttrSetterInternal(foreignCls,
////								bean.getAttrByName(PgCppUtil.getOneRelationDestAttrName(r)))
////						,  foreignBean);
////			
////		
////			for (OneRelation foreignOneRelation: foreignCls.getOneRelations()) {
////				if (foreignOneRelation.getDestTable().equals(bean.getTbl())) {
////					ifRelatedBeanIsNull.getIfInstr().addInstr(foreignBean.callMethodInstruction("set"+r.getSourceTable().getUc1stCamelCaseName()+"Internal", b1DoWhile));
////				}
////			}
//////			ifRelatedBeanIsNull.getIfInstr()._callMethodInstr(foreignBean, "setLoaded", BoolExpression.TRUE);
////			
////			//bCount++;
////		}
////		ifNotB1SetContains.getIfInstr()._callMethodInstr(b1DoWhile, "setLoaded", BoolExpression.TRUE);
////		ifNotB1SetContains.getIfInstr()._callMethodInstr(result, "append", b1DoWhile);
////		_return(result);
//		
//	}
//	
//	@Override
//	public String toString() {
//		// TODO Auto-generated method stub
//		return super.toString();
//	}
//
//	public static String getMethodName(BeanCls bean) {
//		return "fetchList"+bean.getName()+"Static";
//	}
//}
