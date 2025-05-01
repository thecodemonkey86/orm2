package sunjava.entityrepository.method;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sunjava.core.JavaString;
import sunjava.core.Method;
import sunjava.core.Param;
import sunjava.core.Type;
import sunjava.core.Types;
import sunjava.core.expression.BoolExpression;
import sunjava.core.expression.CharExpression;
import sunjava.core.expression.Expression;
import sunjava.core.expression.Expressions;
import sunjava.core.expression.Var;
import sunjava.core.instruction.While;
import sunjava.entity.Entities;
import sunjava.entity.EntityCls;
import sunjava.entity.method.MethodOneRelationAttrSetter;
import sunjava.entityrepository.ClsEntityRepository;
import sunjava.lib.ClsBaseEntity;
import sunjava.lib.ClsHashSet;
import sunjava.lib.ClsSqlQuery;
import sunjava.orm.OrmUtil;
import util.CodeUtil2;
import util.pg.PgCppUtil;
import codegen.CodeUtil;
import database.column.Column;
import database.relation.AbstractRelation;
import database.relation.IManyRelation;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import database.relation.PrimaryKey;

public class MethodEntityLoad extends Method {

	protected List<OneRelation> oneRelations;
	protected List<OneToManyRelation> oneToManyRelations;
	protected List<ManyRelation> manyToManyRelations;
	protected PrimaryKey primaryKey;
	protected EntityCls entity;
	protected Param pSqlCon;
	public MethodEntityLoad(EntityCls entity) {
		super(Public, Types.Void, "load"+entity.getName());

		this.oneRelations =entity.getOneRelations();
		this.oneToManyRelations =entity.getOneToManyRelations();
		this.manyToManyRelations = entity.getManyRelations();
		this.primaryKey = entity.getTbl().getPrimaryKey();

		setStatic(true);

		addParam(new Param(entity, "entity"));
		pSqlCon =addParam(new Param(Types.Connection, "sqlConnection"));
		this.entity = entity;
	}

	@Override
	public boolean includeIfEmpty() {
		return true;
	}

