package php.beanrepository.method;

import java.util.ArrayList;
import java.util.List;

import php.bean.BeanCls;
import php.bean.Beans;
import php.bean.method.MethodOneRelationAttrSetter;
import php.bean.method.MethodOneRelationBeanIsNull;
import php.beanrepository.ClsBeanRepository;
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
import php.lib.ClsBaseBean;
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

public class MethodBeanLoad extends Method {

	protected PrimaryKey primaryKey;
	protected BeanCls bean;

	public MethodBeanLoad(BeanCls bean) {
		super(Public, Types.Void, "load"+bean.getName());

		this.primaryKey = bean.getTbl().getPrimaryKey();

		setStatic(true);

		addParam(new Param(bean, "entity"));
		this.bean = bean;
	}

	@Override
	public boolean includeIfEmpty() {
		return true;
	}
	
	private Expression getFetchExpression(Var res) {
		return BeanCls.getTypeMapper().getDefaultFetchExpression(res);
		
	}

	@Override
	public void addImplementation() {
		Param pBean = getParam("entity");
		List<OneRelation> oneRelations = bean.getOneRelations();
		List<OneToManyRelation> oneToManyRelations = bean.getOneToManyRelations();
		List<ManyRelation> manyToManyRelations = bean.getManyToManyRelations();
		
		if(!oneRelations.isEmpty() || !oneToManyRelations.isEmpty() || !manyToManyRelations.isEmpty()) {
			ClsBeanRepository parent = (ClsBeanRepository) this.parent;
			
			
			Attr aSqlCon = parent.getAttrByName("sqlCon");
			//Method mBuildQuery = aSqlCon.getClassType().getMethod("buildQuery");
			Var sqlQuery = _declare(BeanCls.getSqlQueryCls(), "query",BeanCls.getSqlQueryCls().newInstance(aSqlCon));
			
			
			List<AbstractRelation> allRelations = new ArrayList<>(oneRelations.size()+oneToManyRelations.size()+manyToManyRelations.size());
			allRelations.addAll(oneRelations);
			allRelations.addAll(oneToManyRelations);
			allRelations.addAll(manyToManyRelations);
			
			Expression exprSqlQuery = sqlQuery.callMethod("select",  parent.callStaticMethod(ClsBeanRepository.getMethodNameGetAllSelectFields(bean)) )
										.callMethod("from", Types.BeanRepository.callStaticMethod(ClsBeanRepository.getMethodNameGetTableName(bean),new PhpStringLiteral("b1")));
			
			for(OneRelation r:oneRelations) {
				ArrayList<String> joinConditions=new ArrayList<>();
				for(int i=0;i<r.getColumnCount();i++) {
					joinConditions.add(CodeUtil.sp("b1."+r.getColumns(i).getValue1().getEscapedName(),'=',(r.getAlias())+"."+ r.getColumns(i).getValue2().getEscapedName()));
				}
				
				exprSqlQuery = exprSqlQuery.callMethod("leftJoin", Types.BeanRepository.callStaticMethod(ClsBeanRepository.getMethodNameGetTableName(Beans.get(r.getDestTable())),new PhpStringLiteral(r.getAlias())), new PhpStringLiteral(CodeUtil2.concat(joinConditions," AND ")));
			}
			for(OneToManyRelation r:oneToManyRelations) {
				ArrayList<String> joinConditions=new ArrayList<>();
				for(int i=0;i<r.getColumnCount();i++) {
					joinConditions.add(CodeUtil.sp("b1."+r.getColumns(i).getValue1().getEscapedName(),'=',(r.getAlias())+"."+ r.getColumns(i).getValue2().getEscapedName()));
				}
				
				exprSqlQuery = exprSqlQuery.callMethod("leftJoin", Types.BeanRepository.callStaticMethod(ClsBeanRepository.getMethodNameGetTableName(Beans.get(r.getDestTable())),new PhpStringLiteral(r.getAlias())), new PhpStringLiteral(CodeUtil2.concat(joinConditions," AND ")));
			}
			for(ManyRelation r:manyToManyRelations) {
				ArrayList<String> joinConditions=new ArrayList<>();
				for(int i=0;i<r.getSourceColumnCount();i++) {
					joinConditions.add(CodeUtil.sp("b1."+r.getSourceEntityColumn(i).getEscapedName(),'=',r.getAlias("mapping")+"."+ r.getSourceMappingColumn(i).getEscapedName()));
				}
				
				exprSqlQuery = exprSqlQuery.callMethod("leftJoin", new PhpStringLiteral(r.getMappingTable().getName()+" "+r.getAlias("mapping")), new PhpStringLiteral(CodeUtil2.concat(joinConditions," AND ")));
				
				joinConditions.clear();
				for(int i=0;i<r.getDestColumnCount();i++) {
					joinConditions.add(CodeUtil.sp(r.getAlias("mapping")+"."+r.getDestMappingColumn(i).getEscapedName(),'=',r.getAlias()+"."+r.getDestEntityColumn(i).getEscapedName() ));
				}
				
				exprSqlQuery = exprSqlQuery.callMethod("leftJoin", Types.BeanRepository.callStaticMethod(ClsBeanRepository.getMethodNameGetTableName(Beans.get(r.getDestTable())),new PhpStringLiteral(r.getAlias())), new PhpStringLiteral(CodeUtil2.concat(joinConditions," AND ")));
				
			}

			
			for(Column col:bean.getTbl().getPrimaryKey().getColumns()) {
				exprSqlQuery = exprSqlQuery.callMethod("where", new PhpStringLiteral("b1."+ col.getEscapedName()+"=?"),pBean.callAttrGetter(bean.getAttrByName(col.getCamelCaseName())));
						
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
	//				BeanCls foreignCls = Beans.get(r.getDestTable()); 
					IfBlock ifBlock= doWhileRowIsNotNull._if(Expressions.and( pBean.callMethod(new MethodOneRelationBeanIsNull(r)),row.arrayIndex(new PhpStringLiteral(BeanCls.getTypeMapper().filterFetchAssocArrayKey(r.getAlias() + "__" + r.getDestTable().getPrimaryKey().getFirstColumn().getName()))).isNotNull()) );
					ifBlock.thenBlock().
					_callMethodInstr(pBean, new MethodOneRelationAttrSetter( pBean.getClassConcreteType().getAttrByName(PgCppUtil.getOneRelationDestAttrName(r)), true), 
							Types.BeanRepository.callStaticMethod(MethodGetFromQueryAssocArray.getMethodName(Beans.get(r.getDestTable())),  row, new PhpStringLiteral(r.getAlias())));
				}
				
				for(AbstractRelation r:manyRelations) {
					BeanCls foreignCls = Beans.get(r.getDestTable()); 
					IfBlock ifIsRowIndexNotNull = null;
					Expression pkArrayIndex = null;
					if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
						//Var pkSet = ifRowNotNull.thenBlock()._declareNewArray(Types.array(foreignCls.getPkType()), "pkSet"+StringUtil.ucfirst(r.getAlias()));
						Expression[] arrayIndexExpressions = new Expression[1+r.getDestTable().getPrimaryKey().getColumnCount()];
						ArrayList<String> pkSprintf = new ArrayList<String> ();
						int i=0;
						Expression[] ifIsRowIndexNotNullCondition = new Expression[r.getDestTable().getPrimaryKey().getColumnCount()];
						for(Column colPk : r.getDestTable().getPrimaryKey().getColumns()) {
							Expression foreignPkArg = new PhpStringLiteral( BeanCls.getTypeMapper().filterFetchAssocArrayKey(r.getAlias()+"__"+colPk.getName()));
							
							pkSprintf.add(BeanCls.getTypeMapper().columnToType(colPk).getSprintfType());
							ifIsRowIndexNotNullCondition[i] = foreignPkArg.isNotNull();
							arrayIndexExpressions[++i] = row.arrayIndex(foreignPkArg);
						}
						ifIsRowIndexNotNull = ifRowNotNull.thenBlock()._if(Expressions.and(ifIsRowIndexNotNullCondition));
						
						arrayIndexExpressions[0] = new PhpStringLiteral( CodeUtil.concat(pkSprintf, "_"));			
						Var pkSet = ifIsRowIndexNotNull.thenBlock()._declareNewArray(Types.array(Types.Mixed), "pkSet"+StringUtil.ucfirst(r.getAlias()));
						pkArrayIndex = pkSet.arrayIndex(PhpFunctions.sprintf.call(arrayIndexExpressions));
											
						//throw new RuntimeException("not implemented");
					} else {
						Column colPk = r.getDestTable().getPrimaryKey().getColumns().get(0);
						
						
						//IfBlock ifNotRecValueIsNull = doWhileQSqlQueryNext._if(Expressions.not(  Types.BeanRepository.callStaticMethod(MethodGetFromResultSet.getMethodName(Beans.get(r.getDestTable())),  resultSet, JavaString.fromStringConstant("pk"+r.getAlias()))));
						
						Var pkSet = ifRowNotNull.thenBlock()._declareNewArray(Types.array(Types.Mixed), "pkSet"+StringUtil.ucfirst(r.getAlias()));
						pkArrayIndex = pkSet.arrayIndex(row.arrayIndex(new PhpStringLiteral( BeanCls.getTypeMapper().filterFetchAssocArrayKey(r.getAlias()+"__"+colPk.getName()))));
						ifIsRowIndexNotNull = ifRowNotNull.thenBlock()._if(pkArrayIndex.isNotNull());
						
						
					}
					IfBlock ifNotIssetPk = ifIsRowIndexNotNull.thenBlock()._if(_not(PhpFunctions.isset.call(pkArrayIndex)));
					Var foreignBean = ifNotIssetPk.thenBlock()._declare(foreignCls, "b" + r.getAlias(),  Types.BeanRepository.callStaticMethod(MethodGetFromQueryAssocArray.getMethodName(Beans.get(r.getDestTable())),  row, new PhpStringLiteral(r.getAlias())));
					ifNotIssetPk.thenBlock()._assign(pkArrayIndex, foreignBean);
					ifNotIssetPk.thenBlock()._callMethodInstr(pBean, BeanCls.getAddRelatedBeanMethodName(r), foreignBean);
					doWhileRowIsNotNull.addInstr(ifIsRowIndexNotNull);
				}
				
				ifRowNotNull.addIfInstr(doWhileRowIsNotNull);
				doWhileRowIsNotNull.setCondition(ifRowNotNull.getCondition());
				doWhileRowIsNotNull.addInstr(row.assign(getFetchExpression(res)));
			}
			
		}
		_callMethodInstr(pBean, ClsBaseBean.setLoaded, BoolExpression.TRUE);
	}

}
