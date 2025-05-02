package sunjava.entityrepository.method;

import sunjava.core.JavaString;
import sunjava.core.Method;
import sunjava.core.Param;
import sunjava.core.Types;
import sunjava.core.expression.Expression;
import sunjava.core.expression.Var;
import sunjava.entity.EntityCls;

public class MethodFetchOneStatic extends Method {


	protected EntityCls entity;
	
	public MethodFetchOneStatic(EntityCls entity) {
		super(Public, entity, getMethodName(entity) );
		addParam(new Param(Types.Connection, "sqlCon"));
//		addParam(new Param(Types.QSqlQuery, "query"));	
	
		setStatic(true);
		this.entity = entity;
	}
	
//	@Override
//	public ThisEntityRepositoryExpression getByRecordExpression() {
		//return new ThisEntityRepositoryExpression((EntityRepository) parent);
//	}

	protected Expression getByRecordExpression(EntityCls entity, Var record, JavaString alias) {
		//return new ThisEntityRepositoryExpression((EntityRepository) parent);
		return entity.callStaticMethod("getByRecord", getVarSqlCon(), record, alias);
	}
	
	protected Expression getExpressionQuery() {
		return  getParam("query");
	}
	
	protected Var getVarSqlCon() {
		return getParam("sqlCon");
	}
	
