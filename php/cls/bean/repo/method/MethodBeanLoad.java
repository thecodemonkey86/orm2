package php.cls.bean.repo.method;

import generate.CodeUtil2;

import java.util.ArrayList;
import java.util.List;

import pg.PgCppUtil;
import model.AbstractRelation;
import model.Column;
import model.ManyRelation;
import model.OneRelation;
import model.OneToManyRelation;
import model.PrimaryKey;
import php.PhpFunctions;
import php.Types;
import php.cls.Attr;
import php.cls.Method;
import php.cls.Param;
import php.cls.bean.BeanCls;
import php.cls.bean.Beans;
import php.cls.bean.method.MethodOneRelationAttrSetter;
import php.cls.bean.method.MethodOneRelationBeanIsNull;
import php.cls.bean.repo.ClsBeanRepository;
import php.cls.expression.BoolExpression;

import php.cls.expression.Expression;
import php.cls.expression.Expressions;
import php.cls.expression.PhpStringLiteral;
import php.cls.expression.Var;
import php.cls.instruction.DoWhile;
import php.cls.instruction.IfBlock;
import php.lib.ClsBaseBean;
import php.lib.ClsMysqliResult;
import php.lib.ClsSqlQuery;
import util.StringUtil;
import codegen.CodeUtil;

public class MethodBeanLoad extends Method {

	protected PrimaryKey primaryKey;
	protected BeanCls bean;

	public MethodBeanLoad(BeanCls bean) {
		super(Public, Types.Void, "load"+bean.getName());

		this.primaryKey = bean.getTbl().getPrimaryKey();

		setStatic(true);

		addParam(new Param(bean, "bean"));
		this.bean = bean;
	}

	@Override
	public boolean includeIfEmpty() {
		return true;
	}

