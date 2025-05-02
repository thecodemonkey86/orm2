package php.entityrepository.method;

import java.util.ArrayList;
import java.util.List;

import php.core.Attr;
import php.core.Param;
import php.core.PhpFunctions;
import php.core.Types;
import php.core.expression.BoolExpression;
import php.core.expression.Expression;
import php.core.expression.Expressions;
import php.core.expression.PhpStringLiteral;
import php.core.expression.Var;
import php.core.instruction.DoWhile;
import php.core.instruction.IfBlock;
import php.core.method.Method;
import php.entity.Entities;
import php.entity.EntityCls;
import php.entity.method.MethodOneRelationAttrSetter;
import php.entity.method.MethodOneRelationEntityIsNull;
import php.entityrepository.ClsEntityRepository;
import php.lib.ClsBaseEntity;
import php.lib.ClsSqlQuery;
import util.CodeUtil2;
import util.StringUtil;
import util.pg.PgCppUtil;
import codegen.CodeUtil;
import database.column.Column;
import database.relation.AbstractRelation;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import database.relation.PrimaryKey;

public class MethodEntityLoad extends Method {

	protected PrimaryKey primaryKey;
	protected EntityCls entity;

	public MethodEntityLoad(EntityCls entity) {
		super(Public, Types.Void, "load"+entity.getName());

		this.primaryKey = entity.getTbl().getPrimaryKey();

		setStatic(true);

		addParam(new Param(entity, "entity"));
		this.entity = entity;
	}

	@Override
	public boolean includeIfEmpty() {
		return true;
	}
	
	private Expression getFetchExpression(Var res) {
		return EntityCls.getTypeMapper().getDefaultFetchExpression(res);
		
	}

