package cpp.entityrepository.method;

import java.util.ArrayList;
import java.util.List;

import codegen.CodeUtil;
import util.CodeUtil2;
import util.pg.PgCppUtil;
import cpp.Types;
import cpp.CoreTypes;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.Type;
import cpp.core.expression.BinaryOperatorExpression;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.Expression;
import cpp.core.expression.Expressions;
import cpp.core.expression.QStringInitList;
import cpp.core.expression.Var;
import cpp.core.instruction.DoWhile;
import cpp.core.instruction.ForeachLoop;
import cpp.core.instruction.IfBlock;
import cpp.core.instruction.InstructionBlock;
import cpp.entity.Entities;
import cpp.entity.EntityCls;
import cpp.entity.method.MethodAttrSetterInternal;
import cpp.entity.method.MethodOneRelationEntityIsNull;
import cpp.lib.ClsQHash;
import cpp.lib.ClsQSet;
import cpp.lib.ClsQVariant;
import cpp.lib.LibEqualsOperator;
import cpp.orm.OrmUtil;
import cpp.util.ClsDbPool;
import database.column.Column;
import database.relation.AbstractRelation;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import database.relation.PrimaryKey;

public class MethodLoadCollection extends Method{
	protected EntityCls bean;
	protected Param pSqlCon;
	
	public MethodLoadCollection(Param p,EntityCls bean) {
		super(Public, Types.Void, "loadCollection");
//		addParam(new Param(Types.QSqlQuery.toSharedPtr(), "query"));	
		//addParam(new Param(Types.qset(cls.toSharedPtr()).toRawPointer(), "collection"));
		addParam(p);
		this.bean=bean;
		pSqlCon = addParam(Types.QSqlDatabase.toConstRef(),"sqlCon",ClsDbPool.instance.callStaticMethod(ClsDbPool.getDatabase));
		setStatic(true);
	}

	
	protected Var getVarSqlCon() {
		return this.parent.getAttrByName("sqlCon");
	}
	
	protected Expression getByRecordExpression(EntityCls bean, Var record, QString alias) {
		//return new ThisBeanRepositoryExpression((BeanRepository) parent);
		return parent.callStaticMethod(MethodGetFromRecord.getMethodName(bean),  record, alias);
	}
	
