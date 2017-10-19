package cpp.cls.bean.repo.method;

import generate.CodeUtil2;

import java.util.ArrayList;
import java.util.List;

import codegen.CodeUtil;
import model.AbstractRelation;
import model.Column;
import model.ManyRelation;
import model.OneToManyRelation;
import model.OneRelation;
import cpp.Types;
import cpp.cls.Attr;
import cpp.cls.Method;
import cpp.cls.Param;
import cpp.cls.QString;
import cpp.cls.Type;
import cpp.cls.bean.BeanCls;
import cpp.cls.bean.Beans;
import cpp.cls.bean.method.MethodOneRelationAttrSetter;
import cpp.cls.bean.method.MethodOneRelationBeanIsNull;
import cpp.cls.bean.repo.ClsBeanRepository;
import cpp.cls.bean.repo.expression.ThisBeanRepositoryExpression;
import cpp.cls.expression.BoolExpression;
import cpp.cls.expression.Expression;
import cpp.cls.expression.Expressions;
import cpp.cls.expression.QChar;
import cpp.cls.expression.Var;
import cpp.cls.instruction.DoWhile;
import cpp.cls.instruction.IfBlock;

public class MethodGetById extends Method {

	protected BeanCls bean;
	public MethodGetById(BeanCls cls) {
//		super(Public, cls.toRawPointer(), "getById");
		super(Public, cls.toSharedPtr(), "get"+cls.getName()+"ById");
		for(Column col:cls.getTbl().getPrimaryKey().getColumns()) {
			Type colType =  BeanCls.getDatabaseMapper().columnToType(col);
			addParam(new Param(colType.isPrimitiveType() ? colType : colType.toConstRef(), col.getCamelCaseName()));
		}
//		setStatic(true);
		this.bean=cls;
	}

	@Override
	public ThisBeanRepositoryExpression _this() {
		return new ThisBeanRepositoryExpression((ClsBeanRepository) parent);
	}
	