	@Override
	public void addImplementation() {
		Param pEntity = getParam("entity");
		if(!oneRelations.isEmpty() || !oneToManyRelations.isEmpty() || !manyToManyRelations.isEmpty()) {
			addThrowsException(Types.SqlException);
			ClsEntityRepository parent = (ClsEntityRepository) this.parent;

			Var sqlQuery =  _declare(Types.SqlQuery, "query",EntityCls.getSqlQueryCls().newInstance(pSqlCon));

			ArrayList<Expression> selectFields = new ArrayList<>();
			selectFields.add(parent.callStaticMethod(MethodGetSelectFields.getMethodName(entity),JavaString.stringConstant("e1")));

			List<AbstractRelation> allRelations = new ArrayList<>(oneRelations.size()+oneToManyRelations.size()+manyToManyRelations.size());
			allRelations.addAll(oneRelations);
			allRelations.addAll(oneToManyRelations);
			allRelations.addAll(manyToManyRelations);

			ArrayList<IManyRelation> manyRelations = new ArrayList<>(this.oneToManyRelations.size()+this.manyToManyRelations.size());
			manyRelations.addAll(this.oneToManyRelations);
			manyRelations.addAll(this.manyToManyRelations);

			for(AbstractRelation r:allRelations) {
				selectFields.add(parent.callStaticMethod(MethodGetSelectFields.getMethodName(Entities.get(r.getDestTable())), JavaString.stringConstant(r.getAlias())));

			}
			Expression exprQSqlQuery = sqlQuery.callMethod("select", Expressions.concat(CharExpression.fromChar(','), selectFields) )
					.callMethod("from", parent.callStaticMethod(MethodGetTableName.getMethodName(entity),JavaString.stringConstant("e1")));


			for(OneRelation r:oneRelations) {
				ArrayList<String> joinConditions=new ArrayList<>();
				for(int i=0;i<r.getColumnCount();i++) {
					joinConditions.add(CodeUtil.sp("e1."+r.getColumns(i).getValue1().getEscapedName(),'=',r.getAlias()+"."+ r.getColumns(i).getValue2().getEscapedName()));
				}

				exprQSqlQuery = exprQSqlQuery.callMethod("leftJoin", parent.callStaticMethod(MethodGetTableName.getMethodName(Entities.get(r.getDestTable()))),JavaString.stringConstant(r.getAlias()), JavaString.stringConstant(CodeUtil2.concat(joinConditions," AND ")));
			}
			for(OneToManyRelation r:oneToManyRelations) {
				ArrayList<String> joinConditions=new ArrayList<>();
				for(int i=0;i<r.getColumnCount();i++) {
					joinConditions.add(CodeUtil.sp("e1."+r.getColumns(i).getValue1().getEscapedName(),'=',r.getAlias()+"."+ r.getColumns(i).getValue2().getEscapedName()));
				}

				exprQSqlQuery = exprQSqlQuery.callMethod("leftJoin", parent.callStaticMethod(MethodGetTableName.getMethodName(Entities.get(r.getDestTable()))),JavaString.stringConstant(r.getAlias()), JavaString.stringConstant(CodeUtil2.concat(joinConditions," AND ")));
			}
			for(ManyRelation r:this.manyToManyRelations) {
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
						JavaString.stringConstant(r.getMappingTable().getName()),
						JavaString.stringConstant(r.getMappingAlias()), 
						JavaString.stringConstant(CodeUtil2.concat(joinConditionsE1Mapping," AND ")));
				exprQSqlQuery = exprQSqlQuery.callMethod("leftJoin", 
						parent.callStaticMethod(MethodGetTableName.getMethodName(Entities.get(r.getDestTable()))),
						JavaString.stringConstant(r.getAlias()), 
						JavaString.stringConstant(CodeUtil2.concat(joinConditionsMappingDest," AND ")));


			}

			for(Column col:primaryKey.getColumns()) {

				exprQSqlQuery = exprQSqlQuery.callMethod("where", JavaString.stringConstant("e1."+ col.getEscapedName()+"=?"),pEntity.callAttrGetter(col.getCamelCaseName()));

			}
			exprQSqlQuery = exprQSqlQuery.callMethod(ClsSqlQuery.query);
			Var resultSet = _declare(exprQSqlQuery.getType(),
					"resultSet", exprQSqlQuery
					);

			//bCount = 2;
			HashMap<String, Var> pkSets=new HashMap<>();
			/*for(OneToManyRelation r:oneToManyRelations) {
			EntityCls foreignCls = Entities.get(r.getDestTable()); 
			if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
				Var pkSet = _declareInitDefaultConstructor(Types.hashset(foreignCls.getPkType()), "pkSetB"+r.getAlias());
				pkSets.put(r.getAlias() ,pkSet);
			} else {
				Column colPk = r.getDestTable().getPrimaryKey().getColumns().get(0);
				Type type = EntityCls.getTypeMapper().columnToType( colPk);
				Var pkSet = _declareInitDefaultConstructor(Types.hashset(type), "pkSetB"+r.getAlias());
				pkSets.put(r.getAlias(), pkSet);
			}
		}*/
			for(IManyRelation r:manyRelations) {
				EntityCls foreignCls = Entities.get(r.getDestTable()); 
				if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
					Var pkSet = _declareInitDefaultConstructor(Types.hashset(foreignCls.getPkType()), "pkSetB"+r.getAlias());
					pkSets.put(r.getAlias() ,pkSet);
				} else {
					Column colPk = r.getDestTable().getPrimaryKey().getColumns().get(0);
					Type type = EntityCls.getTypeMapper().columnToType( colPk);
					Var pkSet = _declareInitDefaultConstructor(Types.hashset(type), "pkSetB"+r.getAlias());
					pkSets.put(r.getAlias(), pkSet);
				}
			}

