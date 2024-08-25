package cpp.entityrepository.method;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import codegen.CodeUtil;
import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.Type;
import cpp.core.expression.Expression;
import cpp.core.expression.Expressions;
import cpp.core.expression.QChar;
import cpp.core.expression.Var;
import cpp.core.instruction.IfBlock;
import cpp.core.instruction.While;
import cpp.entity.Entities;
import cpp.entity.EntityCls;
import cpp.entity.method.MethodAddRelatedEntityInternal;
import cpp.entity.method.MethodOneRelationAttrSetter;
import cpp.entity.method.MethodOneRelationEntityIsNull;
import cpp.lib.ClsSqlQuery;
import cpp.lib.ClsQVariant;
import cpp.util.ClsDbPool;
import database.column.Column;
import database.relation.AbstractRelation;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import database.relation.PrimaryKey;
import util.CodeUtil2;

public class MethodEntityLoad extends Method {

	protected AbstractRelation relation;
	protected PrimaryKey primaryKey;
	protected EntityCls entity;
	protected Param pEntity;
	protected Param pSqlCon;
	
	public static String getMethodName() {
		return "load";
	}
	
	public MethodEntityLoad(EntityCls entity, AbstractRelation r) {
		super(Public, Types.Void, getMethodName());
		
		this.relation =r;
		this.primaryKey = entity.getTbl().getPrimaryKey();
		this.entity = entity;
		pEntity = addParam(entity.toRef(), "entity");
		pSqlCon = addParam(Types.QSqlDatabase.toConstRef(),"sqlCon",ClsDbPool.instance.callStaticMethod(ClsDbPool.getDatabase));
		setStatic(true);
	}

	@Override
	public boolean includeIfEmpty() {
		return true;
	}
	