	@Override
	public void addImplementation() {
//		//EntityCls cls = (EntityCls) parent;
//		List<OneRelation> oneRelations = entity.getOneRelations();
//		//PrimaryKey pk=entity.getTbl().getPrimaryKey();
//		
//		Expression query = getExpressionQuery();
//		//int //bCount = 2;
//		//Type e1PkType = pk.isMultiColumn() ? entity.getStructPk() : pk.getColumns().get(0).toType();
//		
//		ArrayList<AbstractRelation> manyRelations = new ArrayList<>();
//		
//		manyRelations.addAll(entity.getOneToManyRelations());
//		manyRelations.addAll(entity.getManyToManyRelations());
//		
//		IfBlock ifQueryNext = _if(query.callMethod("next"));
//		InstructionBlock ifInstr = ifQueryNext.getIfInstr();
//		//Var e1Map =  ifInstr._declare((!manyRelations.isEmpty()) ? new ClsQHash(e1PkType, entity.getFetchListHelperCls()) : new ClsQSet(e1PkType), "e1Map");
//		Var recDoWhile =ifInstr._declare(Types.QSqlRecord, "rec",query.callMethod("record") );
//		
///*		Var e1pk = null;
//		
//		if (pk.isMultiColumn()) {
//			e1pk =ifInstr._declare( entity.getStructPk(), "e1pk" );
//			for(Column colPk:pk.getColumns()) {
//				ifInstr._assign(e1pk.accessAttr(colPk.getCamelCaseName()), recDoWhile.callMethod("value", JavaString.fromStringConstant("e1__"+ colPk.getName())).callMethod(EntityCls.getTypeMapper().getConvertMethod(colPk.getDbType())));
//			}
//			
//		} else {
//			e1pk =ifInstr._declare( pk.getFirstColumn().toType(), "e1pk", recDoWhile.callMethod("value", JavaString.fromStringConstant("e1__"+ pk.getFirstColumn().getName())).callMethod(EntityCls.getTypeMapper().getConvertMethod(pk.getFirstColumn().getDbType())));
//		}*/
//		
//		//IfBlock ifNotE1SetContains = ifInstr._if(Expressions.not(e1Map.callMethod("contains", e1pk)));
//		
//		
//		Var e1 = ifInstr
//				._declare(entity, "e1", getByRecordExpression(entity, recDoWhile, JavaString.fromStringConstant("e1")));
//		//bCount = 2;
//		if (!manyRelations.isEmpty()) {
//			
//		//	Var structHelper = ifInstr._declare(entity.getFetchListHelperCls(), "structHelper", new NewOperator(entity.getFetchListHelperCls()));
//
////			for(Relation r:manyRelations) {
////				Type entityPk=Types.getRelationForeignPrimaryKeyType(r);
////				ifInstr._assign(structHelper.accessAttr(r.getAlias()+"Set"),  new NewOperator(Types.qset(entityPk)));
////				//bCount++;
////			}
//
//			ifInstr._assignInstruction(recDoWhile, query.callMethod("record"));
//			
//		//	Var fkHelper = ifInstr._declare(entity.getFetchListHelperCls(), "fkHelper",e1Map.arrayIndex(e1pk));
//			
//		//	Var structHelperIfNotE1SetContains = ifNotE1SetContains.getIfInstr()._declare(entity.getFetchListHelperCls(), "structHelper");
//		//	ifNotE1SetContains.getIfInstr()._assign(structHelperIfNotE1SetContains.accessAttr("e1"), e1);
////			//bCount = 2;
////			for(Relation r:manyRelations) {
////				Type entityPk=Types.getRelationForeignPrimaryKeyType(r);
////				ifNotE1SetContains.getIfInstr()._assign(structHelperIfNotE1SetContains.accessAttr(r.getAlias()+"Set"),  new NewOperator(Types.qset(entityPk)));
////				//bCount++;
////			}
//			
//			//ifNotE1SetContains.getIfInstr()._callMethodInstr(e1Map, "insert", e1pk, structHelperIfNotE1SetContains );
//			Var fkHelper = ifInstr._declare(entity.getFetchListHelperCls(), "fkHelper");		
//			
//			for(AbstractRelation r:manyRelations) {
//				Type entityPk=Types.getRelationForeignPrimaryKeyType(r);
//				EntityCls foreignCls = Entities.get(r.getDestTable()); 
//				Expression foreignEntityExpression = getByRecordExpression(foreignCls, recDoWhile, JavaString.fromStringConstant(r.getAlias()));
////				IfBlock ifRecValueIsNotNull = null;
////				Var foreignEntity = null;				
//				
//				IfBlock ifNotPkForeignIsNull= ifInstr._if(Expressions.not( recDoWhile.callMethod("value", JavaString.fromStringConstant(r.getAlias()+"__"+ r.getDestTable().getPrimaryKey().getFirstColumn().getName())).callMethod("isNull")));
//				
//				Var pkForeign = null;
//				if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
//					pkForeign = ifNotPkForeignIsNull.getIfInstr()._declare(entityPk, "pkForeignB"+r.getAlias());
//					
//					for(Column colPk:r.getDestTable().getPrimaryKey().getColumns()) {
//						ifNotPkForeignIsNull.getIfInstr()._assign(
//								pkForeign.accessAttr(colPk.getCamelCaseName()), 
//								recDoWhile.callMethod("value",
//										JavaString.fromStringConstant(r.getAlias()+"__"+ colPk.getName())).callMethod(EntityCls.getTypeMapper().getConvertMethod(colPk.getDbType()))
//								);
//					}
//				} else {
//					Column colPk=r.getDestTable().getPrimaryKey().getFirstColumn();
//					pkForeign = ifNotPkForeignIsNull.getIfInstr()._declare(entityPk, "pkForeignB"+r.getAlias(),JavaString.fromStringConstant(r.getAlias()+"__"+ colPk.getName()).callMethod(EntityCls.getTypeMapper().getConvertMethod(colPk.getDbType())));
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
//				Var foreignEntity =ifRecValueIsNotNull.getIfInstr()._declare(foreignEntityExpression.getType(), "foreignB"+r.getAlias(),foreignEntityExpression) ;
//				//					
////					
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
//				//ifRecValueIsNotNull.getIfInstr()._callMethodInstr(foreignEntity, "setLoaded", BoolExpression.TRUE);
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
//			EntityCls foreignCls = Entities.get(r.getDestTable());
//			Expression foreignEntityExpression = getByRecordExpression(foreignCls, recDoWhile, JavaString.fromStringConstant(r.getAlias()));
//			
//			IfBlock ifRelatedEntityIsNull= ifInstr.
//					_if(e1.callMethod(new MethodOneRelationEntityIsNull(r)));
//			
//			Var foreignEntity =ifRelatedEntityIsNull.getIfInstr()._declare(foreignEntityExpression.getType(), "foreignB"+r.getAlias(),foreignEntityExpression) ;
//			ifRelatedEntityIsNull.getIfInstr()
//				._callMethodInstr(
//						e1 ,
//						new MethodAttrSetterInternal(foreignCls,
//								entity.getAttrByName(PgCppUtil.getOneRelationDestAttrName(r)))
//						,  foreignEntity);
//			
//		
//			for (OneRelation foreignOneRelation: foreignCls.getOneRelations()) {
//				if (foreignOneRelation.getDestTable().equals(entity.getTbl())) {
//					ifRelatedEntityIsNull.getIfInstr().addInstr(foreignEntity.callMethodInstruction("set"+r.getSourceTable().getUc1stCamelCaseName()+"Internal", e1));
//				}
//			}
////			ifRelatedEntityIsNull.getIfInstr()._callMethodInstr(foreignEntity, "setLoaded", BoolExpression.TRUE);
//			
//			//bCount++;
//		}
//		ifInstr._callMethodInstr(e1, "setLoaded", BoolExpression.TRUE);
//		ifInstr._return(e1);
//		_return(Expressions.Null);
		
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	public static String getMethodName(EntityCls entity) {
		return "fetchOne"+entity.getName()+"Static";
	}
}
