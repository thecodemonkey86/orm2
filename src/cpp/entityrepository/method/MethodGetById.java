package cpp.entityrepository.method;

import java.util.ArrayList;
import java.util.List;

import codegen.CodeUtil;
import cpp.Types;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.Type;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.Expression;
import cpp.core.expression.Expressions;
import cpp.core.expression.Var;
import cpp.core.instruction.DoWhile;
import cpp.core.instruction.IfBlock;
import cpp.entity.Entities;
import cpp.entity.EntityCls;
import cpp.entity.method.MethodOneRelationAttrSetter;
import cpp.entity.method.MethodOneRelationEntityIsNull;
import cpp.entityrepository.ClsEntityRepository;
import cpp.entityrepository.expression.ThisEntityRepositoryExpression;
import cpp.lib.ClsQVariant;
import cpp.lib.ClsSqlQuery;
import database.column.Column;
import database.relation.AbstractRelation;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import util.CodeUtil2;

public class MethodGetById extends Method {

	protected EntityCls bean;
	protected boolean addSortingParam;
	protected Param pOrderBy;
	
	public MethodGetById(EntityCls cls,boolean addSortingParams) {
		this(cls);
		this.addSortingParam = addSortingParams;
		
		if(addSortingParams) {
			pOrderBy = addParam(Types.QString.toConstRef(), "orderBy");
		}
		
	}
	public MethodGetById(EntityCls cls) {
//		super(Public, cls.toRawPointer(), "getById");
		super(Public, cls.toSharedPtr(), getMethodName(cls));
		for(Column col:cls.getTbl().getPrimaryKey().getColumns()) {
			Type colType =  EntityCls.getDatabaseMapper().columnToType(col);
			addParam(new Param(colType.isPrimitiveType() ? colType : colType.toConstRef(), col.getCamelCaseName()));
		}
//		setStatic(true);
		this.bean=cls;
	}
	public static String getMethodName(EntityCls cls) {
		return "get"+cls.getName()+"ById";
	}

	@Override
	public ThisEntityRepositoryExpression _this() {
		return new ThisEntityRepositoryExpression((ClsEntityRepository) parent);
	}
	