	@Override
	public void addImplementation() {
		Param pBean = getParam("bean");
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
				
				exprSqlQuery = exprSqlQuery.callMethod("leftJoin", Types.BeanRepository.callStaticMethod(ClsBeanRepository.getMethodNameGetTableName(Beans.get(r.getDestTable()))),new PhpStringLiteral(r.getAlias()), new PhpStringLiteral(CodeUtil2.concat(joinConditions," AND ")));
			}
			for(OneToManyRelation r:oneToManyRelations) {
				ArrayList<String> joinConditions=new ArrayList<>();
				for(int i=0;i<r.getColumnCount();i++) {
					joinConditions.add(CodeUtil.sp("b1."+r.getColumns(i).getValue1().getEscapedName(),'=',(r.getAlias())+"."+ r.getColumns(i).getValue2().getEscapedName()));
				}
				
				exprSqlQuery = exprSqlQuery.callMethod("leftJoin", Types.BeanRepository.callStaticMethod(ClsBeanRepository.getMethodNameGetTableName(Beans.get(r.getDestTable()))),new PhpStringLiteral(r.getAlias()), new PhpStringLiteral(CodeUtil2.concat(joinConditions," AND ")));
			}
			for(ManyRelation r:manyToManyRelations) {
				ArrayList<String> joinConditions=new ArrayList<>();
				for(int i=0;i<r.getSourceColumnCount();i++) {
					joinConditions.add(CodeUtil.sp("b1."+r.getSourceEntityColumn(i).getEscapedName(),'=',r.getAlias("mapping")+"."+ r.getSourceMappingColumn(i).getEscapedName()));
				}
				
				exprSqlQuery = exprSqlQuery.callMethod("leftJoin", new PhpStringLiteral(r.getMappingTable().getName()),new PhpStringLiteral(r.getAlias("mapping")), new PhpStringLiteral(CodeUtil2.concat(joinConditions," AND ")));
				
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
			
			Var row = _declare(Types.array(Types.Mixed), "row", res.callMethod(ClsMysqliResult.fetch_assoc) );
			IfBlock ifRowNotNull =
					_if(row.isNotNull())
					

						.setIfInstr(
								pBean.assign(Types.BeanRepository.callStaticMethod(MethodGetFromQueryAssocArray.getMethodName(bean),  row, new PhpStringLiteral("b1")))
								,
								pBean.callAttrSetterMethodInstr("loaded", BoolExpression.TRUE)//_assignInstruction(b1.accessAttr("loaded"), BoolExpression.TRUE)
								)
								;
			
			
			DoWhile doWhileQSqlQueryNext = DoWhile.create();
			
			
			ArrayList<AbstractRelation> manyRelations = new ArrayList<>(oneToManyRelations.size()+manyToManyRelations.size());
			manyRelations.addAll(oneToManyRelations);
			manyRelations.addAll(manyToManyRelations);
		
			for(OneRelation r:oneRelations) {
//				BeanCls foreignCls = Beans.get(r.getDestTable()); 
				IfBlock ifBlock= doWhileQSqlQueryNext._if(Expressions.and( pBean.callMethod(new MethodOneRelationBeanIsNull(r)),row.arrayIndex(new PhpStringLiteral(r.getAlias() + "__" + r.getDestTable().getPrimaryKey().getFirstColumn().getName())).isNotNull()) );
				ifBlock.thenBlock().
				_callMethodInstr(pBean, new MethodOneRelationAttrSetter( pBean.getClassConcreteType().getAttrByName(PgCppUtil.getOneRelationDestAttrName(r)), true,r.isPartOfPk()), 
						Types.BeanRepository.callStaticMethod(MethodGetFromQueryAssocArray.getMethodName(Beans.get(r.getDestTable())),  row, new PhpStringLiteral(r.getAlias())));
			}
			
			for(AbstractRelation r:manyRelations) {
				BeanCls foreignCls = Beans.get(r.getDestTable()); 
				
				if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
					//Var pkSet = ifRowNotNull.thenBlock()._declareNewArray(Types.array(foreignCls.getPkType()), "pkSet"+StringUtil.ucfirst(r.getAlias()));
					Expression[] arrayIndexExpressions = new Expression[1+r.getDestTable().getPrimaryKey().getColumnCount()];
					ArrayList<String> pkSprintf = new ArrayList<String> ();
					int i=0;
					
					for(Column colPk : r.getDestTable().getPrimaryKey().getColumns()) {
						pkSprintf.add(BeanCls.getTypeMapper().columnToType(colPk).getSprintfType());
						arrayIndexExpressions[++i] = row.arrayIndex(new PhpStringLiteral( r.getAlias()+"__"+colPk.getName()));
					}
					arrayIndexExpressions[0] = new PhpStringLiteral( CodeUtil.concat(pkSprintf, "_"));			
					Var pkSet = ifRowNotNull.thenBlock()._declareNewArray(Types.array(Types.Mixed), "pkSet"+StringUtil.ucfirst(r.getAlias()));
					Expression pkArrayIndex = pkSet.arrayIndex(PhpFunctions.sprintf.call(arrayIndexExpressions));
					IfBlock ifNotIssetPk = doWhileQSqlQueryNext._if(_not(PhpFunctions.isset.call(pkArrayIndex)));
					Var foreignBean = ifNotIssetPk.thenBlock()._declare(foreignCls, "b" + r.getAlias(),  Types.BeanRepository.callStaticMethod(MethodGetFromQueryAssocArray.getMethodName(Beans.get(r.getDestTable())),  row, new PhpStringLiteral(r.getAlias())));
					ifNotIssetPk.thenBlock()._assign(pkArrayIndex, foreignBean);
					ifNotIssetPk.thenBlock()._callMethodInstr(pBean, BeanCls.getAddRelatedBeanMethodName(r), foreignBean);
					
					
					//throw new RuntimeException("not implemented");
				} else {
					Column colPk = r.getDestTable().getPrimaryKey().getColumns().get(0);

					//IfBlock ifNotRecValueIsNull = doWhileQSqlQueryNext._if(Expressions.not(  Types.BeanRepository.callStaticMethod(MethodGetFromResultSet.getMethodName(Beans.get(r.getDestTable())),  resultSet, JavaString.fromStringConstant("pk"+r.getAlias()))));
					
					Var pkSet = ifRowNotNull.thenBlock()._declareNewArray(Types.array(Types.Mixed), "pkSet"+StringUtil.ucfirst(r.getAlias()));
					Expression pkArrayIndex = pkSet.arrayIndex(row.arrayIndex(new PhpStringLiteral( r.getAlias()+"__"+colPk.getName())));
					IfBlock ifNotIssetPk = doWhileQSqlQueryNext._if(_not(PhpFunctions.isset.call(pkArrayIndex)));
					Var foreignBean = ifNotIssetPk.thenBlock()._declare(foreignCls, "b" + r.getAlias(),  Types.BeanRepository.callStaticMethod(MethodGetFromQueryAssocArray.getMethodName(Beans.get(r.getDestTable())),  row, new PhpStringLiteral(r.getAlias())));
					ifNotIssetPk.thenBlock()._assign(pkArrayIndex, foreignBean);
					ifNotIssetPk.thenBlock()._callMethodInstr(pBean, BeanCls.getAddRelatedBeanMethodName(r), foreignBean);
					
				}
				
			}
			
			ifRowNotNull.addIfInstr(doWhileQSqlQueryNext);
			doWhileQSqlQueryNext.setCondition(ifRowNotNull.getCondition());
			doWhileQSqlQueryNext.addInstr(row.assign(res.callMethod(ClsMysqliResult.fetch_assoc)));
			
		}
		_callMethodInstr(pBean, ClsBaseBean.setLoaded, BoolExpression.TRUE);
	}

}
