package cpp.beanrepository.method;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import codegen.CodeUtil;
import cpp.Types;
import cpp.bean.BeanCls;
import cpp.bean.Beans;
import cpp.bean.method.MethodAddRelatedBeanInternal;
import cpp.bean.method.MethodOneRelationAttrSetter;
import cpp.bean.method.MethodOneRelationBeanIsNull;
import cpp.beanrepository.ClsBeanRepository;
import cpp.core.Attr;
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
import cpp.lib.ClsQSqlQuery;
import cpp.lib.ClsSql;
import database.column.Column;
import database.relation.AbstractRelation;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import database.relation.PrimaryKey;
import util.CodeUtil2;

public class MethodBeanLoad extends Method {

	protected List<OneRelation> oneRelations;
	protected List<OneToManyRelation> oneToManyRelations;
	protected List<ManyRelation> manyRelations;
	protected PrimaryKey primaryKey;
	protected BeanCls bean;
	protected Param pBean;
	
	public static String getMethodName() {
		return "load";
	}
	
	public MethodBeanLoad(BeanCls bean) {
		super(Public, Types.Void, getMethodName());
		
		this.oneRelations = bean.getOneRelations();
		this.oneToManyRelations = bean.getOneToManyRelations();
		this.manyRelations = bean.getManyRelations();
		this.primaryKey = bean.getTbl().getPrimaryKey();
		this.bean = bean;
		pBean = addParam(bean.toRawPointer(), "bean");
//		setConstQualifier(true);
	}

	@Override
	public boolean includeIfEmpty() {
		return true;
	}
	