	@Override
	public void addImplementation() {
		Param pEntity = getParam("entity");
		List<OneRelation> oneRelations = entity.getOneRelations();
		List<OneToManyRelation> oneToManyRelations = entity.getOneToManyRelations();
		List<ManyRelation> manyToManyRelations = entity.getManyToManyRelations();
		
		if(!oneRelations.isEmpty() || !oneToManyRelations.isEmpty() || !manyToManyRelations.isEmpty()) {
			ClsEntityRepository parent = (ClsEntityRepository) this.parent;
			
			
			Attr aSqlCon = parent.getAttrByName("sqlCon");
			//Method mBuildQuery = aSqlCon.getClassType().getMethod("buildQuery");
			Var sqlQuery = _declare(EntityCls.getSqlQueryCls(), "query",EntityCls.getSqlQueryCls().newInstance(aSqlCon));
			
			
			List<AbstractRelation> allRelations = new ArrayList<>(oneRelations.size()+oneToManyRelations.size()+manyToManyRelations.size());
			allRelations.addAll(oneRelations);
			allRelations.addAll(oneToManyRelations);
			allRelations.addAll(manyToManyRelations);
			
			Expression exprSqlQuery = sqlQuery.callMethod("select",  parent.callStaticMethod(ClsEntityRepository.getMethodNameGetAllSelectFields(entity)) )
										.callMethod("from", Types.EntityRepository.callStaticMethod(ClsEntityRepository.getMethodNameGetTableName(entity),new PhpStringLiteral("e1")));
			
			for(OneRelation r:oneRelations) {
				ArrayList<String> joinConditions=new ArrayList<>();
				for(int i=0;i<r.getColumnCount();i++) {
					joinConditions.add(CodeUtil.sp("e1."+r.getColumns(i).getValue1().getEscapedName(),'=',(r.getAlias())+"."+ r.getColumns(i).getValue2().getEscapedName()));
				}
				
				exprSqlQuery = exprSqlQuery.callMethod("leftJoin", Types.EntityRepository.callStaticMethod(ClsEntityRepository.getMethodNameGetTableName(Entities.get(r.getDestTable())),new PhpStringLiteral(r.getAlias())), new PhpStringLiteral(CodeUtil2.concat(joinConditions," AND ")));
				

				if(r.hasAdditionalJoin()) {
					exprSqlQuery = exprSqlQuery.callMethod(ClsSqlQuery.join,new PhpStringLiteral(r.getAdditionalJoin()));
				}
			}
			for(OneToManyRelation r:oneToManyRelations) {
				ArrayList<String> joinConditions=new ArrayList<>();
				for(int i=0;i<r.getColumnCount();i++) {
					joinConditions.add(CodeUtil.sp("e1."+r.getColumns(i).getValue1().getEscapedName(),'=',(r.getAlias())+"."+ r.getColumns(i).getValue2().getEscapedName()));
				}
				
				exprSqlQuery = exprSqlQuery.callMethod("leftJoin", Types.EntityRepository.callStaticMethod(ClsEntityRepository.getMethodNameGetTableName(Entities.get(r.getDestTable())),new PhpStringLiteral(r.getAlias())), new PhpStringLiteral(CodeUtil2.concat(joinConditions," AND ")));
				if(r.hasAdditionalJoin()) {
					exprSqlQuery = exprSqlQuery.callMethod(ClsSqlQuery.join,new PhpStringLiteral(r.getAdditionalJoin()));
				}
			}
			for(ManyRelation r:manyToManyRelations) {
				ArrayList<String> joinConditions=new ArrayList<>();
				for(int i=0;i<r.getSourceColumnCount();i++) {
					joinConditions.add(CodeUtil.sp("e1."+r.getSourceEntityColumn(i).getEscapedName(),'=',r.getAlias("mapping")+"."+ r.getSourceMappingColumn(i).getEscapedName()));
				}
				
				exprSqlQuery = exprSqlQuery.callMethod("leftJoin", new PhpStringLiteral(r.getMappingTable().getName()+" "+r.getAlias("mapping")), new PhpStringLiteral(CodeUtil2.concat(joinConditions," AND ")));
				
				joinConditions.clear();
				for(int i=0;i<r.getDestColumnCount();i++) {
					joinConditions.add(CodeUtil.sp(r.getAlias("mapping")+"."+r.getDestMappingColumn(i).getEscapedName(),'=',r.getAlias()+"."+r.getDestEntityColumn(i).getEscapedName() ));
				}
				
				exprSqlQuery = exprSqlQuery.callMethod("leftJoin", Types.EntityRepository.callStaticMethod(ClsEntityRepository.getMethodNameGetTableName(Entities.get(r.getDestTable())),new PhpStringLiteral(r.getAlias())), new PhpStringLiteral(CodeUtil2.concat(joinConditions," AND ")));
				if(r.hasAdditionalJoin()) {
					exprSqlQuery = exprSqlQuery.callMethod(ClsSqlQuery.join,new PhpStringLiteral(r.getAdditionalJoin()));
				}
			}

			
			for(Column col:entity.getTbl().getPrimaryKey().getColumns()) {
				exprSqlQuery = exprSqlQuery.callMethod("where", new PhpStringLiteral("e1."+ col.getEscapedName()+"=?"),pEntity.callAttrGetter(entity.getAttrByName(col.getCamelCaseName())));
						
			}
			for(AbstractRelation r:allRelations) {
				if(r.hasAdditionalOrderBy()) {
					exprSqlQuery = exprSqlQuery.callMethod(ClsSqlQuery.orderBy,new PhpStringLiteral(r.getAdditionalOrderBy()));
				}
			}
			exprSqlQuery = exprSqlQuery.callMethod(ClsSqlQuery.query);
			Var res = _declare(exprSqlQuery.getType(),
					"res", exprSqlQuery
					);
			
			Var row = _declare(Types.array(Types.Mixed), "row", getFetchExpression(res) );
			IfBlock ifRowNotNull =	_if(row);
								
			if(!oneRelations.isEmpty() || !manyToManyRelations.isEmpty() || !oneToManyRelations.isEmpty()) {
				DoWhile doWhileRowIsNotNull = DoWhile.create();
				
				
				ArrayList<AbstractRelation> manyRelations = new ArrayList<>(oneToManyRelations.size()+manyToManyRelations.size());
				manyRelations.addAll(oneToManyRelations);
				manyRelations.addAll(manyToManyRelations);
			
				for(OneRelation r:oneRelations) {
					IfBlock ifBlock= doWhileRowIsNotNull._if(Expressions.and( pEntity.callMethod(new MethodOneRelationEntityIsNull(r)),row.arrayIndex(new PhpStringLiteral(EntityCls.getTypeMapper().filterFetchAssocArrayKey(r.getAlias() + "__" + r.getDestTable().getPrimaryKey().getFirstColumn().getName()))).isNotNull()) );
					ifBlock.thenBlock().
					_callMethodInstr(pEntity, new MethodOneRelationAttrSetter( pEntity.getClassConcreteType().getAttrByName(PgCppUtil.getOneRelationDestAttrName(r)), true), 
							Types.EntityRepository.callStaticMethod(MethodGetFromQueryAssocArray.getMethodName(Entities.get(r.getDestTable())),  row, new PhpStringLiteral(r.getAlias())));
				}
				
				for(AbstractRelation r:manyRelations) {
					EntityCls foreignCls = Entities.get(r.getDestTable()); 
					IfBlock ifIsRowIndexNotNull = null;
					Expression pkArrayIndex = null;
					if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
						//Var pkSet = ifRowNotNull.thenBlock()._declareNewArray(Types.array(foreignCls.getPkType()), "pkSet"+StringUtil.ucfirst(r.getAlias()));
						Expression[] arrayIndexExpressions = new Expression[1+r.getDestTable().getPrimaryKey().getColumnCount()];
						ArrayList<String> pkSprintf = new ArrayList<String> ();
						int i=0;
						Expression[] ifIsRowIndexNotNullCondition = new Expression[r.getDestTable().getPrimaryKey().getColumnCount()];
						for(Column colPk : r.getDestTable().getPrimaryKey().getColumns()) {
							Expression foreignPkArg = new PhpStringLiteral( EntityCls.getTypeMapper().filterFetchAssocArrayKey(r.getAlias()+"__"+colPk.getName()));
							
							pkSprintf.add(EntityCls.getTypeMapper().columnToType(colPk).getSprintfType());
							ifIsRowIndexNotNullCondition[i] = row.arrayIndex(foreignPkArg).isNotNull();
							arrayIndexExpressions[++i] = row.arrayIndex(foreignPkArg);
						}
						ifIsRowIndexNotNull = ifRowNotNull.thenBlock()._if(Expressions.and(ifIsRowIndexNotNullCondition));
						
						arrayIndexExpressions[0] = new PhpStringLiteral( CodeUtil.concat(pkSprintf, "_"));			
						Var pkSet = ifIsRowIndexNotNull.thenBlock()._declareNewArray(Types.array(Types.Mixed), "pkSet"+StringUtil.ucfirst(r.getAlias()));
						pkArrayIndex = pkSet.arrayIndex(PhpFunctions.sprintf.call(arrayIndexExpressions));
											
						//throw new RuntimeException("not implemented");
					} else {
						Column colPk = r.getDestTable().getPrimaryKey().getColumns().get(0);
						Var pkSet = ifRowNotNull.thenBlock()._declareNewArray(Types.array(Types.Mixed), "pkSet"+StringUtil.ucfirst(r.getAlias()));
						pkArrayIndex = pkSet.arrayIndex(row.arrayIndex(new PhpStringLiteral( EntityCls.getTypeMapper().filterFetchAssocArrayKey(r.getAlias()+"__"+colPk.getName()))));
						ifIsRowIndexNotNull = ifRowNotNull.thenBlock()._if(pkArrayIndex.isNotNull());
						
						
					}
					IfBlock ifNotIssetPk = ifIsRowIndexNotNull.thenBlock()._if(_not(PhpFunctions.isset.call(pkArrayIndex)));
					Var foreignEntity = ifNotIssetPk.thenBlock()._declare(foreignCls, "b" + r.getAlias(),  Types.EntityRepository.callStaticMethod(MethodGetFromQueryAssocArray.getMethodName(Entities.get(r.getDestTable())),  row, new PhpStringLiteral(r.getAlias())));
					ifNotIssetPk.thenBlock()._assign(pkArrayIndex, foreignEntity);
					ifNotIssetPk.thenBlock()._callMethodInstr(pEntity, EntityCls.getAddRelatedEntityMethodName(r), foreignEntity);
					doWhileRowIsNotNull.addInstr(ifIsRowIndexNotNull);
				}
				
				ifRowNotNull.addIfInstr(doWhileRowIsNotNull);
				doWhileRowIsNotNull.setCondition(ifRowNotNull.getCondition());
				doWhileRowIsNotNull.addInstr(row.assign(getFetchExpression(res)));
			}
			
		}
		_callMethodInstr(pEntity, ClsBaseEntity.setLoaded, BoolExpression.TRUE);
	}

}
