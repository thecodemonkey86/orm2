package cpp.bean.method;

import generate.CodeUtil2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import codegen.CodeUtil;
import util.StringUtil;
import util.pg.PgCppUtil;
import cpp.Types;
import cpp.bean.BeanCls;
import cpp.bean.Beans;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.core.QString;
import cpp.core.Type;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.Expression;
import cpp.core.expression.Expressions;
import cpp.core.expression.QChar;
import cpp.core.expression.Var;
import cpp.core.instruction.Comment;
import cpp.core.instruction.ForeachLoop;
import cpp.core.instruction.IfBlock;
import cpp.core.instruction.While;
import cpp.orm.OrmUtil;
import database.column.Column;
import database.relation.AbstractRelation;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import database.relation.PrimaryKey;
import database.table.Table;

public class MethodLoad2Levels extends Method {

	protected List<OneRelation> oneRelations;
	protected List<OneToManyRelation> oneToManyRelations;
	protected List<ManyRelation> manyRelations;
	protected PrimaryKey primaryKey;

	public MethodLoad2Levels(List<OneRelation> oneRelations,
			List<OneToManyRelation> oneToManyRelations,
			List<ManyRelation> manyRelations, Table tbl) {
		super(Public, Types.Void, "load2Levels");

		this.oneRelations = oneRelations;
		this.oneToManyRelations = oneToManyRelations;
		this.manyRelations = manyRelations;
		this.primaryKey = tbl.getPrimaryKey();

		setConstQualifier(true);
	}

	
	@Override
	public boolean includeIfEmpty() {
		return true;
	}

	protected void addLevel1GetSelectFields() {
		
	}
	