	@Override
	public void addImplementation() {
		if (bean.getName().equals("Order")) {
			System.out.println();
		}
		List<OneRelation> oneRelations = bean.getOneRelations();
		List<OneToManyRelation> oneToManyRelations = bean.getOneToManyRelations();
		List<ManyRelation> manyToManyRelations = bean.getManyToManyRelations();
		
		Attr aSqlCon = this.parent.getAttrByName("sqlCon");
		Method mBuildQuery = aSqlCon.getClassType().getMethod("buildQuery");
		Var sqlQuery = _declare(mBuildQuery.getReturnType(), "query",aSqlCon.callMethod(mBuildQuery));
		
		ArrayList<Expression> selectFields = new ArrayList<>();
		selectFields.add(bean.callStaticMethod("getSelectFields",QString.fromStringConstant("b1")));
		
		List<AbstractRelation> allRelations = new ArrayList<>(oneRelations.size()+oneToManyRelations.size()+manyToManyRelations.size());
		allRelations.addAll(oneRelations);
		allRelations.addAll(oneToManyRelations);
		allRelations.addAll(manyToManyRelations);
		
		for(AbstractRelation r:allRelations) {
			selectFields.add(Beans.get(r.getDestTable()).callStaticMethod("getSelectFields", QString.fromStringConstant(r.getAlias())));
		}
		Expression exprQSqlQuery = sqlQuery.callMethod("select", Expressions.concat(QChar.fromChar(','), selectFields) )
									.callMethod("from", bean.callStaticMethod("getTableName",QString.fromStringConstant("b1")));
		
		for(OneRelation r:oneRelations) {
			ArrayList<String> joinConditions=new ArrayList<>();
			for(int i=0;i<r.getColumnCount();i++) {
				joinConditions.add(CodeUtil.sp("b1."+r.getColumns(i).getValue1().getEscapedName(),'=',(r.getAlias())+"."+ r.getColumns(i).getValue2().getEscapedName()));
			}
			
			exprQSqlQuery = exprQSqlQuery.callMethod("leftJoin", QString.fromExpression(Beans.get(r.getDestTable()).callStaticMethod("getTableName")),QString.fromStringConstant(r.getAlias()), QString.fromStringConstant(CodeUtil2.concat(joinConditions," AND ")));
		}
		for(OneToManyRelation r:oneToManyRelations) {
			ArrayList<String> joinConditions=new ArrayList<>();
			for(int i=0;i<r.getColumnCount();i++) {
				joinConditions.add(CodeUtil.sp("b1."+r.getColumns(i).getValue1().getEscapedName(),'=',(r.getAlias())+"."+ r.getColumns(i).getValue2().getEscapedName()));
			}
			
			exprQSqlQuery = exprQSqlQuery.callMethod("leftJoin", QString.fromExpression(Beans.get(r.getDestTable()).callStaticMethod("getTableName")),QString.fromStringConstant(r.getAlias()), QString.fromStringConstant(CodeUtil2.concat(joinConditions," AND ")));
		}
		for(ManyRelation r:manyToManyRelations) {
			ArrayList<String> joinConditions=new ArrayList<>();
			for(int i=0;i<r.getSourceColumnCount();i++) {
				joinConditions.add(CodeUtil.sp("b1."+r.getSourceEntityColumn(i).getEscapedName(),'=',r.getAlias("mapping")+"."+ r.getSourceMappingColumn(i).getEscapedName()));
			}
			
			exprQSqlQuery = exprQSqlQuery.callMethod("leftJoin", QString.fromStringConstant(r.getMappingTable().getName()),QString.fromStringConstant(r.getAlias("mapping")), QString.fromStringConstant(CodeUtil2.concat(joinConditions," AND ")));
			
			joinConditions.clear();
			for(int i=0;i<r.getDestColumnCount();i++) {
				joinConditions.add(CodeUtil.sp(r.getAlias("mapping")+"."+r.getDestMappingColumn(i).getEscapedName(),'=',r.getAlias()+"."+r.getDestEntityColumn(i).getEscapedName() ));
			}
			
			exprQSqlQuery = exprQSqlQuery.callMethod("leftJoin", Beans.get(r.getDestTable()).callStaticMethod("getTableName"),QString.fromStringConstant(r.getAlias()), QString.fromStringConstant(CodeUtil2.concat(joinConditions," AND ")));
			
			//bCount++;
		}

		
		for(Column col:bean.getTbl().getPrimaryKey().getColumns()) {
			exprQSqlQuery = exprQSqlQuery.callMethod("where", QString.fromStringConstant("b1."+ col.getEscapedName()+"=?"),getParam(col.getCamelCaseName()));
					
		}
		exprQSqlQuery = exprQSqlQuery.callMethod("execQuery");
		Var qSqlQuery = _declare(exprQSqlQuery.getType(),
				"qSqlQuery", exprQSqlQuery
				);
		Var b1 = _declare(returnType, "b1", Expressions.Nullptr);
		IfBlock ifQSqlQueryNext =
				_if(qSqlQuery.callMethod("next"))
					.setIfInstr(
							b1.assign(_this().callMethod(MethodGetFromRecord.getMethodName(bean),  qSqlQuery.callMethod("record"), QString.fromStringConstant("b1")))
							,
							b1.callSetterMethodInstruction("loaded", BoolExpression.TRUE)//_assignInstruction(b1.accessAttr("loaded"), BoolExpression.TRUE)
							);
		
		
		DoWhile doWhileQSqlQueryNext = DoWhile.create();
		Var rec = doWhileQSqlQueryNext._declare(Types.QSqlRecord, "rec",qSqlQuery.callMethod("record") );
//		//bCount = 2;
		
		ArrayList<AbstractRelation> manyRelations = new ArrayList<>(oneToManyRelations.size()+manyToManyRelations.size());
		manyRelations.addAll(oneToManyRelations);
		manyRelations.addAll(manyToManyRelations);
		
		for(OneRelation r:oneRelations) {
			BeanCls foreignCls = Beans.get(r.getDestTable()); 
			//AccessExpression acc = b1.accessAttr(PgCppUtil.getOneRelationDestAttrName(r));
			//IfBlock ifBlock= doWhileQSqlQueryNext._if(acc.isNull());
			IfBlock ifBlock= doWhileQSqlQueryNext._if(b1.callMethod(new MethodOneRelationBeanIsNull(r)));
			ifBlock.thenBlock().
			_callMethodInstr(b1, new MethodOneRelationAttrSetter( bean,r, true),  _this().callGetByRecordMethod(foreignCls, rec, QString.fromStringConstant(r.getAlias())));
			//ifBlock.getIfInstr()._assign(acc, _this().callGetByRecordMethod(foreignCls, rec, QString.fromStringConstant(r.getAlias())));
//			//bCount++;
		}
		for(AbstractRelation r:manyRelations) {
			BeanCls foreignCls = Beans.get(r.getDestTable()); 
			if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
				Var pkSet = ifQSqlQueryNext.thenBlock()._declare(Types.qset(foreignCls.getStructPk()), "pkSet"+r.getAlias());
				
				Var pk = doWhileQSqlQueryNext._declare(foreignCls.getStructPk(), "pk"+r.getAlias());
				for(Column colPk : r.getDestTable().getPrimaryKey().getColumns()) {
					doWhileQSqlQueryNext._assign(
							pk.accessAttr(colPk.getCamelCaseName()), 
							
							rec.callMethod("value", QString.fromStringConstant(r.getAlias()+"__"+ colPk.getName())).callMethod(BeanCls.getDatabaseMapper().getQVariantConvertMethod(colPk.getDbType())));
				}
				
				
//				IfBlock ifNotContains = 
						doWhileQSqlQueryNext._if(Expressions.not(pkSet.callMethod("contains", pk)))
						.addIfInstr(pkSet.callMethodInstruction("insert", pk))
						.addIfInstr(b1.callMethodInstruction(BeanCls.getRelatedBeanMethodName(r),_this().callGetByRecordMethod(foreignCls, rec, QString.fromStringConstant(r.getAlias()))));
//						.addIfInstr(b1.accessAttr(CodeUtil2.plural(r.getDestTable().getCamelCaseName())).callMethodInstruction("append",  _this().callGetByRecordMethod(foreignCls, rec, QString.fromStringConstant(r.getAlias()))));
				
			} else {
				Column colPk = r.getDestTable().getPrimaryKey().getColumns().get(0);
				Type type = BeanCls.getDatabaseMapper().columnToType(colPk);

				IfBlock ifNotRecValueIsNull = doWhileQSqlQueryNext._if(Expressions.not( rec.callMethod("value", QString.fromStringConstant(r.getAlias()+"__"+colPk.getName())).callMethod("isNull")));
				
				Var pkSet = ifQSqlQueryNext.thenBlock()._declare(Types.qset(type), "pkSet"+r.getAlias());
				Var pk = ifNotRecValueIsNull.thenBlock()._declare(
						type, 
						"pk"+r.getAlias(), 
						rec.callMethod("value", QString.fromStringConstant(r.getAlias()+"__"+colPk.getName())).callMethod(BeanCls.getDatabaseMapper().getQVariantConvertMethod(colPk.getDbType()))
						
						);
				ifNotRecValueIsNull.thenBlock()._if(Expressions.not(pkSet.callMethod("contains", pk)))
					.addIfInstr(pkSet.callMethodInstruction("insert", pk))
					.addIfInstr(b1.callMethodInstruction(BeanCls.getRelatedBeanMethodName(r),_this().callGetByRecordMethod(foreignCls, rec, QString.fromStringConstant(r.getAlias()))));
//						.addIfInstr(b1.accessAttr(PgCppUtil.getManyRelationDestAttrName(r))
//								.callMethodInstruction("append",  _this().callGetByRecordMethod(foreignCls,  rec, QString.fromStringConstant(r.getAlias()))));
			}
			
//			Var varForeach = new Var(, name);
//			doWhileQSqlQueryNext._foreach(var, collection);
//			//bCount++;
		}
		
		ifQSqlQueryNext.addIfInstr(doWhileQSqlQueryNext);
		doWhileQSqlQueryNext.setCondition(ifQSqlQueryNext.getCondition());
		_return(b1);
	}

}