	@Override
	public void addImplementation() {
		BeanCls bean = this.bean;
		
		Attr aSqlCon = this.parent.getAttrByName(ClsBeanRepository.sqlCon);
		Method mBuildQuery = aSqlCon.getClassType().getMethod(ClsSql.buildQuery);
		Var sqlQuery = _declare(mBuildQuery.getReturnType(), "query",aSqlCon.callMethod(mBuildQuery));
		
		ArrayList<Expression> selectFields = new ArrayList<>();
		selectFields.add(bean.callStaticMethod("getSelectFields",QString.fromStringConstant("b1")));
		
		List<AbstractRelation> allRelations = new ArrayList<>(oneRelations.size()+oneToManyRelations.size()+manyRelations.size());
		allRelations.addAll(oneRelations);
		allRelations.addAll(oneToManyRelations);
		allRelations.addAll(manyRelations);
		
		
		for(AbstractRelation r:allRelations) {
			selectFields.add(Beans.get(r.getDestTable()).callStaticMethod("getSelectFields", QString.fromStringConstant(r.getAlias())));
			
		}
		Expression exprQSqlQuery = sqlQuery.callMethod("select", Expressions.concat(QChar.fromChar(','), selectFields) )
									.callMethod("from", QString.fromExpression(bean.callStaticMethod("getTableName",QString.fromStringConstant("b1"))));
		
				
		for(OneRelation r:oneRelations) {
			ArrayList<String> joinConditions=new ArrayList<>();
			for(int i=0;i<r.getColumnCount();i++) {
				joinConditions.add(CodeUtil.sp("b1."+r.getColumns(i).getValue1().getEscapedName(),'=',r.getAlias()+"."+ r.getColumns(i).getValue2().getEscapedName()));
			}
			
			exprQSqlQuery = exprQSqlQuery.callMethod("leftJoin", QString.fromExpression(Beans.get(r.getDestTable()).callStaticMethod("getTableName")),QString.fromStringConstant(r.getAlias()), QString.fromStringConstant(CodeUtil2.concat(joinConditions," AND ")));
		}
		for(OneToManyRelation r:oneToManyRelations) {
			ArrayList<String> joinConditions=new ArrayList<>();
			for(int i=0;i<r.getColumnCount();i++) {
				joinConditions.add(CodeUtil.sp("b1."+r.getColumns(i).getValue1().getEscapedName(),'=',r.getAlias()+"."+ r.getColumns(i).getValue2().getEscapedName()));
			}
			
			exprQSqlQuery = exprQSqlQuery.callMethod("leftJoin", QString.fromExpression(Beans.get(r.getDestTable()).callStaticMethod("getTableName")),QString.fromStringConstant(r.getAlias()), QString.fromStringConstant(CodeUtil2.concat(joinConditions," AND ")));
		}
		for(ManyRelation r:manyRelations) {
			ArrayList<String> joinConditionsMappingDest=new ArrayList<>();
			ArrayList<String> joinConditionsB1Mapping=new ArrayList<>();
			for(int i=0;i<r.getSourceColumnCount();i++) {
				joinConditionsB1Mapping.add(
						CodeUtil.sp("b1."+r.getSourceEntityColumn(i).getEscapedName(),
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
					QString.fromStringConstant(r.getMappingTable().getName()),
					QString.fromStringConstant(r.getMappingAlias()), 
					QString.fromStringConstant(CodeUtil2.concat(joinConditionsB1Mapping," AND ")));
			exprQSqlQuery = exprQSqlQuery.callMethod("leftJoin", 
					Beans.get(r.getDestTable()).callStaticMethod("getTableName"),
					QString.fromStringConstant(r.getAlias()), 
					QString.fromStringConstant(CodeUtil2.concat(joinConditionsMappingDest," AND ")));
			
			
		}
		
		for(Column col:primaryKey.getColumns()) {
			
			exprQSqlQuery = exprQSqlQuery.callMethod("where", QString.fromStringConstant("b1."+ col.getEscapedName()+"=?"),BeanCls.accessThisAttrGetterByColumn(pBean,col));
					
		}
		exprQSqlQuery = exprQSqlQuery.callMethod("execQuery");
		Var qSqlQuery = _declare(exprQSqlQuery.getType(),
				"qSqlQuery", exprQSqlQuery
				);
		
		//bCount = 2;
		HashMap<String, Var> pkSets=new HashMap<>();
		for(OneToManyRelation r:oneToManyRelations) {
			BeanCls foreignCls = Beans.get(r.getDestTable()); 
			if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
				Var pkSet = _declare(Types.qset(foreignCls.getStructPk()), "pkSetB"+r.getAlias());
				pkSets.put(r.getAlias() ,pkSet);
			} else {
				Column colPk = r.getDestTable().getPrimaryKey().getColumns().get(0);
				Type type = BeanCls.getDatabaseMapper().columnToType(colPk);
				Var pkSet = _declare(Types.qset(type), "pkSetB"+r.getAlias());
				pkSets.put(r.getAlias(), pkSet);
			}
		}
		for(ManyRelation r:manyRelations) {
			BeanCls foreignCls = Beans.get(r.getDestTable()); 
			if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
				Var pkSet = _declare(Types.qset(foreignCls.getStructPk()), "pkSetB"+r.getAlias());
				pkSets.put(r.getAlias() ,pkSet);
			} else {
				Column colPk = r.getDestTable().getPrimaryKey().getColumns().get(0);
				Type type = BeanCls.getDatabaseMapper().columnToType(colPk);
				Var pkSet = _declare(Types.qset(type), "pkSetB"+r.getAlias());
				pkSets.put(r.getAlias(), pkSet);
			}
		}
		
		While doWhileQSqlQueryNext = _while(qSqlQuery.callMethod("next"));
		Var rec = doWhileQSqlQueryNext._declare(Types.QSqlRecord, "rec",qSqlQuery.callMethod("record") );
		//bCount = 2;
		for(OneToManyRelation r:oneToManyRelations) {
			Var pkSet=pkSets.get(r.getAlias());
			BeanCls foreignCls = Beans.get(r.getDestTable()); 
			if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
				
				Var pk = doWhileQSqlQueryNext._declare(foreignCls.getStructPk(), "pk"+r.getAlias());
				for(Column colPk : r.getDestTable().getPrimaryKey().getColumns()) {
					doWhileQSqlQueryNext._assign(
							pk.accessAttr(colPk.getCamelCaseName()), 
							
							rec.callMethod("value", QString.fromStringConstant(r.getAlias()+"__"+ colPk.getName())).callMethod(BeanCls.getDatabaseMapper().getQVariantConvertMethod(colPk.getDbType())));
				}
				
//				IfBlock ifNotContains = 
						doWhileQSqlQueryNext._if(Expressions.not(pkSet.callMethod("contains", pk)))
						.addIfInstr(pkSet.callMethodInstruction("insert", pk))
						.addIfInstr(pBean.callMethodInstruction(MethodAddRelatedBeanInternal.getMethodName(r) , _this().callMethod(MethodGetFromRecord.getMethodName(foreignCls), rec, QString.fromStringConstant(r.getAlias()))));
				
			} else {
				
				Column colPk = r.getDestTable().getPrimaryKey().getColumns().get(0);
				Expression recValueColPk = rec.callMethod("value", QString.fromStringConstant(r.getAlias()+"__"+colPk.getName()));
				Var pk = doWhileQSqlQueryNext._declare(recValueColPk.getType(), "pk"+r.getAlias(),recValueColPk);
//				Type type = BeanCls.getDatabaseMapper().columnToType(colPk);
				
//				Var pk = doWhileQSqlQueryNext._declare(
//						type, 
//						"pk"+r.getAlias() 
//						.callMethod(BeanCls.getDatabaseMapper().getQVariantConvertMethod(colPk.getDbType()))
//						
//						);
				
				
				doWhileQSqlQueryNext._if(
						Expressions.and(
							Expressions.not(pk.callMethod("isNull")),
							Expressions.not(pkSet.callMethod("contains", pk.callMethod(BeanCls.getDatabaseMapper().getQVariantConvertMethod(colPk.getDbType()))))
						))
							.addIfInstr(pkSet.callMethodInstruction("insert", pk.callMethod(BeanCls.getDatabaseMapper().getQVariantConvertMethod(colPk.getDbType()))))
							.addIfInstr(pBean.callMethodInstruction(MethodAddRelatedBeanInternal.getMethodName(r) , _this().callMethod(MethodGetFromRecord.getMethodName(foreignCls), rec, QString.fromStringConstant(r.getAlias()))))
					;
			}
			
//			Var varForeach = new Var(, name);
//			doWhileQSqlQueryNext._foreach(var, collection);
			//bCount++;
		}
		for(ManyRelation r:manyRelations) {
			Var pkSet=pkSets.get(r.getAlias());
			BeanCls foreignCls = Beans.get(r.getDestTable()); 
			if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
				
				Var pk = doWhileQSqlQueryNext._declare(foreignCls.getStructPk(), "pk"+r.getAlias());
				for(Column colPk : r.getDestTable().getPrimaryKey().getColumns()) {
					doWhileQSqlQueryNext._assign(
							pk.accessAttr(colPk.getCamelCaseName()), 
							
							rec.callMethod("value", QString.fromStringConstant(r.getAlias()+"__"+ colPk.getName())).callMethod(BeanCls.getDatabaseMapper().getQVariantConvertMethod(colPk.getDbType())));
				}
				
//				IfBlock ifNotContains = 
						doWhileQSqlQueryNext._if(Expressions.not(pkSet.callMethod("contains", pk)))
						.addIfInstr(pkSet.callMethodInstruction("insert", pk))
						.addIfInstr(pBean.callMethodInstruction(MethodAddRelatedBeanInternal.getMethodName(r) ,_this().callMethod(MethodGetFromRecord.getMethodName(foreignCls),  rec, QString.fromStringConstant(r.getAlias()))));
				
			} else {
				
				Column colPk = r.getDestTable().getPrimaryKey().getColumns().get(0);
				Expression recValueColPk = rec.callMethod("value", QString.fromStringConstant(r.getAlias()+"__"+colPk.getName()));
				Var pk = doWhileQSqlQueryNext._declare(recValueColPk.getType(), "pk"+r.getAlias(),recValueColPk);
				
				doWhileQSqlQueryNext._if(
						Expressions.and(
							Expressions.not(pk.callMethod("isNull")),
							Expressions.not(pkSet.callMethod("contains", pk.callMethod(BeanCls.getDatabaseMapper().getQVariantConvertMethod(colPk.getDbType()))))
						))
							.addIfInstr(pkSet.callMethodInstruction("insert", pk.callMethod(BeanCls.getDatabaseMapper().getQVariantConvertMethod(colPk.getDbType()))))
							.addIfInstr(pBean.callMethodInstruction(MethodAddRelatedBeanInternal.getMethodName(r) , _this().callMethod(MethodGetFromRecord.getMethodName(foreignCls), rec, QString.fromStringConstant(r.getAlias()))))
					;
			}
		}
		for(OneRelation r:oneRelations) {
			BeanCls foreignCls = Beans.get(r.getDestTable()); 
			try {
				IfBlock ifBlock= doWhileQSqlQueryNext._if(pBean.callMethod(MethodOneRelationBeanIsNull.getMethodName(r, true)));
				ifBlock.thenBlock()._callMethodInstr(pBean, MethodOneRelationAttrSetter.getMethodName(r, true),  _this().callMethod(MethodGetFromRecord.getMethodName(foreignCls), rec, QString.fromStringConstant(r.getAlias())));
			} catch (Exception e) {
				e.printStackTrace();
			}
			//bCount++;
		}
		_callMethodInstr(qSqlQuery, ClsQSqlQuery.clear); 
	}

}