	@Override
	public void addImplementation() {
		BeanCls parent = (BeanCls) this.parent;
		Expression _this = parent._this();
		Attr aSqlCon = parent.getAttrByName("sqlCon");

		Method mBuildQuery = aSqlCon.getClassType().getMethod("buildQuery");
		Var sqlQuery = _declare(mBuildQuery.getReturnType(), "query",
				aSqlCon.callMethod(mBuildQuery));

		ArrayList<Expression> selectFields = new ArrayList<>();
		selectFields.add(parent.callStaticMethod("getSelectFields",
				QString.fromStringConstant("b1")));


		List<AbstractRelation> allRelations = new ArrayList<>(
				oneRelations.size() + oneToManyRelations.size()
						+ manyRelations.size());
		allRelations.addAll(oneRelations);
		allRelations.addAll(oneToManyRelations);
		allRelations.addAll(manyRelations);
		// level 1 get select fields
		for (OneRelation r : oneRelations) {
			selectFields.add(Beans.get(r.getDestTable())
					.callStaticMethod("getSelectFields",
							QString.fromStringConstant(r.getAlias())));

		}
		for (OneToManyRelation r : oneToManyRelations) {
			selectFields.add(Beans.get(r.getDestTable())
					.callStaticMethod("getSelectFields",
							QString.fromStringConstant(r.getAlias())));

		}
		for (AbstractRelation r : manyRelations) {
			selectFields.add(Beans.get(r.getDestTable())
					.callStaticMethod("getSelectFields",
							QString.fromStringConstant(r.getAlias())));
		}
		// end level 1 get select fields
		
		// level 2 get select fields
		for (AbstractRelation r : allRelations) {
			BeanCls destBean = Beans.get(r.getDestTable());

			for (OneRelation rDest : destBean.getOneRelations()) {
				selectFields.add(Beans.get(rDest.getDestTable())
						.callStaticMethod(
								"getSelectFields",
								QString.fromStringConstant(rDest
										.getAlias("l2"))));

			}
			for (OneToManyRelation rDest : destBean.getOneToManyRelations()) {
				selectFields.add(Beans.get(rDest.getDestTable())
						.callStaticMethod(
								"getSelectFields",
								QString.fromStringConstant(rDest
										.getAlias("l2"))));

			}
			for (AbstractRelation rDest : destBean.getManyRelations()) {
				selectFields.add(Beans.get(rDest.getDestTable())
						.callStaticMethod(
								"getSelectFields",
								QString.fromStringConstant(rDest
										.getAlias("l2"))));
			}
		}
		// end level 2 get select fields
		
		// level 1 from / joins
		Expression exprQSqlQuery = sqlQuery.callMethod("select",
				Expressions.concat(QChar.fromChar(','), selectFields))
				.callMethod(
						"from",
						QString.fromExpression(parent.callStaticMethod(
								"getTableName",
								QString.fromStringConstant("b1"))));

		for (OneRelation r : oneRelations) {
			ArrayList<String> joinConditions = new ArrayList<>();
			for (int i = 0; i < r.getColumnCount(); i++) {
				joinConditions
						.add(CodeUtil.sp("b1."
								+ r.getColumns(i).getValue1().getEscapedName(),
								'=', r.getAlias()
										+ "."
										+ r.getColumns(i).getValue2()
												.getEscapedName()));
			}

			exprQSqlQuery = exprQSqlQuery.callMethod("leftJoin", QString
					.fromExpression(Beans.get(r.getDestTable())
							.callStaticMethod("getTableName")), QString
					.fromStringConstant(r.getAlias()), QString
					.fromStringConstant(CodeUtil2.concat(joinConditions,
							" AND ")));
		}
		for (OneToManyRelation r : oneToManyRelations) {
			ArrayList<String> joinConditions = new ArrayList<>();
			for (int i = 0; i < r.getColumnCount(); i++) {
				joinConditions
						.add(CodeUtil.sp("b1."
								+ r.getColumns(i).getValue1().getEscapedName(),
								'=', r.getAlias()
										+ "."
										+ r.getColumns(i).getValue2()
												.getEscapedName()));
			}

			exprQSqlQuery = exprQSqlQuery.callMethod("leftJoin", QString
					.fromExpression(Beans.get(r.getDestTable())
							.callStaticMethod("getTableName")), QString
					.fromStringConstant(r.getAlias()), QString
					.fromStringConstant(CodeUtil2.concat(joinConditions,
							" AND ")));
		}

		for (ManyRelation r : manyRelations) {
			ArrayList<String> joinConditionsMappingDest = new ArrayList<>();
			ArrayList<String> joinConditionsB1Mapping = new ArrayList<>();
			for (int i = 0; i < r.getSourceColumnCount(); i++) {
				joinConditionsB1Mapping.add(CodeUtil.sp("b1."
						+ r.getSourceEntityColumn(i)
								.getEscapedName(), '=', r.getMappingAlias()
						+ "."
						+ r.getSourceMappingColumn(i)
								.getEscapedName()));


			}
			for (int i = 0; i < r.getDestColumnCount(); i++) {

				joinConditionsMappingDest.add(CodeUtil.sp(r.getMappingAlias()
						+ "."
						+ r.getDestMappingColumn(i)
								.getEscapedName(), '=', r.getAlias()
						+ "."
						+ r.getDestEntityColumn(i)
								.getEscapedName()));
			}
			exprQSqlQuery = exprQSqlQuery.callMethod("leftJoin", QString
					.fromStringConstant(r.getMappingTable().getName()), QString
					.fromStringConstant(r.getMappingAlias()), QString
					.fromStringConstant(CodeUtil2.concat(
							joinConditionsB1Mapping, " AND ")));
			exprQSqlQuery = exprQSqlQuery.callMethod("leftJoin",
					Beans.get(r.getDestTable())
							.callStaticMethod("getTableName"), QString
							.fromStringConstant(r.getAlias()), QString
							.fromStringConstant(CodeUtil2.concat(
									joinConditionsMappingDest, " AND ")));

		}
		// end level 1 from / joins
		
		
		// level 2 joins
		for (AbstractRelation r : allRelations) {
			BeanCls srcBean = Beans.get(r.getSourceTable());
			
			if (srcBean.getName().equals("OrderItem")) {
				System.out.println();
			}
			
			BeanCls destBean = Beans.get(r.getDestTable());
			
			for (OneRelation r2ndLevel : destBean.getOneRelations()) {
				ArrayList<String> joinConditions = new ArrayList<>();
				for (int i = 0; i < r2ndLevel.getColumnCount(); i++) {
					joinConditions.add(CodeUtil.sp(r.getAlias()+ "."
							+ r2ndLevel.getColumns(i).getValue1().getEscapedName(),
							'=',
							r2ndLevel.getAlias("l2")
									+ "."
									+ r2ndLevel.getColumns(i).getValue2()
											.getEscapedName()));
				}

				exprQSqlQuery = exprQSqlQuery.callMethod("leftJoin", QString
						.fromExpression(Beans.get(r2ndLevel.getDestTable())
								.callStaticMethod("getTableName")), QString
						.fromStringConstant(r2ndLevel.getAlias("l2")), QString
						.fromStringConstant(CodeUtil2.concat(joinConditions,
								" AND ")));
			}
			for (OneToManyRelation r2ndLevel : destBean.getOneToManyRelations()) {
				ArrayList<String> joinConditions = new ArrayList<>();
				for (int i = 0; i < r2ndLevel.getColumnCount(); i++) {
					joinConditions.add(CodeUtil.sp(r.getAlias()+"."
							+ r2ndLevel.getColumns(i).getValue1().getEscapedName(),
							'=',
							r2ndLevel.getAlias("l2")
									+ "."
									+ r2ndLevel.getColumns(i).getValue2()
											.getEscapedName()));
				}

				exprQSqlQuery = exprQSqlQuery.callMethod("leftJoin", QString
						.fromExpression(Beans.get(r2ndLevel.getDestTable())
								.callStaticMethod("getTableName")), QString
						.fromStringConstant(r2ndLevel.getAlias("l2")), QString
						.fromStringConstant(CodeUtil2.concat(joinConditions,
								" AND ")));
			}
			for (ManyRelation r2ndLevel : destBean.getManyRelations()) {
				ArrayList<String> joinConditionsMappingDest = new ArrayList<>();
				ArrayList<String> joinConditionsB1Mapping = new ArrayList<>();
				for (int i = 0; i < r2ndLevel.getSourceColumnCount(); i++) {
					joinConditionsB1Mapping.add(CodeUtil.sp(r.getAlias()+"."
							+ r2ndLevel.getSourceEntityColumn(i)
									.getEscapedName(), '=',
							r2ndLevel.getMappingAlias(r.getAlias())
									+ "."
									+ r2ndLevel.getSourceMappingColumn(i)
										.getEscapedName()));


				}
				for (int i = 0; i < r2ndLevel.getDestColumnCount(); i++) {

					joinConditionsMappingDest.add(CodeUtil.sp(
							r2ndLevel.getMappingAlias(r.getAlias())
									+ "."
									+ r2ndLevel.getDestMappingColumn(i)
									.getEscapedName(), '=',
							r2ndLevel.getAlias("l2")
									+ "."
									+ r2ndLevel.getDestEntityColumn(i)
									.getEscapedName()));
				}
				exprQSqlQuery = exprQSqlQuery.callMethod("leftJoin", QString
						.fromStringConstant(r2ndLevel.getMappingTable().getName()),
						QString.fromStringConstant(r2ndLevel.getMappingAlias(r.getAlias())),
						QString.fromStringConstant(CodeUtil2.concat(
								joinConditionsB1Mapping, " AND ")));
				exprQSqlQuery = exprQSqlQuery.callMethod(
						"leftJoin",
						Beans.get(r2ndLevel.getDestTable()).callStaticMethod(
								"getTableName"), QString.fromStringConstant(r2ndLevel
								.getAlias("l2")), QString
								.fromStringConstant(CodeUtil2.concat(
										joinConditionsMappingDest, " AND ")));

			}
		}
		// end level 2 joins

		for (Column col : primaryKey.getColumns()) {

			exprQSqlQuery = exprQSqlQuery.callMethod(
					"where",
					QString.fromStringConstant("b1." + col.getEscapedName()
							+ "=?"), parent.accessThisAttrGetterByColumn(col));

		}
		exprQSqlQuery = exprQSqlQuery.callMethod("execQuery");
		Var qSqlQuery = _declare(exprQSqlQuery.getType(), "qSqlQuery",
				exprQSqlQuery);

		// level 1 primary key hashsets
		HashMap<String, Var> pkSets = new HashMap<>();
		for (OneToManyRelation r : oneToManyRelations) {
			BeanCls foreignCls = Beans.get(r.getDestTable());
			if (r.getDestTable().getPrimaryKey().isMultiColumn()) {
				Var pkSet = _declare(Types.qset(foreignCls.getStructPk()),
						"pkSetB" + r.getAlias());
				pkSets.put(r.getAlias(), pkSet);
			} else {
				Column colPk = r.getDestTable().getPrimaryKey().getColumns()
						.get(0);
				Type type = BeanCls.getDatabaseMapper().columnToType(colPk);
				Var pkSet = _declare(Types.qset(type), "pkSetB" + r.getAlias());
				pkSets.put(r.getAlias(), pkSet);
			}
		}
		for (ManyRelation r : manyRelations) {
			BeanCls foreignCls = Beans.get(r.getDestTable());
			if (r.getDestTable().getPrimaryKey().isMultiColumn()) {
				Var pkSet = _declare(Types.qset(foreignCls.getStructPk()),
						"pkSetB" + r.getAlias());
				pkSets.put(r.getAlias(), pkSet);
			} else {
				Column colPk = r.getDestTable().getPrimaryKey().getFirstColumn();
				Type type = BeanCls.getDatabaseMapper().columnToType(colPk);
				Var pkSet = _declare(Types.qset(type), "pkSetB" + r.getAlias());
				pkSets.put(r.getAlias(), pkSet);
			}
		}
		// end level 1 primary key hashsets

		// level 2 primary key hashsets
		/*for (AbstractRelation r : allRelations) {
			BeanCls destBean = Beans.get(r.getDestTable());
			ArrayList<OneRelation> secondLevelOneRelations = new ArrayList<>(destBean.getOneRelations().size()+destBean.getOneToManyRelations().size());
			secondLevelOneRelations.addAll(destBean.getOneRelations());
			secondLevelOneRelations.addAll(destBean.getOneToManyRelations());
			for (OneRelation rDest : secondLevelOneRelations) {
				BeanCls foreignCls = Beans.get(rDest.getDestTable());
				if (rDest.getDestTable().getPrimaryKey().isMultiColumn()) {
					Var pkSet = _declare(Types.qset(foreignCls.getStructPk()),
							"pkSetB" + rDest.getAlias("l2"));
					pkSets.put(rDest.getAlias("l2"), pkSet);
				} else {
					Column colPk = rDest.getDestTable().getPrimaryKey().getColumns()
							.get(0);
					Type type = BeanCls.getDatabaseMapper().columnToType(colPk);
					Var pkSet = _declare(Types.qset(type), "pkSetB" + rDest.getAlias("l2"));
					pkSets.put(rDest.getAlias("l2"), pkSet);
				}
			}
			for (ManyRelation rDest : destBean.getManyRelations()) {
				BeanCls foreignCls = Beans.get(rDest.getDestTable());
				if (rDest.getDestTable().getPrimaryKey().isMultiColumn()) {
					Var pkSet = _declare(Types.qset(foreignCls.getStructPk()),
							"pkSetB" + rDest.getAlias("l2"));
					pkSets.put(rDest.getAlias("l2"), pkSet);
				} else {
					Column colPk = rDest.getDestTable().getPrimaryKey().getColumns()
							.get(0);
					Type type = BeanCls.getDatabaseMapper().columnToType(colPk);
					Var pkSet = _declare(Types.qset(type), "pkSetB" + rDest.getAlias("l2"));
					pkSets.put(rDest.getAlias("l2"), pkSet);
				}
			}
		}*/
		// end level 2 primary key hashsets
		
		While doWhileQSqlQueryNext = _while(qSqlQuery.callMethod("next"));
		Var rec = doWhileQSqlQueryNext._declare(Types.QSqlRecord, "rec",
				qSqlQuery.callMethod("record"));
		// level 1 loop
		for (OneToManyRelation r : oneToManyRelations) {
			Var pkSet = pkSets.get(r.getAlias());
			BeanCls foreignCls = Beans.get(r.getDestTable());
			if (r.getDestTable().getPrimaryKey().isMultiColumn()) {

				Var pk = doWhileQSqlQueryNext._declare(
						foreignCls.getStructPk(), "pk" + r.getAlias());
				for (Column colPk : r.getDestTable().getPrimaryKey()
						.getColumns()) {
					doWhileQSqlQueryNext
							._assign(
									pk.accessAttr(colPk.getCamelCaseName()),

									rec.callMethod(
											"value",
											QString.fromStringConstant(r
													.getAlias()
													+ "__"
													+ colPk.getName()))
											.callMethod(
													BeanCls.getDatabaseMapper()
															.getQVariantConvertMethod(
																	colPk.getDbType())));
				}

				doWhileQSqlQueryNext
						._if(Expressions.not(pkSet.callMethod("contains", pk)))
						.addIfInstr(pkSet.callMethodInstruction("insert", pk))
						.addIfInstr(
								_this.accessAttr(
										CodeUtil2.plural(r.getDestTable()
												.getCamelCaseName()))
										.callMethodInstruction(
												"append",
												foreignCls.callStaticMethod(
														"getByRecord",
														aSqlCon,
														rec,
														QString.fromStringConstant(r
																.getAlias()))));

			} else {

				Column colPk = r.getDestTable().getPrimaryKey().getFirstColumn();
				Expression recValueColPk = rec.callMethod(
						"value",
						QString.fromStringConstant(r.getAlias() + "__"
								+ colPk.getName()));
				Var pk = doWhileQSqlQueryNext._declare(recValueColPk.getType(),
						"pk" + r.getAlias(), recValueColPk);

				doWhileQSqlQueryNext
						._if(Expressions.and(Expressions.not(pk
								.callMethod("isNull")), Expressions.not(pkSet
								.callMethod("contains", pk.callMethod(BeanCls
										.getDatabaseMapper()
										.getQVariantConvertMethod(
												colPk.getDbType()))))))
						.addIfInstr(
								pkSet.callMethodInstruction("insert", pk
										.callMethod(BeanCls.getDatabaseMapper()
												.getQVariantConvertMethod(
														colPk.getDbType()))))
						.addIfInstr(
								_this.accessAttr(
										CodeUtil2.plural(r.getDestTable()
												.getCamelCaseName()))
										.callMethodInstruction(
												"append",
												foreignCls.callStaticMethod(
														"getByRecord",
														aSqlCon,
														rec,
														QString.fromStringConstant(r
																.getAlias()))));
			}

		}
		for (ManyRelation r : manyRelations) {
			Var pkSet = pkSets.get(r.getAlias());
			BeanCls foreignCls = Beans.get(r.getDestTable());
			if (r.getDestTable().getPrimaryKey().isMultiColumn()) {

				Var pk = doWhileQSqlQueryNext._declare(
						foreignCls.getStructPk(), "pk" + r.getAlias());
				for (Column colPk : r.getDestTable().getPrimaryKey()
						.getColumns()) {
					doWhileQSqlQueryNext
							._assign(
									pk.accessAttr(colPk.getCamelCaseName()),

									rec.callMethod(
											"value",
											QString.fromStringConstant(r
													.getAlias()
													+ "__"
													+ colPk.getName()))
											.callMethod(
													BeanCls.getDatabaseMapper()
															.getQVariantConvertMethod(
																	colPk.getDbType())));
				}

				doWhileQSqlQueryNext
						._if(Expressions.not(pkSet.callMethod("contains", pk)))
						.addIfInstr(pkSet.callMethodInstruction("insert", pk))
						.addIfInstr(
								_this.accessAttr(
										OrmUtil.getManyRelationDestAttrName(r))
										.callMethodInstruction(
												"append",
												foreignCls.callStaticMethod(
														"getByRecord",
														aSqlCon,
														rec,
														QString.fromStringConstant(r
																.getAlias()))));

			} else {

				Column colPk = r.getDestTable().getPrimaryKey().getColumns()
						.get(0);
				Expression recValueColPk = rec.callMethod(
						"value",
						QString.fromStringConstant(r.getAlias() + "__"
								+ colPk.getName()));
				Var pk = doWhileQSqlQueryNext._declare(recValueColPk.getType(),
						"pk" + r.getAlias(), recValueColPk);

				doWhileQSqlQueryNext
						._if(Expressions.and(Expressions.not(pk
								.callMethod("isNull")), Expressions.not(pkSet
								.callMethod("contains", pk.callMethod(BeanCls
										.getDatabaseMapper()
										.getQVariantConvertMethod(
												colPk.getDbType()))))))
						.addIfInstr(
								pkSet.callMethodInstruction("insert", pk
										.callMethod(BeanCls.getDatabaseMapper()
												.getQVariantConvertMethod(
														colPk.getDbType()))))
						.addIfInstr(
								_this.accessAttr(
										CodeUtil2.plural(r.getDestTable()
												.getCamelCaseName()))
										.callMethodInstruction(
												"append",
												foreignCls.callStaticMethod(
														"getByRecord",
														aSqlCon,
														rec,
														QString.fromStringConstant(r
																.getAlias()))));
			}
		}
		for (OneRelation r : oneRelations) {
			BeanCls foreignCls = Beans.get(r.getDestTable());
			try {
				Expression acc = _this.accessAttr(
						PgCppUtil.getOneRelationDestAttrName(r), true);
				IfBlock ifBlock = doWhileQSqlQueryNext._if(_this.callMethod(
						MethodOneRelationBeanIsNull.getMethodName(r))
						);
				ifBlock.thenBlock()._assign(
						acc,
						foreignCls.callStaticMethod("getByRecord", aSqlCon,
								rec, QString.fromStringConstant(r.getAlias())));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// end level 1 loop

		// level 2 loop
		for (AbstractRelation r : allRelations) {
			BeanCls destBean = Beans.get(r.getDestTable());
			// level2 1 -> 1
			for (OneRelation r2ndLevel : destBean.getOneRelations()) {
				BeanCls destBean2ndLevel = Beans.get(r2ndLevel.getDestTable());
				Var varDestBean2ndLevel = new Var(destBean2ndLevel.toSharedPtr(), PgCppUtil.getOneRelationDestAttrName(r2ndLevel));
				Expression varDestBean2ndLevelInit = destBean2ndLevel.callStaticMethod("getByRecord", aSqlCon,rec, QString.fromStringConstant(r2ndLevel.getAlias("l2")));
				
				// level 1, 1 -> 1 | level2 1 -> 1
				if (r instanceof OneRelation) {
					try {
						doWhileQSqlQueryNext.addInstr(new Comment("r: level 1, 1 -> 1 | level2 1 -> 1: "+r.getDestTable().getName()+"-> r2:"+r2ndLevel.getDestTable().getName()));
						Expression acc = _this.accessAttr(
								PgCppUtil.getOneRelationDestAttrName((OneRelation) r), true);
						IfBlock ifBlock = doWhileQSqlQueryNext
								._if(_this
										.accessAttr(
												PgCppUtil
														.getOneRelationDestAttrName((OneRelation) r),
												false).callMethod(MethodOneRelationBeanIsNull
														.getMethodName(r2ndLevel)));
						ifBlock.thenBlock()._declare(varDestBean2ndLevel, varDestBean2ndLevelInit);
						ifBlock.thenBlock()._callMethodInstr(
								acc,MethodAttrSetterInternal
										.getMethodName(r2ndLevel),varDestBean2ndLevel)
								;
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					// end level 1, 1 -> 1, 1 -> many | level2 1 -> 1
				} else if (r instanceof OneToManyRelation ) {
					// level 1: 1 -> many | level2 1 -> 1
					doWhileQSqlQueryNext.addInstr(new Comment("r: level 1: 1 -> many | level2 1 -> 1: "+r.getDestTable().getName()+"-> r2:"+r2ndLevel.getDestTable().getName()));
					Expression acc = _this.accessAttr(
							OrmUtil.getOneToManyRelationDestAttrName((OneToManyRelation) r), true);
					ForeachLoop foreachLevel1Relation = doWhileQSqlQueryNext._foreach(new Var(destBean.toSharedPtr().toRef(), CodeUtil2.camelCase(destBean.getName())), acc);
					
					// for multi or single column primary key
					ArrayList<Expression> checkLevel1PkExpressions = new ArrayList<>();
					
					for(Column colPk : r.getDestTable().getPrimaryKey().getColumns()) {
						Expression recValueColPkLevel1 = rec.callMethod(
								"value",
								QString.fromStringConstant(r.getAlias() + "__"
										+ colPk.getName())).callMethod(
												BeanCls.getDatabaseMapper()
												.getQVariantConvertMethod(
														colPk.getDbType()));
						checkLevel1PkExpressions.add(foreachLevel1Relation.getVar().callMethod(BeanCls.getAccessMethodNameByColumn(colPk))._equals(recValueColPkLevel1));
					}
					IfBlock ifBlockForeachLevel1Relation = foreachLevel1Relation.
							_if(Expressions.and(checkLevel1PkExpressions));
				
					ifBlockForeachLevel1Relation.addIfInstr(foreachLevel1Relation.getVar().callMethodInstruction(MethodAttrSetterInternal.getMethodName(r2ndLevel), destBean2ndLevel.callStaticMethod(
							"getByRecord",
							aSqlCon,
							rec,
							QString.fromStringConstant(r2ndLevel
									.getAlias("l2")))));
					// end level 1: 1 -> many | level2 1 -> 1
				} else if (r instanceof ManyRelation){
					// level 1: many -> many | level2 1 -> 1
					doWhileQSqlQueryNext.addInstr(new Comment("r: level 1: 1 -> many | level2 1 -> 1: "+r.getDestTable().getName()+"-> r2:"+r2ndLevel.getDestTable().getName()));
					Expression acc = _this.accessAttr(
							OrmUtil.getManyRelationDestAttrName((ManyRelation) r), true);
					ForeachLoop foreachLevel1Relation = doWhileQSqlQueryNext._foreach(new Var(destBean.toSharedPtr().toRef(), CodeUtil2.camelCase(destBean.getName())), acc);
					
					// for multi or single column primary key
					ArrayList<Expression> checkLevel1PkExpressions = new ArrayList<>();
					
					for(Column colPk : r.getDestTable().getPrimaryKey().getColumns()) {
						Expression recValueColPkLevel1 = rec.callMethod(
								"value",
								QString.fromStringConstant(r.getAlias() + "__"
										+ colPk.getName())).callMethod(
												BeanCls.getDatabaseMapper()
												.getQVariantConvertMethod(
														colPk.getDbType()));
						checkLevel1PkExpressions.add(foreachLevel1Relation.getVar().callMethod(BeanCls.getAccessMethodNameByColumn(colPk))._equals(recValueColPkLevel1));
					}
					IfBlock ifBlockForeachLevel1Relation = foreachLevel1Relation.
							_if(Expressions.and(checkLevel1PkExpressions));
					
					ifBlockForeachLevel1Relation.addIfInstr(foreachLevel1Relation.getVar().callMethodInstruction(MethodAttrSetterInternal.getMethodName(r2ndLevel), destBean2ndLevel.callStaticMethod(
							"getByRecord",
							aSqlCon,
							rec,
							QString.fromStringConstant(r2ndLevel
									.getAlias("l2")))));
					// end level 1: many -> many | level2 1 -> 1
				}
				// level 1, many -> many | level2 1 -> 1
			}
			// end level2 1 -> 1
			
			// level2 1 -> many
			for(OneToManyRelation r2ndLevel : destBean.getOneToManyRelations()) {
				BeanCls destBean2ndLevel = Beans.get(r2ndLevel.getDestTable());
				
				// level 1: 1 -> 1, 1 -> many | level2 1 -> many
				if (r instanceof OneRelation) {
					Expression acc = _this.accessAttr(
							PgCppUtil.getOneRelationDestAttrName((OneRelation) r), true);
					Var varForeach = new Var(destBean2ndLevel.toSharedPtr().toRef(), 
							CodeUtil2.camelCase(destBean2ndLevel.getName()));
					Var foundLevel2Many = doWhileQSqlQueryNext._declare(Types.Bool, "found"+CodeUtil2.uc1stCamelCase(varForeach.getName()),BoolExpression.FALSE);
					
					ForeachLoop foreachLevel2Relation = doWhileQSqlQueryNext._foreach(
							varForeach, acc.callMethod(MethodAttrGetter.getMethodName(r2ndLevel)));
					
					ArrayList<Expression> checkLevel2PkExpressions = new ArrayList<>();
					
					for(Column colPk : r.getDestTable().getPrimaryKey().getColumns()) {
						Expression recValueColPkLevel2 = rec.callMethod(
								"value",
								QString.fromStringConstant(r2ndLevel.getAlias("l2") + "__"
										+ colPk.getName())).callMethod(
												BeanCls.getDatabaseMapper()
												.getQVariantConvertMethod(
														colPk.getDbType()));
						checkLevel2PkExpressions.add(foreachLevel2Relation.getVar().callMethod(BeanCls.getAccessMethodNameByColumn(colPk))._equals(recValueColPkLevel2));
					}
					
					foreachLevel2Relation._if(Expressions.and(checkLevel2PkExpressions)).
					setIfInstr(foundLevel2Many.assign(BoolExpression.TRUE));
					doWhileQSqlQueryNext._if(Expressions.not(foundLevel2Many)).setIfInstr(
							acc.callMethod(MethodAttrGetter.getMethodName(r2ndLevel)).callMethod("append", destBean2ndLevel.callStaticMethod(
									"getByRecord",
									aSqlCon,
									rec,
									QString.fromStringConstant(r2ndLevel
											.getAlias("l2")))).asInstruction()
						);
				} else {
					// level 1: many -> many | level2 1 -> many
					doWhileQSqlQueryNext.addInstr(new Comment("level 1: many -> many | level2 1 -> many | r:"+r.getDestTable().getName()+"-> r2:"+r2ndLevel.getDestTable().getName()));
				
				
						
					Expression acc = _this.accessAttr(
							PgCppUtil.getManyRelationDestAttrName((ManyRelation) r), true);
					ForeachLoop foreachLevel1Relation =doWhileQSqlQueryNext._foreach( new Var(destBean.toSharedPtr().toRef(), 
							CodeUtil2.camelCase(destBean.getName())), acc);
					
					// for multi or single column primary key
					ArrayList<Expression> checkLevel1PkExpressions = new ArrayList<>();
					ArrayList<Expression> checkLevel2PkExpressions = new ArrayList<>();
					
					for(Column colPk : r.getDestTable().getPrimaryKey().getColumns()) {
						Expression recValueColPkLevel1 = rec.callMethod(
								"value",
								QString.fromStringConstant(r.getAlias() + "__"
										+ colPk.getName())).callMethod(
												BeanCls.getDatabaseMapper()
												.getQVariantConvertMethod(
														colPk.getDbType()));
						checkLevel1PkExpressions.add(foreachLevel1Relation.getVar().callMethod(BeanCls.getAccessMethodNameByColumn(colPk))._equals(recValueColPkLevel1));
					}
					
					
					Expression accLevel2 = foreachLevel1Relation.getVar().callMethod("get"+
						 StringUtil.ucfirst(PgCppUtil.getOneToManyRelationDestAttrName((OneToManyRelation) r2ndLevel)));
					IfBlock ifBlockLevel1 = foreachLevel1Relation._if(Expressions.and(checkLevel1PkExpressions));
					Var foundLevel2Bean = ifBlockLevel1.thenBlock()._declare(Types.Bool, "foundLevel2Bean", BoolExpression.FALSE);
					ForeachLoop foreachLevel2Relation = ifBlockLevel1.thenBlock()._foreach(new Var(destBean2ndLevel.toSharedPtr(), CodeUtil2.camelCase(destBean2ndLevel.getName())), accLevel2);
					
					for(Column colPk : r2ndLevel.getDestTable().getPrimaryKey().getColumns()) {
						Expression recValueColPkLevel2 = rec.callMethod(
								"value",
								QString.fromStringConstant(r2ndLevel.getAlias("l2") + "__"
										+ colPk.getName())).callMethod(
												BeanCls.getDatabaseMapper()
												.getQVariantConvertMethod(
														colPk.getDbType()));
						checkLevel2PkExpressions.add(foreachLevel2Relation.getVar().callMethod(BeanCls.getAccessMethodNameByColumn(colPk))._equals(recValueColPkLevel2));
					}
					IfBlock ifBlockLevel2 = foreachLevel2Relation._if(Expressions.and(checkLevel2PkExpressions));
					ifBlockLevel2.addIfInstr(foundLevel2Bean.assign(BoolExpression.TRUE));
					ifBlockLevel2._break();
					ifBlockLevel1.thenBlock()._if(Expressions.not(foundLevel2Bean))
						.addIfInstr(foreachLevel1Relation.getVar().callMethodInstruction(MethodAddRelatedBeanInternal.getMethodName(r2ndLevel),
								destBean2ndLevel.callStaticMethod(
										"getByRecord",
										aSqlCon,
										rec,
										QString.fromStringConstant(r2ndLevel
												.getAlias("l2")))
								));
				}
			}
			// end level2 1 -> many
			
			// level2 many -> many
			for(ManyRelation r2ndLevel : destBean.getManyRelations()) {
				BeanCls destBean2ndLevel = Beans.get(r2ndLevel.getDestTable());
				
				// level 1: 1 -> 1 | level2 many -> many
				if (r instanceof OneRelation) {
					doWhileQSqlQueryNext.addInstr(new Comment("level 1: 1 -> 1 | level2 many -> many | r:"+r.getDestTable().getName()+"-> r2:"+r2ndLevel.getDestTable().getName()));
					
					Expression acc = _this.accessAttr(
							PgCppUtil.getOneRelationDestAttrName((OneRelation) r), true);
					Var varForeach = new Var(destBean2ndLevel.toSharedPtr().toRef(), 
							CodeUtil2.camelCase(destBean2ndLevel.getName()));
					Var foundLevel2Many = doWhileQSqlQueryNext._declare(Types.Bool, "found"+CodeUtil2.uc1stCamelCase(varForeach.getName()),BoolExpression.FALSE);
					
					ForeachLoop foreachLevel2Relation = doWhileQSqlQueryNext._foreach(
							varForeach, acc.callMethod(MethodAttrGetter.getMethodName(r2ndLevel)));
					
					ArrayList<Expression> checkLevel2PkExpressions = new ArrayList<>();
					
					for(Column colPk : r.getDestTable().getPrimaryKey().getColumns()) {
						Expression recValueColPkLevel2 = rec.callMethod(
								"value",
								QString.fromStringConstant(r2ndLevel.getAlias("l2") + "__"
										+ colPk.getName())).callMethod(
												BeanCls.getDatabaseMapper()
												.getQVariantConvertMethod(
														colPk.getDbType()));
						checkLevel2PkExpressions.add(foreachLevel2Relation.getVar().callMethod(BeanCls.getAccessMethodNameByColumn(colPk))._equals(recValueColPkLevel2));
					}
					
					foreachLevel2Relation._if(Expressions.and(checkLevel2PkExpressions)).
					setIfInstr(foundLevel2Many.assign(BoolExpression.TRUE));
					doWhileQSqlQueryNext._if(Expressions.not(foundLevel2Many)).setIfInstr(
							acc.callMethod(MethodAttrGetter.getMethodName(r2ndLevel)).callMethod("append", destBean2ndLevel.callStaticMethod(
									"getByRecord",
									aSqlCon,
									rec,
									QString.fromStringConstant(r2ndLevel
											.getAlias("l2")))).asInstruction()
						);
					// end level 1: 1 -> 1 | level2 many -> many
					
					
				} else if (r instanceof OneToManyRelation){
					// level 1: 1 -> many | level2 many -> many
					doWhileQSqlQueryNext.addInstr(new Comment("level 1: many -> many | level2 many -> many | r:"+r.getDestTable().getName()+"-> r2:"+r2ndLevel.getDestTable().getName()));
				
				
						
					Expression acc = _this.accessAttr(
							PgCppUtil.getOneToManyRelationDestAttrName((OneToManyRelation) r), true);
					ForeachLoop foreachLevel1Relation =doWhileQSqlQueryNext._foreach( new Var(destBean.toSharedPtr().toRef(), 
							CodeUtil2.camelCase(destBean.getName())), acc);
					
					// for multi or single column primary key
					ArrayList<Expression> checkLevel1PkExpressions = new ArrayList<>();
					ArrayList<Expression> checkLevel2PkExpressions = new ArrayList<>();
					
					for(Column colPk : r.getDestTable().getPrimaryKey().getColumns()) {
						Expression recValueColPkLevel1 = rec.callMethod(
								"value",
								QString.fromStringConstant(r.getAlias() + "__"
										+ colPk.getName())).callMethod(
												BeanCls.getDatabaseMapper()
												.getQVariantConvertMethod(
														colPk.getDbType()));
						checkLevel1PkExpressions.add(foreachLevel1Relation.getVar().callMethod(BeanCls.getAccessMethodNameByColumn(colPk))._equals(recValueColPkLevel1));
					}
					
					
					Expression accLevel2 = foreachLevel1Relation.getVar().callMethod("get"+
						 StringUtil.ucfirst(PgCppUtil.getManyRelationDestAttrName((ManyRelation) r2ndLevel)));
					IfBlock ifBlockLevel1 = foreachLevel1Relation._if(Expressions.and(checkLevel1PkExpressions));
					Var foundLevel2Bean = ifBlockLevel1.thenBlock()._declare(Types.Bool, "foundLevel2Bean", BoolExpression.FALSE);
					ForeachLoop foreachLevel2Relation = ifBlockLevel1.thenBlock()._foreach(new Var(destBean2ndLevel.toSharedPtr(), CodeUtil2.camelCase(destBean2ndLevel.getName())), accLevel2);
					
					for(Column colPk : r2ndLevel.getDestTable().getPrimaryKey().getColumns()) {
						Expression recValueColPkLevel2 = rec.callMethod(
								"value",
								QString.fromStringConstant(r2ndLevel.getAlias("l2") + "__"
										+ colPk.getName())).callMethod(
												BeanCls.getDatabaseMapper()
												.getQVariantConvertMethod(
														colPk.getDbType()));
						checkLevel2PkExpressions.add(foreachLevel2Relation.getVar().callMethod(BeanCls.getAccessMethodNameByColumn(colPk))._equals(recValueColPkLevel2));
					}
					IfBlock ifBlockLevel2 = foreachLevel2Relation._if(Expressions.and(checkLevel2PkExpressions));
					ifBlockLevel2.addIfInstr(foundLevel2Bean.assign(BoolExpression.TRUE));
					ifBlockLevel2._break();
					ifBlockLevel1.thenBlock()._if(Expressions.not(foundLevel2Bean))
						.addIfInstr(foreachLevel1Relation.getVar().callMethodInstruction(MethodAddRelatedBeanInternal.getMethodName(r2ndLevel),
								destBean2ndLevel.callStaticMethod(
										"getByRecord",
										aSqlCon,
										rec,
										QString.fromStringConstant(r2ndLevel
												.getAlias("l2")))
								));
					// end level 1: 1 -> many | level2 many -> many
				} else {
					// level 1: many -> many | level2 many -> many
					doWhileQSqlQueryNext.addInstr(new Comment("level 1: many -> many | level2 many -> many | r:"+r.getDestTable().getName()+"-> r2:"+r2ndLevel.getDestTable().getName()));
				
				
						
					Expression acc = _this.accessAttr(
							PgCppUtil.getManyRelationDestAttrName((ManyRelation) r), true);
					ForeachLoop foreachLevel1Relation =doWhileQSqlQueryNext._foreach( new Var(destBean.toSharedPtr().toRef(), 
							CodeUtil2.camelCase(destBean.getName())), acc);
					
					// for multi or single column primary key
					ArrayList<Expression> checkLevel1PkExpressions = new ArrayList<>();
					ArrayList<Expression> checkLevel2PkExpressions = new ArrayList<>();
					
					for(Column colPk : r.getDestTable().getPrimaryKey().getColumns()) {
						Expression recValueColPkLevel1 = rec.callMethod(
								"value",
								QString.fromStringConstant(r.getAlias() + "__"
										+ colPk.getName())).callMethod(
												BeanCls.getDatabaseMapper()
												.getQVariantConvertMethod(
														colPk.getDbType()));
						checkLevel1PkExpressions.add(foreachLevel1Relation.getVar().callMethod(BeanCls.getAccessMethodNameByColumn(colPk))._equals(recValueColPkLevel1));
					}
					
					
					Expression accLevel2 = foreachLevel1Relation.getVar().callMethod("get"+
						 StringUtil.ucfirst(PgCppUtil.getManyRelationDestAttrName((ManyRelation) r2ndLevel)));
					IfBlock ifBlockLevel1 = foreachLevel1Relation._if(Expressions.and(checkLevel1PkExpressions));
					Var foundLevel2Bean = ifBlockLevel1.thenBlock()._declare(Types.Bool, "foundLevel2Bean", BoolExpression.FALSE);
					ForeachLoop foreachLevel2Relation = ifBlockLevel1.thenBlock()._foreach(new Var(destBean2ndLevel.toSharedPtr(), CodeUtil2.camelCase(destBean2ndLevel.getName())), accLevel2);
					
					for(Column colPk : r2ndLevel.getDestTable().getPrimaryKey().getColumns()) {
						Expression recValueColPkLevel2 = rec.callMethod(
								"value",
								QString.fromStringConstant(r2ndLevel.getAlias("l2") + "__"
										+ colPk.getName())).callMethod(
												BeanCls.getDatabaseMapper()
												.getQVariantConvertMethod(
														colPk.getDbType()));
						checkLevel2PkExpressions.add(foreachLevel2Relation.getVar().callMethod(BeanCls.getAccessMethodNameByColumn(colPk))._equals(recValueColPkLevel2));
					}
					IfBlock ifBlockLevel2 = foreachLevel2Relation._if(Expressions.and(checkLevel2PkExpressions));
					ifBlockLevel2.addIfInstr(foundLevel2Bean.assign(BoolExpression.TRUE));
					ifBlockLevel2._break();
					ifBlockLevel1.thenBlock()._if(Expressions.not(foundLevel2Bean))
						.addIfInstr(foreachLevel1Relation.getVar().callMethodInstruction(MethodAddRelatedBeanInternal.getMethodName(r2ndLevel),
								destBean2ndLevel.callStaticMethod(
										"getByRecord",
										aSqlCon,
										rec,
										QString.fromStringConstant(r2ndLevel
												.getAlias("l2")))
								));
				}
			}
			// end level2 many -> many
		}
		// end level 2 loop
	}

}
