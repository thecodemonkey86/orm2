package php.beanrepository.method;

import generate.CodeUtil2;

import java.util.ArrayList;
import java.util.List;

import codegen.CodeUtil;
import database.column.Column;
import database.relation.AbstractRelation;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import php.bean.BeanCls;
import php.bean.Beans;
import php.bean.method.MethodOneRelationAttrSetter;
import php.bean.method.MethodOneRelationBeanIsNull;
import php.beanrepository.ClsBeanRepository;
import php.core.Attr;
import php.core.Param;
import php.core.PhpFunctions;
import php.core.Type;
import php.core.Types;
import php.core.expression.BoolExpression;
import php.core.expression.Expression;
import php.core.expression.Expressions;
import php.core.expression.PhpStringLiteral;
import php.core.expression.Var;
import php.core.instruction.DoWhile;
import php.core.instruction.IfBlock;
import php.core.method.Method;
import php.lib.ClsMysqliResult;
import php.lib.ClsSqlQuery;
import php.orm.OrmUtil;
import util.StringUtil;
import util.pg.PgCppUtil;

public class MethodGetById extends Method {

	protected BeanCls bean;
	public MethodGetById(BeanCls cls) {
//		super(Public, cls, "getById");
		super(Public, cls, "get"+cls.getName()+"ById");
		for(Column col:cls.getTbl().getPrimaryKey().getColumns()) {
			Type colType = BeanCls.getTypeMapper().columnToType(  col);
			addParam(new Param(colType.isPrimitiveType() ? colType : colType, col.getCamelCaseName()));
			
		}
		setStatic(true);
		this.bean=cls;
	}

//	@Override
//	public ThisBeanRepositoryExpression _this() {
//		return new ThisBeanRepositoryExpression((ClsBeanRepository) parent);
//	}
	
	@Override
	public void addImplementation() {
		//		StringBuilder sbSql = new StringBuilder(CodeUtil.sp("select");
		ClsBeanRepository parent = (ClsBeanRepository) this.parent;
		
		List<OneRelation> oneRelations = bean.getOneRelations();
		List<OneToManyRelation> oneToManyRelations = bean.getOneToManyRelations();
		List<ManyRelation> manyToManyRelations = bean.getManyToManyRelations();
		
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
			
			exprSqlQuery = exprSqlQuery.callMethod("leftJoin", new PhpStringLiteral(r.getMappingTable().getEscapedName()+" " + r.getAlias("mapping")), new PhpStringLiteral(CodeUtil2.concat(joinConditions," AND ")));
			
			joinConditions.clear();
			for(int i=0;i<r.getDestColumnCount();i++) {
				joinConditions.add(CodeUtil.sp(r.getAlias("mapping")+"."+r.getDestMappingColumn(i).getEscapedName(),'=',r.getAlias()+"."+r.getDestEntityColumn(i).getEscapedName() ));
			}
			
			exprSqlQuery = exprSqlQuery.callMethod("leftJoin", Types.BeanRepository.callStaticMethod(ClsBeanRepository.getMethodNameGetTableName(Beans.get(r.getDestTable())),new PhpStringLiteral(r.getAlias())), new PhpStringLiteral(CodeUtil2.concat(joinConditions," AND ")));
			
		}

		
		for(Column col:bean.getTbl().getPrimaryKey().getColumns()) {
			exprSqlQuery = exprSqlQuery.callMethod("where", new PhpStringLiteral("b1."+ col.getEscapedName()+"=?"),getParam(col.getCamelCaseName()));
					
		}
		exprSqlQuery = exprSqlQuery.callMethod(ClsSqlQuery.query);
		Var res = _declare(exprSqlQuery.getType(),
				"res", exprSqlQuery
				);
		Var b1 = _declare(returnType, "b1", Expressions.Null);
		Var row = _declare(Types.array(Types.Mixed), "row", res.callMethod(ClsMysqliResult.fetch_assoc) );
		IfBlock ifRowNotNull =
				_if(row.isNotNull())
				