	@Override
	public void addImplementation() {
		PrimaryKey pk=bean.getTbl().getPrimaryKey();
//		Param query = getParam("query");
		Param collection = getParam("collection");
		
		List<OneRelation> oneRelations = bean.getOneRelations();
		List<OneToManyRelation> oneToManyRelations = bean.getOneToManyRelations();
		List<ManyRelation> manyToManyRelations = bean.getManyToManyRelations();
		ArrayList<AbstractRelation> manyRelations = new ArrayList<>();
		manyRelations.addAll(oneToManyRelations);
		manyRelations.addAll(manyToManyRelations);
		
		
//		query.callMethod("select");
		
		Var sqlQuery = _declareInitConstructor( EntityCls.getDatabaseMapper().getSqlQueryType(),"sqlQuery");
		
		Type e1PkType = pk.isMultiColumn() ? bean.getStructPk() : EntityCls.getDatabaseMapper().columnToType(pk.getColumns().get(0));
		
		//ArrayList<Expression> selectFields = new ArrayList<>();
		//selectFields.add(bean.callStaticMethod("getSelectFields",QString.fromStringConstant("e1")));
		
		List<OneRelation> relations = new ArrayList<>(oneRelations);
		relations.addAll(oneRelations);
//		
//		//int //bCount = 2;
//		for(Relation r:relations) {
//			selectFields.add(Beans.get(r.getDestTable()).callStaticMethod("getSelectFields", QString.fromStringConstant(r.getAlias())));
//			//bCount++;
//		}
//		Expression exprQSqlQuery = sqlQuery.callMethod("select", Expressions.concat(QChar.fromChar(','), selectFields) )
//									.callMethod("from", QString.fromExpression(bean.accessStaticAttribute("TABLENAME")).concat(QString.fromStringConstant(" e1")));
		
		Expression exprQSqlQuery = sqlQuery.callMethod("select", bean.callStaticMethod("getAllSelectFields",QString.fromStringConstant("e1")))
				.callMethod("from", QString.fromExpression(bean.callStaticMethod("getTableName")).concat(QString.fromStringConstant(" e1")));
		
		//int //bCount = 2;
		
		for(OneRelation r:relations) {
			ArrayList<String> joinConditions=new ArrayList<>();
			for(int i=0;i<r.getColumnCount();i++) {
				joinConditions.add(CodeUtil.sp("e1."+r.getColumns(i).getValue1().getEscapedName(),'=',r.getAlias()+"."+ r.getColumns(i).getValue2().getEscapedName()));
			}
			
			exprQSqlQuery = exprQSqlQuery.callMethod("leftJoin", QString.fromExpression(Entities.get(r.getDestTable()).callStaticMethod("getTableName")),QString.fromStringConstant(r.getAlias()), QString.fromStringConstant(CodeUtil2.concat(joinConditions," AND ")));
			//bCount++;
		}
		
		
		QStringInitList init=new QStringInitList();
		
		for (Column pkCol : pk.getColumns()) {
			init.addString(QString.fromStringConstant("e1."+ pkCol.getEscapedName()));
		}
		Var varColumns = _declare(Types.QStringList, "columns",init);
		
		
		
		
		Var params= _declare(CoreTypes.QVariantList, "params");
		_callMethodInstr(params, "reserve", collection.callMethod("size"));
		Var varForeachBean = new Var(bean.toSharedPtr().toConstRef(), "entity");
		ForeachLoop foreach= _foreach(varForeachBean, collection.deref());
		for (Column pkCol : pk.getColumns()) {
			
			if(pkCol.hasOneRelation()){
				//colPk.getRelation().getDestTable().getCamelCaseName()
				foreach._callMethodInstr(params, "append",Types.QVariant.callStaticMethod(ClsQVariant.fromValue, varForeachBean.callAttrGetter(PgCppUtil.getOneRelationDestAttrName(pkCol.getOneRelation())).callMethod("get"+pkCol.getOneRelationMappedColumn().getUc1stCamelCaseName())) ); 
			}else{
				foreach._callMethodInstr(params, "append",Types.QVariant.callStaticMethod(ClsQVariant.fromValue, varForeachBean.callAttrGetter(pkCol.getCamelCaseName())));	
			}
			
//			foreach._callMethodInstr(params, "append", varForeachBean.callAttrGetter(pkCol.getCamelCaseName()));
		}
		
		exprQSqlQuery = exprQSqlQuery.callMethod("whereIn", varColumns, params);
		
		addInstr(exprQSqlQuery.asInstruction());
		Var query = _declare(Types.QSqlQuery, "query", sqlQuery.callMethod("execQuery",pSqlCon));
		
		
		IfBlock ifQueryNext = _if(query.callMethod("next"));
		InstructionBlock ifInstr = ifQueryNext.thenBlock();
		Var e1Map =  ifInstr._declare((!manyRelations.isEmpty()) ? new ClsQHash(e1PkType, bean.getFetchListHelperCls()) : new ClsQSet(e1PkType), "e1Map");
		
		DoWhile doWhileQueryNext = ifQueryNext.thenBlock()._doWhile();
		doWhileQueryNext.setCondition(query.callMethod("next"));
		Var recDoWhile =doWhileQueryNext._declare(Types.QSqlRecord, "rec",query.callMethod("record") );
		
		Var e1pk = null;
		ArrayList<Expression> listForeachPkCompare = new ArrayList<>();
		Var varIfNotE1SetContainsForeachBean = new Var(bean.toSharedPtr().toConstRef(), "entity");
		if (pk.isMultiColumn()) {
			e1pk =doWhileQueryNext._declare( bean.getStructPk(), "e1pk" );
			for(Column colPk:pk.getColumns()) {
				doWhileQueryNext._assign(e1pk.accessAttr(colPk.getCamelCaseName()), recDoWhile.callMethod("value", QString.fromStringConstant("e1__"+ colPk.getName())).callMethod(EntityCls.getDatabaseMapper().getQVariantConvertMethod(colPk)));
				
				listForeachPkCompare.add(
						new BinaryOperatorExpression(
								
								(colPk.hasOneRelation() 
									? varIfNotE1SetContainsForeachBean.callMethod( OrmUtil.getOneRelationDestAttrGetter(colPk.getOneRelation())).callMethod("get"+colPk.getOneRelationMappedColumn().getUc1stCamelCaseName())								
									: varIfNotE1SetContainsForeachBean.callMethod("get"+colPk.getUc1stCamelCaseName())
								),
								new LibEqualsOperator(),
								e1pk.accessAttr(colPk.getCamelCaseName())
						));
			}
			/*if(col.hasOneRelation()){
				//colPk.getRelation().getDestTable().getCamelCaseName()
				addInstr(params.callMethodInstruction("append",parent.getAttrByName(PgCppUtil.getOneRelationDestAttrName(col.getOneRelation())).callMethod("get"+col.getOneRelationMappedColumn().getUc1stCamelCaseName()) )); 
			}else{
				addInstr(params.callMethodInstruction("append",parent.getAttrByName(col.getCamelCaseName())));	
			}*/
			
		} else {
			Column colPk=pk.getFirstColumn();
			e1pk =doWhileQueryNext._declare( EntityCls.getDatabaseMapper().columnToType(pk.getFirstColumn()), "e1pk", recDoWhile.callMethod("value", QString.fromStringConstant("e1__"+ pk.getFirstColumn().getName())).callMethod(EntityCls.getDatabaseMapper().getQVariantConvertMethod(pk.getFirstColumn())));
			listForeachPkCompare.add(
					new BinaryOperatorExpression(
							varIfNotE1SetContainsForeachBean.callMethod("get"+colPk.getUc1stCamelCaseName()),
							new LibEqualsOperator(),
							e1pk
					));
		}
		
		
		IfBlock ifNotE1SetContains = doWhileQueryNext._if(Expressions.not(e1Map.callMethod("contains", e1pk)));
		

		ForeachLoop foreachIfNotE1SetContains= ifNotE1SetContains.thenBlock()._foreach(varIfNotE1SetContainsForeachBean, collection.deref());
		IfBlock ifForeachPkCompare = foreachIfNotE1SetContains._if(Expressions.and(listForeachPkCompare));

		doWhileQueryNext._assignInstruction(recDoWhile, query.callMethod("record"));
		
		
		
		
//		ifForeachPkCompare.getIfInstr()._assign(structHelperIfNotE1SetContains.accessAttr("e1"), e1DoWhile);
//		//bCount = 2;
//		for(Relation r:manyRelations) {
//			Type beanPk=Types.getRelationForeignPrimaryKeyType(r);
//			ifNotE1SetContains.getIfInstr()._assign(structHelperIfNotE1SetContains.accessAttr(r.getAlias()+"Set"),  new CreateObjectExpression(Types.qset(beanPk)));
//			//bCount++;
//		}
		//bCount = 2;
		if (manyRelations.isEmpty()) {
			ifForeachPkCompare.thenBlock()._callMethodInstr(e1Map, "insert", e1pk );
		} else {
			Var structHelperIfNotE1SetContains = ifForeachPkCompare.thenBlock()._declare(bean.getFetchListHelperCls(), "structHelper");
			ifForeachPkCompare.thenBlock()._assign(structHelperIfNotE1SetContains.accessAttr("e1"), varIfNotE1SetContainsForeachBean);
			ifForeachPkCompare.thenBlock()._callMethodInstr(e1Map, "insert", e1pk, structHelperIfNotE1SetContains );
			
			Var fkHelper = doWhileQueryNext._declare(bean.getFetchListHelperCls().toRef(), "fkHelper",e1Map.arrayIndex(e1pk));
			
			for(AbstractRelation r:manyRelations) {
				Type beanPk=Types.getRelationForeignPrimaryKeyType(r);
				EntityCls foreignCls = Entities.get(r.getDestTable()); 
				Expression foreignBeanExpression = getByRecordExpression(foreignCls, recDoWhile, QString.fromStringConstant(r.getAlias()));
//				IfBlock ifRecValueIsNotNull = null;
				Var foreignBean = null;				
				
				IfBlock ifNotPkForeignIsNull= doWhileQueryNext._if(Expressions.not( recDoWhile.callMethod("value", QString.fromStringConstant(r.getAlias()+"__"+ r.getDestTable().getPrimaryKey().getFirstColumn().getName())).callMethod("isNull")));
				
				Var pkForeign = null;
				if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
					pkForeign = ifNotPkForeignIsNull.thenBlock()._declare(beanPk, "pkForeignB"+r.getAlias());
					
					for(Column colPk:r.getDestTable().getPrimaryKey().getColumns()) {
						ifNotPkForeignIsNull.thenBlock()._assign(
								pkForeign.accessAttr(colPk.getCamelCaseName()), 
								recDoWhile.callMethod("value", QString.fromStringConstant(r.getAlias()+"__"+ colPk.getName())).callMethod(EntityCls.getDatabaseMapper().getQVariantConvertMethod(colPk)));
					}
				} else {
					Column colPk=r.getDestTable().getPrimaryKey().getFirstColumn();
					pkForeign = ifNotPkForeignIsNull.thenBlock()._declare(beanPk, "pkForeignB"+r.getAlias(),recDoWhile.callMethod("value", QString.fromStringConstant(r.getAlias()+"__"+ r.getDestTable().getPrimaryKey().getFirstColumn().getName())).callMethod(EntityCls.getDatabaseMapper().getQVariantConvertMethod(colPk)));
				}
				IfBlock ifRecValueIsNotNull = ifNotPkForeignIsNull.thenBlock()._if(
						Expressions.not(fkHelper
										.accessAttr(r.getAlias()+"Set")
										.callMethod("contains",
												pkForeign
												
												))
								
						
						
					);
				foreignBean =ifRecValueIsNotNull.thenBlock()._declare(foreignBeanExpression.getType(), "foreignB"+r.getAlias(),foreignBeanExpression) ;
				
				ifRecValueIsNotNull.thenBlock().addInstr(fkHelper.accessAttr("e1")
						.callMethodInstruction(EntityCls.getRelatedBeanMethodName(r), foreignBean));
				ifRecValueIsNotNull.thenBlock().addInstr(
						fkHelper.accessAttr(r.getAlias()+"Set")
						.callMethod("insert", 
								pkForeign
									).asInstruction())
					 ;
				
//				for (OneRelation foreignOneRelation: foreignCls.getOneRelations()) {
//					if (foreignOneRelation.getDestTable().equals(bean.getTbl())) {
//						ifRecValueIsNotNull.thenBlock().addInstr(foreignBean.callMethodInstruction("set"+r.getSourceTable().getUc1stCamelCaseName()+"Internal", fkHelper.accessAttr("e1")));
//					}
//				}
				
				
				//bCount++;
			}		
		}
		for(OneRelation r:oneRelations) {
			EntityCls foreignCls = Entities.get(r.getDestTable());
			Expression foreignBeanExpression = getByRecordExpression(foreignCls, recDoWhile, QString.fromStringConstant(r.getAlias()));
			
			IfBlock ifRelatedBeanIsNull= foreachIfNotE1SetContains.
					_if(varForeachBean.callMethod(new MethodOneRelationEntityIsNull(r)));
			
			Var foreignBean =ifRelatedBeanIsNull.thenBlock()._declare(foreignBeanExpression.getType(), "foreignB"+r.getAlias(),foreignBeanExpression) ;
			ifRelatedBeanIsNull.thenBlock()
				._callMethodInstr(
						varForeachBean ,
						new MethodAttrSetterInternal(foreignCls,
								bean.getAttrByName(PgCppUtil.getOneRelationDestAttrName(r)))
						,  foreignBean);
			
		
//			for (OneRelation foreignOneRelation: foreignCls.getOneRelations()) {
//				if (foreignOneRelation.getDestTable().equals(bean.getTbl())) {
//					ifRelatedBeanIsNull.thenBlock().addInstr(foreignBean.callMethodInstruction("set"+r.getSourceTable().getUc1stCamelCaseName()+"Internal", recDoWhile));
//				}
//			}
//			ifRelatedBeanIsNull.getIfInstr()._callMethodInstr(foreignBean, "setLoaded", BoolExpression.TRUE);
			
			//bCount++;
		}
		
		foreachIfNotE1SetContains._callMethodInstr(varForeachBean, "setLoaded", BoolExpression.TRUE);
				
		for(Column col:bean.getTbl().getAllColumns()) {
			try{
				if (!col.hasOneRelation() && !col.isPartOfPk() && !col.isFileImportEnabled()) {
					ifForeachPkCompare.thenBlock().addInstr(varIfNotE1SetContainsForeachBean.callMethodInstruction("set"+ col.getUc1stCamelCaseName()+"Internal",recDoWhile.callMethod("value", QString.fromStringConstant("e1__"+ col.getName())).callMethod(EntityCls.getDatabaseMapper().getQVariantConvertMethod(col))));
				}
//					_callMethodInstr(bean, "set"+col.getUc1stCamelCaseName(), getParam("record").callMethod("value", new QStringPlusOperatorExpression(getParam("alias"), QString.fromStringConstant("__"+ col.getName()))).callMethod(BeanCls.getDatabaseMapper().getQVariantConvertMethod(col)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
			
		
		
	}
}
