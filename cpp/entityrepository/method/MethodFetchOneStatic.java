package cpp.entityrepository.method;

//public class MethodFetchOneStatic extends Method {
//
//
//	protected BeanCls bean;
//	
//	public MethodFetchOneStatic(BeanCls bean) {
//		super(Public, bean.toSharedPtr(), getMethodName(bean) );
//		addParam(new Param(Types.Sql.toRawPointer(), "sqlCon"));
//		addParam(new Param(Types.QSqlQuery.toUniquePointer(), "query"));	
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
//	protected Expression getByRecordExpression(BeanCls bean, Var record, QString alias) {
//		//return new ThisBeanRepositoryExpression((BeanRepository) parent);
//		return Types.BeanRepository.callStaticMethod(MethodGetFromRecord.getMethodName(bean), record, alias);
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
//		//BeanCls cls = (BeanCls) parent;
//		List<OneRelation> oneRelations = bean.getOneRelations();
//		//PrimaryKey pk=bean.getTbl().getPrimaryKey();
//		
//		Expression query = getExpressionQuery();
//		//int //bCount = 2;
//		//Type e1PkType = pk.isMultiColumn() ? bean.getStructPk() : BeanCls.getDatabaseMapper().columnToType(pk.getColumns().get(0));
//		
//		ArrayList<AbstractRelation> manyRelations = new ArrayList<>();
//		
//		manyRelations.addAll(bean.getOneToManyRelations());
//		manyRelations.addAll(bean.getManyToManyRelations());
//		
//		IfBlock ifQueryNext = _if(query.callMethod("next"));
//		InstructionBlock ifInstr = ifQueryNext.getIfInstr();
//		//Var e1Map =  ifInstr._declare((!manyRelations.isEmpty()) ? new ClsQHash(e1PkType, bean.getFetchListHelperCls()) : new ClsQSet(e1PkType), "e1Map");
//		Var recDoWhile =ifInstr._declare(Types.QSqlRecord, "rec",query.callMethod("record") );
//		
///*		Var e1pk = null;
//		
//		if (pk.isMultiColumn()) {
//			e1pk =ifInstr._declare( bean.getStructPk(), "e1pk" );
//			for(Column colPk:pk.getColumns()) {
//				ifInstr._assign(e1pk.accessAttr(colPk.getCamelCaseName()), recDoWhile.callMethod("value", QString.fromStringConstant("e1__"+ colPk.getName())).callMethod(BeanCls.getDatabaseMapper().getQVariantConvertMethod(colPk)));
//			}
//			
//		} else {
//			e1pk =ifInstr._declare( BeanCls.getDatabaseMapper().columnToType(pk.getFirstColumn()), "e1pk", recDoWhile.callMethod("value", QString.fromStringConstant("e1__"+ pk.getFirstColumn().getName())).callMethod(BeanCls.getDatabaseMapper().getQVariantConvertMethod(pk.getFirstColumn().getDbType())));
//		}*/
//		
//		//IfBlock ifNotE1SetContains = ifInstr._if(Expressions.not(e1Map.callMethod("contains", e1pk)));
//		
//		
//		Var e1 = ifInstr
//				._declare(bean.toSharedPtr(), "e1", getByRecordExpression(bean, recDoWhile, QString.fromStringConstant("e1")));
//		//bCount = 2;
//		if (!manyRelations.isEmpty()) {
//			
//		//	Var structHelper = ifInstr._declare(bean.getFetchListHelperCls().toRawPointer(), "structHelper", new NewOperator(bean.getFetchListHelperCls()));
//
////			for(Relation r:manyRelations) {
////				Type beanPk=Types.getRelationForeignPrimaryKeyType(r);
////				ifInstr._assign(structHelper.accessAttr(r.getAlias()+"Set"),  new CreateObjectExpression(Types.qset(beanPk)));
////				//bCount++;
////			}
//
//			ifInstr._assignInstruction(recDoWhile, query.callMethod("record"));
//			
//		//	Var fkHelper = ifInstr._declare(bean.getFetchListHelperCls().toRef(), "fkHelper",e1Map.arrayIndex(e1pk));
//			
//		//	Var structHelperIfNotE1SetContains = ifNotE1SetContains.getIfInstr()._declare(bean.getFetchListHelperCls(), "structHelper");
//		//	ifNotE1SetContains.getIfInstr()._assign(structHelperIfNotE1SetContains.accessAttr("e1"), e1);
////			//bCount = 2;
////			for(Relation r:manyRelations) {
////				Type beanPk=Types.getRelationForeignPrimaryKeyType(r);
////				ifNotE1SetContains.getIfInstr()._assign(structHelperIfNotE1SetContains.accessAttr(r.getAlias()+"Set"),  new CreateObjectExpression(Types.qset(beanPk)));
////				//bCount++;
////			}
//			
//			//ifNotE1SetContains.getIfInstr()._callMethodInstr(e1Map, "insert", e1pk, structHelperIfNotE1SetContains );
//			Var fkHelper = ifInstr._declare(bean.getFetchListHelperCls(), "fkHelper");		
//			
//			for(AbstractRelation r:manyRelations) {
//				Type beanPk=Types.getRelationForeignPrimaryKeyType(r);
//				BeanCls foreignCls = Beans.get(r.getDestTable()); 
//				Expression foreignBeanExpression = getByRecordExpression(foreignCls, recDoWhile, QString.fromStringConstant(r.getAlias()));
////				IfBlock ifRecValueIsNotNull = null;
////				Var foreignBean = null;				
//				
//				IfBlock ifNotPkForeignIsNull= ifInstr._if(Expressions.not( recDoWhile.callMethod("value", QString.fromStringConstant(r.getAlias()+"__"+ r.getDestTable().getPrimaryKey().getFirstColumn().getName())).callMethod("isNull")));
//				
//				Var pkForeign = null;
//				if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
//					pkForeign = ifNotPkForeignIsNull.getIfInstr()._declare(beanPk, "pkForeignB"+r.getAlias());
//					
//					for(Column colPk:r.getDestTable().getPrimaryKey().getColumns()) {
//						ifNotPkForeignIsNull.getIfInstr()._assign(
//								pkForeign.accessAttr(colPk.getCamelCaseName()), 
//								recDoWhile.callMethod("value",
//										QString.fromStringConstant(r.getAlias()+"__"+ colPk.getName())).callMethod(BeanCls.getDatabaseMapper().getQVariantConvertMethod(colPk))
//								);
//					}
//				} else {
//					Column colPk=r.getDestTable().getPrimaryKey().getFirstColumn();
//					pkForeign = ifNotPkForeignIsNull.getIfInstr()._declare(beanPk, "pkForeignB"+r.getAlias(),QString.fromStringConstant(r.getAlias()+"__"+ colPk.getName()).callMethod(BeanCls.getDatabaseMapper().getQVariantConvertMethod(colPk)));
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
//				Var foreignBean =ifRecValueIsNotNull.getIfInstr()._declare(foreignBeanExpression.getType(), "foreignB"+r.getAlias(),foreignBeanExpression) ;
//				//					
////					
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
//				//ifRecValueIsNotNull.getIfInstr()._callMethodInstr(foreignBean, "setLoaded", BoolExpression.TRUE);
//				
//				//bCount++;
//			}
//			
//
//			
//		} else {
//			/* manyRelations.isEmpty() */
//		//	ifNotE1SetContains.getIfInstr()._callMethodInstr(e1Map, "insert", e1pk);
//		}
//		for(OneRelation r:oneRelations) {
//			BeanCls foreignCls = Beans.get(r.getDestTable());
//			Expression foreignBeanExpression = getByRecordExpression(foreignCls, recDoWhile, QString.fromStringConstant(r.getAlias()));
//			
//			IfBlock ifRelatedBeanIsNull= ifInstr.
//					_if(e1.callMethod(new MethodOneRelationBeanIsNull(r)));
//			
//			Var foreignBean =ifRelatedBeanIsNull.getIfInstr()._declare(foreignBeanExpression.getType(), "foreignB"+r.getAlias(),foreignBeanExpression) ;
//			ifRelatedBeanIsNull.getIfInstr()
//				._callMethodInstr(
//						e1 ,
//						new MethodAttrSetterInternal(foreignCls,
//								bean.getAttrByName(PgCppUtil.getOneRelationDestAttrName(r)))
//						,  foreignBean);
//			
//		
//			for (OneRelation foreignOneRelation: foreignCls.getOneRelations()) {
//				if (foreignOneRelation.getDestTable().equals(bean.getTbl())) {
//					ifRelatedBeanIsNull.getIfInstr().addInstr(foreignBean.callMethodInstruction("set"+r.getSourceTable().getUc1stCamelCaseName()+"Internal", e1));
//				}
//			}
////			ifRelatedBeanIsNull.getIfInstr()._callMethodInstr(foreignBean, "setLoaded", BoolExpression.TRUE);
//			
//			//bCount++;
//		}
//		ifInstr._callMethodInstr(e1, "setLoaded", BoolExpression.TRUE);
//		ifInstr._return(e1);
//		_return(Expressions.Nullptr);
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
//		return "fetchOne"+bean.getName()+"Static";
//	}
//}