					.setIfInstr(
							b1.assign(Types.BeanRepository.callStaticMethod(MethodGetFromQueryAssocArray.getMethodName(bean),  row, new PhpStringLiteral("b1")))
							,
							b1.callAttrSetterMethodInstr("loaded", BoolExpression.TRUE)//_assignInstruction(b1.accessAttr("loaded"), BoolExpression.TRUE)
							)
							;
		
		
		DoWhile doWhileRowIsNotNull = DoWhile.create();
		
		
		ArrayList<AbstractRelation> manyRelations = new ArrayList<>(oneToManyRelations.size()+manyToManyRelations.size());
		manyRelations.addAll(oneToManyRelations);
		manyRelations.addAll(manyToManyRelations);
	
		for(OneRelation r:oneRelations) {
//			BeanCls foreignCls = Beans.get(r.getDestTable()); 
			IfBlock ifBlock= doWhileRowIsNotNull._if(Expressions.and( b1.callMethod(new MethodOneRelationBeanIsNull(r)),row.arrayIndex(new PhpStringLiteral(r.getAlias() + "__" + r.getDestTable().getPrimaryKey().getFirstColumn().getName())).isNotNull()) );
			ifBlock.thenBlock().
			_callMethodInstr(b1, new MethodOneRelationAttrSetter( b1.getClassConcreteType().getAttrByName(PgCppUtil.getOneRelationDestAttrName(r)), true,r.isPartOfPk()), 
					Types.BeanRepository.callStaticMethod(MethodGetFromQueryAssocArray.getMethodName(Beans.get(r.getDestTable())),  row, new PhpStringLiteral(r.getAlias())));
		}
		
		for(AbstractRelation r:manyRelations) {
			BeanCls foreignCls = Beans.get(r.getDestTable()); 
			Type beanPk=OrmUtil.getRelationForeignPrimaryKeyType(r);
			
			Expression pkArrayIndex = null;
			if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
				Expression[] foreignPkArgs = new Expression[r.getDestTable().getPrimaryKey().getColumnCount()];
				
				for(int i=0; i < r.getDestTable().getPrimaryKey().getColumnCount(); i++) {
					foreignPkArgs[i] = row.arrayIndex(new PhpStringLiteral( r.getAlias()+"__"+r.getDestTable().getPrimaryKey().getColumn(i).getName()));
				}
				Var foreignPk = ifRowNotNull.thenBlock()._declareNew(beanPk, "foreignPk"+StringUtil.ucfirst(r.getAlias()),foreignPkArgs);
							
				Var pkSet = ifRowNotNull.thenBlock()._declareNewArray(Types.array(Types.Mixed), "pkSet"+StringUtil.ucfirst(r.getAlias()));
				pkArrayIndex = pkSet.arrayIndex(PhpFunctions.spl_object_hash.call(foreignPk));
				
			} else {
				Column colPk = r.getDestTable().getPrimaryKey().getFirstColumn();
				
				Var pkSet = ifRowNotNull.thenBlock()._declareNewArray(Types.array(Types.Mixed), "pkSet"+StringUtil.ucfirst(r.getAlias()));
				pkArrayIndex = pkSet.arrayIndex(row.arrayIndex(new PhpStringLiteral( r.getAlias()+"__"+colPk.getName())));
			}
			IfBlock ifNotIssetPk = doWhileRowIsNotNull._if(Expressions.and(_not(PhpFunctions.isset.call(pkArrayIndex)), row.arrayIndex( new PhpStringLiteral(r.getAlias()+"__"+r.getDestTable().getPrimaryKey().getFirstColumn().getName())).isNotNull()));
			Var foreignBean = ifNotIssetPk.thenBlock()._declare(foreignCls, "b" + r.getAlias(),  Types.BeanRepository.callStaticMethod(MethodGetFromQueryAssocArray.getMethodName(Beans.get(r.getDestTable())),  row, new PhpStringLiteral(r.getAlias())));
			ifNotIssetPk.thenBlock()._assign(pkArrayIndex, foreignBean);
			ifNotIssetPk.thenBlock()._callMethodInstr(b1, BeanCls.getAddRelatedBeanMethodName(r), foreignBean);
			
		}
		
		ifRowNotNull.addIfInstr(doWhileRowIsNotNull);
		doWhileRowIsNotNull.setCondition(ifRowNotNull.getCondition());
		doWhileRowIsNotNull.addInstr(row.assign(res.callMethod(ClsMysqliResult.fetch_assoc)));
		_return(b1);
	}

}