	@Override
	public void addImplementation() {
		EntityCls entity = this.entity;
		
		Var sqlQuery = _declareInitConstructor( EntityCls.getDatabaseMapper().getSqlQueryType(),"query");
		
		ArrayList<Expression> selectFields = new ArrayList<>();
		selectFields.add(entity.callStaticMethod("getSelectFields",QString.fromStringConstant("e1")));
		
			selectFields.add(Entities.get(relation.getDestTable()).callStaticMethod("getSelectFields", QString.fromStringConstant(relation.getAlias())));
			
		Expression exprQSqlQuery = sqlQuery.callMethod("select", Expressions.concat(QChar.fromChar(','), selectFields) )
									.callMethod("from", QString.fromExpression(entity.callStaticMethod("getTableName",QString.fromStringConstant("e1"))));
		
				
		if(relation instanceof OneRelation) {
			OneRelation r= (OneRelation)relation;
			ArrayList<String> joinConditions=new ArrayList<>();
			for(int i=0;i<r.getColumnCount();i++) {
				joinConditions.add(CodeUtil.sp("e1."+r.getColumns(i).getValue1().getEscapedName(),'=',r.getAlias()+"."+ r.getColumns(i).getValue2().getEscapedName()));
			}
			
			exprQSqlQuery = exprQSqlQuery.callMethod("leftJoin", QString.fromExpression(Entities.get(r.getDestTable()).callStaticMethod("getTableName")),QString.fromStringConstant(r.getAlias()), QString.fromStringConstant(CodeUtil2.concat(joinConditions," AND ")));
			
			if(r.hasAdditionalJoin()) {
				exprQSqlQuery = exprQSqlQuery.callMethod(ClsSqlQuery.join,QString.fromStringConstant(r.getAdditionalJoin()));
			}
		} else 	if(relation instanceof OneToManyRelation) {
			OneToManyRelation r= (OneToManyRelation)relation;
			ArrayList<String> joinConditions=new ArrayList<>();
			for(int i=0;i<r.getColumnCount();i++) {
				joinConditions.add(CodeUtil.sp("e1."+r.getColumns(i).getValue1().getEscapedName(),'=',r.getAlias()+"."+ r.getColumns(i).getValue2().getEscapedName()));
			}
			
			exprQSqlQuery = exprQSqlQuery.callMethod("leftJoin", QString.fromExpression(Entities.get(r.getDestTable()).callStaticMethod("getTableName")),QString.fromStringConstant(r.getAlias()), QString.fromStringConstant(CodeUtil2.concat(joinConditions," AND ")));
			if(r.hasAdditionalJoin()) {
				exprQSqlQuery = exprQSqlQuery.callMethod(ClsSqlQuery.join,QString.fromStringConstant(r.getAdditionalJoin()));
			}
		} else	if(relation instanceof ManyRelation) {
			ManyRelation r= (ManyRelation)relation;
			ArrayList<String> joinConditionsMappingDest=new ArrayList<>();
			ArrayList<String> joinConditionsE1Mapping=new ArrayList<>();
			for(int i=0;i<r.getSourceColumnCount();i++) {
				joinConditionsE1Mapping.add(
						CodeUtil.sp("e1."+r.getSourceEntityColumn(i).getEscapedName(),
								'=',
								r.getMappingAlias()+"."+ r.getSourceMappingColumn(i).getEscapedName()));
				
			}
			for(int i=0;i<r.getDestColumnCount();i++) {
				joinConditionsMappingDest.add(
						CodeUtil.sp(r.getMappingAlias()+ "."+r.getDestMappingColumn(i).getEscapedName(),
								'=',
								r.getAlias()+"."+ r.getDestEntityColumn(i).getEscapedName()));
			}
			exprQSqlQuery = exprQSqlQuery.callMethod("leftJoin", 
					QString.fromStringConstant(r.getMappingTable().getEscapedName()),
					QString.fromStringConstant(r.getMappingAlias()), 
					QString.fromStringConstant(CodeUtil2.concat(joinConditionsE1Mapping," AND ")));
			exprQSqlQuery = exprQSqlQuery.callMethod("leftJoin", 
					Entities.get(r.getDestTable()).callStaticMethod("getTableName"),
					QString.fromStringConstant(r.getAlias()), 
					QString.fromStringConstant(CodeUtil2.concat(joinConditionsMappingDest," AND ")));
			
			if(r.hasAdditionalJoin()) {
				exprQSqlQuery = exprQSqlQuery.callMethod(ClsSqlQuery.join,QString.fromStringConstant(r.getAdditionalJoin()));
			}
		}
		
		for(Column col:primaryKey.getColumns()) {
			
			exprQSqlQuery = exprQSqlQuery.callMethod("where", QString.fromStringConstant("e1."+ col.getEscapedName()+"=?"),EntityCls.accessThisAttrGetterByColumn(pEntity,col));
					
		}
		if(relation.hasAdditionalOrderBy()) {
			exprQSqlQuery = exprQSqlQuery.callMethod(ClsSqlQuery.orderBy,QString.fromStringConstant(relation.getAdditionalOrderBy()));
		}
		exprQSqlQuery = exprQSqlQuery.callMethod("execQuery",pSqlCon);
		Var qSqlQuery = _declare(exprQSqlQuery.getType(),
				"qSqlQuery", exprQSqlQuery
				);
		
		//bCount = 2;
		HashMap<String, Var> pkSets=new HashMap<>();
		if(relation instanceof OneToManyRelation) {
			OneToManyRelation r= (OneToManyRelation)relation;
			EntityCls foreignCls = Entities.get(r.getDestTable()); 
			if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
				Var pkSet = _declare(Types.qset(foreignCls.getStructPk()), "pkSetB"+r.getAlias());
				pkSets.put(r.getAlias() ,pkSet);
			} else {
				Column colPk = r.getDestTable().getPrimaryKey().getColumns().get(0);
				Type type = EntityCls.getDatabaseMapper().columnToType(colPk);
				Var pkSet = _declare(Types.qset(type), "pkSetB"+r.getAlias());
				pkSets.put(r.getAlias(), pkSet);
			}
		} else	if(relation instanceof ManyRelation) {
			ManyRelation r= (ManyRelation)relation;
			EntityCls foreignCls = Entities.get(r.getDestTable()); 
			if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
				Var pkSet = _declare(Types.qset(foreignCls.getStructPk()), "pkSetB"+r.getAlias());
				pkSets.put(r.getAlias() ,pkSet);
			} else {
				Column colPk = r.getDestTable().getPrimaryKey().getColumns().get(0);
				Type type = EntityCls.getDatabaseMapper().columnToType(colPk);
				Var pkSet = _declare(Types.qset(type), "pkSetB"+r.getAlias());
				pkSets.put(r.getAlias(), pkSet);
			}
		}
		
		While doWhileQSqlQueryNext = _while(qSqlQuery.callMethod("next"));
		Var rec = doWhileQSqlQueryNext._declare(Types.QSqlRecord, "rec",qSqlQuery.callMethod("record") );
		//bCount = 2;
		if(relation instanceof OneToManyRelation) {
			OneToManyRelation r= (OneToManyRelation)relation;
			Var pkSet=pkSets.get(r.getAlias());
			EntityCls foreignCls = Entities.get(r.getDestTable()); 
			
			
			if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
				IfBlock ifNotPkForeignIsNull= doWhileQSqlQueryNext._if(Expressions.not( rec.callMethod("value", QString.fromStringConstant(r.getAlias()+"__"+ r.getDestTable().getPrimaryKey().getFirstColumn().getName())).callMethod(ClsQVariant.isNull)));
				Var pk = ifNotPkForeignIsNull.thenBlock()._declare(foreignCls.getStructPk(), "pk"+r.getAlias());
				for(Column colPk : r.getDestTable().getPrimaryKey().getColumns()) {
					ifNotPkForeignIsNull.thenBlock()._assign(
							pk.accessAttr(colPk.getCamelCaseName()), 
							
							rec.callMethod("value", QString.fromStringConstant(r.getAlias()+"__"+ colPk.getName())).callMethod(EntityCls.getDatabaseMapper().getQVariantConvertMethod(colPk)));
				}
				
//				IfBlock ifNotContains = 
				ifNotPkForeignIsNull.thenBlock()._if(Expressions.not(pkSet.callMethod("contains", pk)))
						.addIfInstr(pkSet.callMethodInstruction("insert", pk))
						.addIfInstr(pEntity.callMethodInstruction(MethodAddRelatedEntityInternal.getMethodName(r) , parent.callStaticMethod(MethodGetFromRecord.getMethodName(foreignCls), rec, QString.fromStringConstant(r.getAlias()))));
				
			} else {
				
				Column colPk = r.getDestTable().getPrimaryKey().getColumns().get(0);
				Expression recValueColPk = rec.callMethod("value", QString.fromStringConstant(r.getAlias()+"__"+colPk.getName()));
				Var pk = doWhileQSqlQueryNext._declare(recValueColPk.getType(), "pk"+r.getAlias(),recValueColPk);
				
				//IfBlock ifNotPkForeignIsNull= doWhileQSqlQueryNext._if(Expressions.not(recValueColPk.callMethod(ClsQVariant.isNull)));
//				Type type = BeanCls.getDatabaseMapper().columnToType(colPk);
				
//				Var pk = doWhileQSqlQueryNext._declare(
//						type, 
//						"pk"+r.getAlias() 
//						.callMethod(BeanCls.getDatabaseMapper().getQVariantConvertMethod(colPk))
//						
//						);
				
				
				doWhileQSqlQueryNext._if(
						Expressions.and(
								Expressions.not(pk.callMethod(ClsQVariant.isNull)),
							Expressions.not(pkSet.callMethod("contains", pk.callMethod(EntityCls.getDatabaseMapper().getQVariantConvertMethod(colPk))))
						))
							.addIfInstr(pkSet.callMethodInstruction("insert", pk.callMethod(EntityCls.getDatabaseMapper().getQVariantConvertMethod(colPk))))
							.addIfInstr(pEntity.callMethodInstruction(MethodAddRelatedEntityInternal.getMethodName(r) , parent.callStaticMethod(MethodGetFromRecord.getMethodName(foreignCls), rec, QString.fromStringConstant(r.getAlias()))))
					;
			}
			