	@Override
	public void addImplementation() {
		if (bean.getName().equals("Order")) {
			System.out.println();
		}
		List<OneRelation> oneRelations = bean.getOneRelations();
		List<OneToManyRelation> oneToManyRelations = bean.getOneToManyRelations();
		List<ManyRelation> manyToManyRelations = bean.getManyToManyRelations();
		
		Attr aSqlCon = this.parent.getAttrByName(ClsEntityRepository.sqlCon);
		Var sqlQuery = _declareInitConstructor( EntityCls.getDatabaseMapper().getSqlQueryType(),"query",aSqlCon);
		
		
		ArrayList<String> selectFields = new ArrayList<>();
		for(Column col : bean.getTbl().getAllColumns()) {
			selectFields.add("e1." + col.getEscapedName() + " as e1__" + col.getName());
		}
		List<AbstractRelation> allRelations = new ArrayList<>(oneRelations.size()+oneToManyRelations.size()+manyToManyRelations.size());
		allRelations.addAll(oneRelations);
		allRelations.addAll(oneToManyRelations);
		allRelations.addAll(manyToManyRelations);
		
		for(AbstractRelation r:allRelations) {
			for(Column col : r.getDestTable().getAllColumns()) {
				selectFields.add( r.getAlias()+"." + col.getEscapedName() + " as "+ r.getAlias()+"__" + col.getName());
			}
		}
		Expression exprQSqlQuery = sqlQuery.callMethod("select", QString.fromStringConstant(CodeUtil.commaSep( selectFields) ))
									.callMethod("from", bean.callStaticMethod("getTableName",QString.fromStringConstant("e1")));
		
		for(OneRelation r:oneRelations) {
			ArrayList<String> joinConditions=new ArrayList<>();
			for(int i=0;i<r.getColumnCount();i++) {
				joinConditions.add(CodeUtil.sp("e1."+r.getColumns(i).getValue1().getEscapedName(),'=',(r.getAlias())+"."+ r.getColumns(i).getValue2().getEscapedName()));
			}
			
			exprQSqlQuery = exprQSqlQuery.callMethod("leftJoin", QString.fromExpression(Entities.get(r.getDestTable()).callStaticMethod("getTableName")),QString.fromStringConstant(r.getAlias()), QString.fromStringConstant(CodeUtil2.concat(joinConditions," AND ")));
		}
		for(OneToManyRelation r:oneToManyRelations) {
			ArrayList<String> joinConditions=new ArrayList<>();
			for(int i=0;i<r.getColumnCount();i++) {
				joinConditions.add(CodeUtil.sp("e1."+r.getColumns(i).getValue1().getEscapedName(),'=',(r.getAlias())+"."+ r.getColumns(i).getValue2().getEscapedName()));
			}
			
			exprQSqlQuery = exprQSqlQuery.callMethod("leftJoin", QString.fromExpression(Entities.get(r.getDestTable()).callStaticMethod("getTableName")),QString.fromStringConstant(r.getAlias()), QString.fromStringConstant(CodeUtil2.concat(joinConditions," AND ")));
		}
		for(ManyRelation r:manyToManyRelations) {
			ArrayList<String> joinConditions=new ArrayList<>();
			for(int i=0;i<r.getSourceColumnCount();i++) {
				joinConditions.add(CodeUtil.sp("e1."+r.getSourceEntityColumn(i).getEscapedName(),'=',r.getAlias("mapping")+"."+ r.getSourceMappingColumn(i).getEscapedName()));
			}
			
			exprQSqlQuery = exprQSqlQuery.callMethod("leftJoin", QString.fromStringConstant(r.getMappingTable().getName()),QString.fromStringConstant(r.getAlias("mapping")), QString.fromStringConstant(CodeUtil2.concat(joinConditions," AND ")));
			
			joinConditions.clear();
			for(int i=0;i<r.getDestColumnCount();i++) {
				joinConditions.add(CodeUtil.sp(r.getAlias("mapping")+"."+r.getDestMappingColumn(i).getEscapedName(),'=',r.getAlias()+"."+r.getDestEntityColumn(i).getEscapedName() ));
			}
			
			exprQSqlQuery = exprQSqlQuery.callMethod("leftJoin", Entities.get(r.getDestTable()).callStaticMethod("getTableName"),QString.fromStringConstant(r.getAlias()), QString.fromStringConstant(CodeUtil2.concat(joinConditions," AND ")));
			
			//bCount++;
		}

		
		for(Column col:bean.getTbl().getPrimaryKey().getColumns()) {
			exprQSqlQuery = exprQSqlQuery.callMethod("where", QString.fromStringConstant("e1."+ col.getEscapedName()+"=?"),getParam(col.getCamelCaseName()));
					
		}
		
		if(addSortingParam) {
		
			exprQSqlQuery = exprQSqlQuery.callMethod(ClsSqlQuery.orderBy,pOrderBy);
		}
		
		exprQSqlQuery = exprQSqlQuery.callMethod("execQuery");
		Var qSqlQuery = _declare(exprQSqlQuery.getType(),
				"qSqlQuery", exprQSqlQuery
				);
		Var e1 = _declare(returnType, "e1", Expressions.Nullptr);
		IfBlock ifQSqlQueryNext =
				_if(qSqlQuery.callMethod("next"))
					.setIfInstr(
							e1.assign(_this().callMethod(MethodGetFromRecord.getMethodName(bean),  qSqlQuery.callMethod("record"), QString.fromStringConstant("e1")))
							,
							e1.callSetterMethodInstruction("loaded", BoolExpression.TRUE)//_assignInstruction(e1.accessAttr("loaded"), BoolExpression.TRUE)
							);
		
		if(bean.hasRelations()) {
		DoWhile doWhileQSqlQueryNext = DoWhile.create();
		Var rec = doWhileQSqlQueryNext._declare(Types.QSqlRecord, "rec",qSqlQuery.callMethod("record") );
//		//bCount = 2;
		
		ArrayList<AbstractRelation> manyRelations = new ArrayList<>(oneToManyRelations.size()+manyToManyRelations.size());
		manyRelations.addAll(oneToManyRelations);
		manyRelations.addAll(manyToManyRelations);
		
		for(OneRelation r:oneRelations) {
			EntityCls foreignCls = Entities.get(r.getDestTable()); 
			//AccessExpression acc = e1.accessAttr(PgCppUtil.getOneRelationDestAttrName(r));
			//IfBlock ifBlock= doWhileQSqlQueryNext._if(acc.isNull());
			IfBlock ifBlock= doWhileQSqlQueryNext._if(Expressions.and( e1.callMethod(new MethodOneRelationEntityIsNull(r))
					,
					Expressions.not( rec.callMethod("value", QString.fromStringConstant(r.getAlias()+"__"+ r.getDestTable().getPrimaryKey().getFirstColumn().getName())).callMethod(ClsQVariant.isNull))
					));
			ifBlock.thenBlock().
			_callMethodInstr(e1, new MethodOneRelationAttrSetter( bean,r, true),  _this().callGetByRecordMethod(foreignCls, rec, QString.fromStringConstant(r.getAlias())));
			//ifBlock.getIfInstr()._assign(acc, _this().callGetByRecordMethod(foreignCls, rec, QString.fromStringConstant(r.getAlias())));
//			//bCount++;
		}
		for(AbstractRelation r:manyRelations) {
			EntityCls foreignCls = Entities.get(r.getDestTable()); 
			if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
				Var pkSet = ifQSqlQueryNext.thenBlock()._declare(Types.qset(foreignCls.getStructPk()), "pkSet"+r.getAlias());
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
						.addIfInstr(e1.callMethodInstruction(EntityCls.getRelatedBeanMethodName(r),_this().callGetByRecordMethod(foreignCls, rec, QString.fromStringConstant(r.getAlias()))));
//						.addIfInstr(e1.accessAttr(CodeUtil2.plural(r.getDestTable().getCamelCaseName())).callMethodInstruction("append",  _this().callGetByRecordMethod(foreignCls, rec, QString.fromStringConstant(r.getAlias()))));
				
			} else {
				Column colPk = r.getDestTable().getPrimaryKey().getColumns().get(0);
				Type type = EntityCls.getDatabaseMapper().columnToType(colPk);

				IfBlock ifNotPkForeignIsNull = doWhileQSqlQueryNext._if(Expressions.not( rec.callMethod("value", QString.fromStringConstant(r.getAlias()+"__"+colPk.getName())).callMethod(ClsQVariant.isNull)));
				
				Var pkSet = ifQSqlQueryNext.thenBlock()._declare(Types.qset(type), "pkSet"+r.getAlias());
				Var pk = ifNotPkForeignIsNull.thenBlock()._declare(
						type, 
						"pk"+r.getAlias(), 
						rec.callMethod("value", QString.fromStringConstant(r.getAlias()+"__"+colPk.getName())).callMethod(EntityCls.getDatabaseMapper().getQVariantConvertMethod(colPk))
						
						);
				ifNotPkForeignIsNull.thenBlock()._if(Expressions.not(pkSet.callMethod("contains", pk)))
					.addIfInstr(pkSet.callMethodInstruction("insert", pk))
					.addIfInstr(e1.callMethodInstruction(EntityCls.getRelatedBeanMethodName(r),_this().callGetByRecordMethod(foreignCls, rec, QString.fromStringConstant(r.getAlias()))));
//						.addIfInstr(e1.accessAttr(PgCppUtil.getManyRelationDestAttrName(r))
//								.callMethodInstruction("append",  _this().callGetByRecordMethod(foreignCls,  rec, QString.fromStringConstant(r.getAlias()))));
			}
			
//			Var varForeach = new Var(, name);
//			doWhileQSqlQueryNext._foreach(var, collection);
//			//bCount++;
		}
		
		ifQSqlQueryNext.addIfInstr(doWhileQSqlQueryNext);
		
		doWhileQSqlQueryNext.setCondition(ifQSqlQueryNext.getCondition());
		}
		_return(e1);
	}

}