			While doWhileQSqlQueryNext = _while(resultSet.callMethod("next"));
			/*Var resultSet = _declare(exprQSqlQuery.getType(),
				"resultSet", exprQSqlQuery
				);*/

			for(IManyRelation r:manyRelations) {
				Var pkSet=pkSets.get(r.getAlias());
				EntityCls foreignCls = Entities.get(r.getDestTable()); 
				if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
					Expression[] pkArgs = new Expression[ r.getDestTable().getPrimaryKey().getColumnCount()];
					for(int i = 0; i < pkArgs.length; i++) {
						Expression resultSetValueGetter = EntityCls.getTypeMapper().getResultSetValueGetter(resultSet, r.getDestTable().getPrimaryKey().getColumn(i),JavaString.stringConstant(r.getAlias()));
						pkArgs[i] = resultSetValueGetter;
					}
					Var pk = doWhileQSqlQueryNext._declareNew(foreignCls.getPkType(), "pk"+r.getAlias(),pkArgs);
					doWhileQSqlQueryNext._if(Expressions.not(pkSet.callMethod(ClsHashSet.contains, pk)))
					.addIfInstr(pkSet.callMethodInstruction(ClsHashSet.add, pk))
					.addIfInstr(pEntity.callMethodInstruction(OrmUtil.getAddRelatedEntityMethodName(r), Types.EntityRepository.callStaticMethod(MethodGetFromResultSet.getMethodName(Entities.get(r.getDestTable())),  resultSet, JavaString.stringConstant(r.getAlias()))));
					/*.addIfInstr(pEntity.accessAttr(
								CodeUtil2.plural(r.getDestTable().getCamelCaseName()))
								.callMethodInstruction(ClsArrayList.METHOD_NAME_ADD, 
										Types.EntityRepository.callStaticMethod(MethodGetFromResultSet.getMethodName(Entities.get(r.getDestTable())),  resultSet, JavaString.fromStringConstant(r.getAlias()))))
					 */

					;
				} else {
					Expression resultSetValueGetter = EntityCls.getTypeMapper().getResultSetValueGetter(resultSet, r.getDestTable().getPrimaryKey().getFirstColumn(),JavaString.stringConstant(r.getAlias()));
					Var pk = doWhileQSqlQueryNext._declare(foreignCls.getPkType(), "pk"+r.getAlias(),resultSetValueGetter);

					doWhileQSqlQueryNext._if(Expressions.not(pkSet.callMethod(ClsHashSet.contains, pk)))
					.addIfInstr(pkSet.callMethodInstruction(ClsHashSet.add, pk))
					.addIfInstr(pEntity.callMethodInstruction(OrmUtil.getAddRelatedEntityMethodName(r), Types.EntityRepository.callStaticMethod(MethodGetFromResultSet.getMethodName(Entities.get(r.getDestTable())),  resultSet, JavaString.stringConstant(r.getAlias()))));
					/*.addIfInstr(pEntity.accessAttr(
						CodeUtil2.plural(r.getDestTable().getCamelCaseName()))
						.callMethodInstruction(ClsArrayList.METHOD_NAME_ADD, 
								Types.EntityRepository.callStaticMethod(MethodGetFromResultSet.getMethodName(Entities.get(r.getDestTable())),  resultSet, JavaString.fromStringConstant(r.getAlias()))));
					 */
				}

			}


			for(OneRelation r:oneRelations) {
				try {
					doWhileQSqlQueryNext._callMethodInstr(pEntity, new MethodOneRelationAttrSetter( pEntity.getClassConcreteType().getAttrByName(PgCppUtil.getOneRelationDestAttrName(r)), true, r.isPartOfPk()), 
							Types.EntityRepository.callStaticMethod(MethodGetFromResultSet.getMethodName(Entities.get(r.getDestTable())),  resultSet, JavaString.stringConstant(r.getAlias())));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		_callMethodInstr(pEntity, ClsBaseEntity.setLoaded, BoolExpression.TRUE);
	}

}