//			Var varForeach = new Var(, name);
//			doWhileQSqlQueryNext._foreach(var, collection);
			//bCount++;
		}
		if(relation instanceof ManyRelation) {
			ManyRelation r= (ManyRelation)relation;
			Var pkSet=pkSets.get(r.getAlias());
			EntityCls foreignCls = Entities.get(r.getDestTable()); 
			if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
				
				Var pk = doWhileQSqlQueryNext._declare(foreignCls.getStructPk(), "pk"+r.getAlias());
				for(Column colPk : r.getDestTable().getPrimaryKey().getColumns()) {
					doWhileQSqlQueryNext._assign(
							pk.accessAttr(colPk.getCamelCaseName()), 
							
							rec.callMethod("value", QString.fromStringConstant(r.getAlias()+"__"+ colPk.getName())).callMethod(EntityCls.getDatabaseMapper().getQVariantConvertMethod(colPk)));
				}
				
//				IfBlock ifNotContains = 
						doWhileQSqlQueryNext._if(Expressions.not(pkSet.callMethod("contains", pk)))
						.addIfInstr(pkSet.callMethodInstruction("insert", pk))
						.addIfInstr(pEntity.callMethodInstruction(MethodAddRelatedEntityInternal.getMethodName(r) ,_this().callMethod(MethodGetFromRecord.getMethodName(foreignCls),  rec, QString.fromStringConstant(r.getAlias()))));
				
			} else {
				
				Column colPk = r.getDestTable().getPrimaryKey().getColumns().get(0);
				Expression recValueColPk = rec.callMethod("value", QString.fromStringConstant(r.getAlias()+"__"+colPk.getName()));
				Var pk = doWhileQSqlQueryNext._declare(recValueColPk.getType(), "pk"+r.getAlias(),recValueColPk);
				
				doWhileQSqlQueryNext._if(
						Expressions.and(
							Expressions.not(pk.callMethod("isNull")),
							Expressions.not(pkSet.callMethod("contains", pk.callMethod(EntityCls.getDatabaseMapper().getQVariantConvertMethod(colPk))))
						))
							.addIfInstr(pkSet.callMethodInstruction("insert", pk.callMethod(EntityCls.getDatabaseMapper().getQVariantConvertMethod(colPk))))
							.addIfInstr(pEntity.callMethodInstruction(MethodAddRelatedEntityInternal.getMethodName(r) , parent.callStaticMethod(MethodGetFromRecord.getMethodName(foreignCls), rec, QString.fromStringConstant(r.getAlias()))))
					;
			}
		}
		if(relation instanceof OneRelation) {
			OneRelation r= (OneRelation)relation;
			EntityCls foreignCls = Entities.get(r.getDestTable()); 
			try {
				IfBlock ifBlock= doWhileQSqlQueryNext._if(
						Expressions.and(
								pEntity.callMethod(MethodOneRelationEntityIsNull.getMethodName(r, true))
								,
								Expressions.not( rec.callMethod("value", QString.fromStringConstant(r.getAlias()+"__"+ r.getDestTable().getPrimaryKey().getFirstColumn().getName())).callMethod(ClsQVariant.isNull))
						
						));
				ifBlock.thenBlock()._callMethodInstr(pEntity, MethodOneRelationAttrSetter.getMethodName(r, true), parent.callStaticMethod(MethodGetFromRecord.getMethodName(foreignCls), rec, QString.fromStringConstant(r.getAlias())));
			} catch (Exception e) {
				e.printStackTrace();
			}
			//bCount++;
		}
	}

}
