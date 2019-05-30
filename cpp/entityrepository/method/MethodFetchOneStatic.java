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
//		//Type b1PkType = pk.isMultiColumn() ? bean.getStructPk() : BeanCls.getDatabaseMapper().columnToType(pk.getColumns().get(0));
//		
//		ArrayList<AbstractRelation> manyRelations = new ArrayList<>();
//		
//		manyRelations.addAll(bean.getOneToManyRelations());
//		manyRelations.addAll(bean.getManyToManyRelations());
//		
//		IfBlock ifQueryNext = _if(query.callMethod("next"));
//		InstructionBlock ifInstr = ifQueryNext.getIfInstr();
//		//Var b1Map =  ifInstr._declare((!manyRelations.isEmpty()) ? new ClsQHash(b1PkType, bean.getFetchListHelperCls()) : new ClsQSet(b1PkType), "b1Map");
//		Var recDoWhile =ifInstr._declare(Types.QSqlRecord, "rec",query.callMethod("record") );
//		
///*		Var b1pk = null;
//		
//		if (pk.isMultiColumn()) {
//			b1pk =ifInstr._declare( bean.getStructPk(), "b1pk" );
//			for(Column colPk:pk.getColumns()) {
//				ifInstr._assign(b1pk.accessAttr(colPk.getCamelCaseName()), recDoWhile.callMethod("value", QString.fromStringConstant("b1__"+ colPk.getName())).callMethod(BeanCls.getDatabaseMapper().getQVariantConvertMethod(colPk)));
//			}
//			
//		} else {
//			b1pk =ifInstr._declare( BeanCls.getDatabaseMapper().columnToType(pk.getFirstColumn()), "b1pk", recDoWhile.callMethod("value", QString.fromStringConstant("b1__"+ pk.getFirstColumn().getName())).callMethod(BeanCls.getDatabaseMapper().getQVariantConvertMethod(pk.getFirstColumn().getDbType())));
//		}*/
//		
//		//IfBlock ifNotB1SetContains = ifInstr._if(Expressions.not(b1Map.callMethod("contains", b1pk)));
//		
//		
//		Var b1 = ifInstr
//				._declare(bean.toSharedPtr(), "b1", getByRecordExpression(bean, recDoWhile, QString.fromStringConstant("b1")));
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
//		//	Var fkHelper = ifInstr._declare(bean.getFetchListHelperCls().toRef(), "fkHelper",b1Map.arrayIndex(b1pk));
//			
//		//	Var structHelperIfNotB1SetContains = ifNotB1SetContains.getIfInstr()._declare(bean.getFetchListHelperCls(), "structHelper");
//		//	ifNotB1SetContains.getIfInstr()._assign(structHelperIfNotB1SetContains.accessAttr("b1"), b1);
////			//bCount = 2;
////			for(Relation r:manyRelations) {
////				Type beanPk=Types.getRelationForeignPrimaryKeyType(r);
////				ifNotB1SetContains.getIfInstr()._assign(structHelperIfNotB1SetContains.accessAttr(r.getAlias()+"Set"),  new CreateObjectExpression(Types.qset(beanPk)));
////				//bCount++;
////			}
//			
//			//ifNotB1SetContains.getIfInstr()._callMethodInstr(b1Map, "insert", b1pk, structHelperIfNotB1SetContains );
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
//				ifRecValueIsNotNull.getIfInstr().addInstr(fkHelper.accessAttr("b1")
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
//						ifRecValueIsNotNull.getIfInstr().addInstr(foreignBean.callMethodInstruction("set"+r.getSourceTable().getUc1stCamelCaseName()+"Internal", fkHelper.accessAttr("b1")));
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
//		//	ifNotB1SetContains.getIfInstr()._callMethodInstr(b1Map, "insert", b1pk);
//		}
//		for(OneRelation r:oneRelations) {
//			BeanCls foreignCls = Beans.get(r.getDestTable());
//			Expression foreignBeanExpression = getByRecordExpression(foreignCls, recDoWhile, QString.fromStringConstant(r.getAlias()));
//			
//			IfBlock ifRelatedBeanIsNull= ifInstr.
//					_if(b1.callMethod(new MethodOneRelationBeanIsNull(r)));
//			
//			Var foreignBean =ifRelatedBeanIsNull.getIfInstr()._declare(foreignBeanExpression.getType(), "foreignB"+r.getAlias(),foreignBeanExpression) ;
//			ifRelatedBeanIsNull.getIfInstr()
//				._callMethodInstr(
//						b1 ,
//						new MethodAttrSetterInternal(foreignCls,
//								bean.getAttrByName(PgCppUtil.getOneRelationDestAttrName(r)))
//						,  foreignBean);
//			
//		
//			for (OneRelation foreignOneRelation: foreignCls.getOneRelations()) {
//				if (foreignOneRelation.getDestTable().equals(bean.getTbl())) {
//					ifRelatedBeanIsNull.getIfInstr().addInstr(foreignBean.callMethodInstruction("set"+r.getSourceTable().getUc1stCamelCaseName()+"Internal", b1));
//				}
//			}
////			ifRelatedBeanIsNull.getIfInstr()._callMethodInstr(foreignBean, "setLoaded", BoolExpression.TRUE);
//			
//			//bCount++;
//		}
//		ifInstr._callMethodInstr(b1, "setLoaded", BoolExpression.TRUE);
//		ifInstr._return(b1);
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
