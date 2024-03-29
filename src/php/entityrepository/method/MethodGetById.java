package php.entityrepository.method;

import java.util.ArrayList;
import java.util.List;

import codegen.CodeUtil;
import database.column.Column;
import database.relation.AbstractRelation;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import php.core.Attr;
import php.core.Param;
import php.core.PhpFunctions;
import php.core.Type;
import php.core.Types;
import php.core.expression.ArrayAccessExpression;
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
import php.entity.method.MethodOneRelationBeanIsNull;
import php.entitypk.method.MethodPkHash;
import php.entityrepository.ClsEntityRepository;
import php.lib.ClsFirebirdSqlQuery;
import php.lib.ClsSqlQuery;
import php.orm.OrmUtil;
import util.CodeUtil2;
import util.StringUtil;
import util.pg.PgCppUtil;

public class MethodGetById extends Method {

	protected EntityCls entity;
	Param pTransactionHandle ;
	
	public MethodGetById(EntityCls cls) {
//		super(Public, cls, "getById");
		super(Public, cls.toNullable(), getMethodName(cls));
		for(Column col:cls.getTbl().getPrimaryKey().getColumns()) {
			Type colType = EntityCls.getTypeMapper().columnToType(  col);
			addParam(new Param(colType.isPrimitiveType() ? colType : colType, col.getCamelCaseName()));
			
		}
		if( EntityCls.getTypeMapper().hasTransactionHandle()) {
			pTransactionHandle = addParam(new Param(Types.Resource, "transactionHandle",Expressions.Null));
		
		}
		setStatic(true);
		this.entity=cls;
	}

	public static String getMethodName(EntityCls cls) {
		return "get"+cls.getName()+"ById";
	}

//	@Override
//	public ThisBeanRepositoryExpression _this() {
//		return new ThisBeanRepositoryExpression((ClsBeanRepository) parent);
//	}
	
	private Expression getFetchExpression(Var res) {
		return EntityCls.getTypeMapper().getDefaultFetchExpression(res);
		
	}
	
	@Override
	public void addImplementation() {
		//		StringBuilder sbSql = new StringBuilder(CodeUtil.sp("select");
		ClsEntityRepository parent = (ClsEntityRepository) this.parent;
		
		List<OneRelation> oneRelations = entity.getOneRelations();
		List<OneToManyRelation> oneToManyRelations = entity.getOneToManyRelations();
		List<ManyRelation> manyToManyRelations = entity.getManyToManyRelations();
		
		Attr aSqlCon = parent.getAttrByName("sqlCon");
		//Method mBuildQuery = aSqlCon.getClassType().getMethod("buildQuery");
		Var sqlQuery = _declare(EntityCls.getSqlQueryCls(), "query",EntityCls.getSqlQueryCls().newInstance(aSqlCon));
		PhpStringLiteral e1Alias = new PhpStringLiteral("e1");
		
		List<AbstractRelation> allRelations = new ArrayList<>(oneRelations.size()+oneToManyRelations.size()+manyToManyRelations.size());
		allRelations.addAll(oneRelations);
		allRelations.addAll(oneToManyRelations);
		allRelations.addAll(manyToManyRelations);
		
		Expression exprSqlQuery = sqlQuery.callMethod("select",  parent.callStaticMethod(ClsEntityRepository.getMethodNameGetAllSelectFields(entity),e1Alias) )
									.callMethod("from", Types.EntityRepository.callStaticMethod(ClsEntityRepository.getMethodNameGetTableName(entity),e1Alias));
		
		for(OneRelation r:oneRelations) {
			ArrayList<String> joinConditions=new ArrayList<>();
			for(int i=0;i<r.getColumnCount();i++) {
				joinConditions.add(CodeUtil.sp("e1."+r.getColumns(i).getValue1().getEscapedName(),'=',(r.getAlias())+"."+ r.getColumns(i).getValue2().getEscapedName()));
			}
			
			exprSqlQuery = exprSqlQuery.callMethod("leftJoin", Types.EntityRepository.callStaticMethod(ClsEntityRepository.getMethodNameGetTableName(Entities.get(r.getDestTable())),new PhpStringLiteral(r.getAlias())), new PhpStringLiteral(CodeUtil2.concat(joinConditions," AND ")));
		}
		for(OneToManyRelation r:oneToManyRelations) {
			ArrayList<String> joinConditions=new ArrayList<>();
			for(int i=0;i<r.getColumnCount();i++) {
				joinConditions.add(CodeUtil.sp("e1."+r.getColumns(i).getValue1().getEscapedName(),'=',(r.getAlias())+"."+ r.getColumns(i).getValue2().getEscapedName()));
			}
			
			exprSqlQuery = exprSqlQuery.callMethod("leftJoin", Types.EntityRepository.callStaticMethod(ClsEntityRepository.getMethodNameGetTableName(Entities.get(r.getDestTable())),new PhpStringLiteral(r.getAlias())), new PhpStringLiteral(CodeUtil2.concat(joinConditions," AND ")));
		}
		for(ManyRelation r:manyToManyRelations) {
			ArrayList<String> joinConditions=new ArrayList<>();
			for(int i=0;i<r.getSourceColumnCount();i++) {
				joinConditions.add(CodeUtil.sp("e1."+r.getSourceEntityColumn(i).getEscapedName(),'=',r.getAlias("mapping")+"."+ r.getSourceMappingColumn(i).getEscapedName()));
			}
			
			exprSqlQuery = exprSqlQuery.callMethod("leftJoin", new PhpStringLiteral(r.getMappingTable().getEscapedName()+" " + r.getAlias("mapping")), new PhpStringLiteral(CodeUtil2.concat(joinConditions," AND ")));
			
			joinConditions.clear();
			for(int i=0;i<r.getDestColumnCount();i++) {
				joinConditions.add(CodeUtil.sp(r.getAlias("mapping")+"."+r.getDestMappingColumn(i).getEscapedName(),'=',r.getAlias()+"."+r.getDestEntityColumn(i).getEscapedName() ));
			}
			
			exprSqlQuery = exprSqlQuery.callMethod("leftJoin", Types.EntityRepository.callStaticMethod(ClsEntityRepository.getMethodNameGetTableName(Entities.get(r.getDestTable())),new PhpStringLiteral(r.getAlias())), new PhpStringLiteral(CodeUtil2.concat(joinConditions," AND ")));
			
		}

		
		for(Column col:entity.getTbl().getPrimaryKey().getColumns()) {
			exprSqlQuery = exprSqlQuery.callMethod("where", new PhpStringLiteral("e1."+ col.getEscapedName()+"=?"),getParam(col.getCamelCaseName()));
					
		}
		
		if(pTransactionHandle != null ) {
			_callMethodInstr(sqlQuery, ClsFirebirdSqlQuery.setTransactionHandle, pTransactionHandle);
		}
		
		exprSqlQuery = exprSqlQuery.callMethod(ClsSqlQuery.query);
		Var res = _declare(exprSqlQuery.getType(),
				"res", exprSqlQuery
				);
		Var e1 = _declare(returnType, "e1", Expressions.Null);
		Var row = _declare(Types.array(Types.Mixed), "row", getFetchExpression(res) );
		IfBlock ifRowNotNull =
				_if(row)
				

					.setIfInstr(
							e1.assign(Types.EntityRepository.callStaticMethod(MethodGetFromQueryAssocArray.getMethodName(entity),  row, new PhpStringLiteral("e1")))
							,
							e1.callAttrSetterMethodInstr("loaded", BoolExpression.TRUE)//_assignInstruction(e1.accessAttr("loaded"), BoolExpression.TRUE)
							)
							;
		
		if(!oneRelations.isEmpty() || !manyToManyRelations.isEmpty() || !oneToManyRelations.isEmpty()) {
		DoWhile doWhileRowIsNotNull = DoWhile.create();
		
		
		ArrayList<AbstractRelation> manyRelations = new ArrayList<>(oneToManyRelations.size()+manyToManyRelations.size());
		manyRelations.addAll(oneToManyRelations);
		manyRelations.addAll(manyToManyRelations);
	
		for(OneRelation r:oneRelations) {
//			BeanCls foreignCls = Beans.get(r.getDestTable()); 
			IfBlock ifBlock= doWhileRowIsNotNull._if(Expressions.and( e1.callMethod(new MethodOneRelationBeanIsNull(r)),row.arrayIndex(new PhpStringLiteral(EntityCls.getTypeMapper().filterFetchAssocArrayKey(r.getAlias() + "__" + r.getDestTable().getPrimaryKey().getFirstColumn().getName()))).isNotNull()) );
			ifBlock.thenBlock().
			_callMethodInstr(e1, new MethodOneRelationAttrSetter( e1.getClassConcreteType().getAttrByName(PgCppUtil.getOneRelationDestAttrName(r)), true), 
					Types.EntityRepository.callStaticMethod(MethodGetFromQueryAssocArray.getMethodName(Entities.get(r.getDestTable())),  row, new PhpStringLiteral(r.getAlias())));
		}
		
		for(AbstractRelation r:manyRelations) {
			EntityCls foreignCls = Entities.get(r.getDestTable()); 
			Type beanPk=OrmUtil.getRelationForeignPrimaryKeyType(r);
			
			Expression pkArrayIndex = null;
			IfBlock ifIsRowIndexNotNull = null;
			
			if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
				Expression[] foreignPkArgs = new Expression[r.getDestTable().getPrimaryKey().getColumnCount()];
				Expression[] ifIsRowIndexNotNullCondition = new Expression[foreignPkArgs.length];
				for(int i=0; i < r.getDestTable().getPrimaryKey().getColumnCount(); i++) {
					foreignPkArgs[i] = row.arrayIndex(new PhpStringLiteral( EntityCls.getTypeMapper().filterFetchAssocArrayKey(r.getAlias()+"__"+r.getDestTable().getPrimaryKey().getColumn(i).getName())));
					ifIsRowIndexNotNullCondition[i] = foreignPkArgs[i].isNotNull();
				}
				 ifIsRowIndexNotNull = ifRowNotNull.thenBlock()._if(Expressions.and(ifIsRowIndexNotNullCondition));
				
				
				Var foreignPk = ifIsRowIndexNotNull.thenBlock()._declareNew(beanPk, "foreignPk"+StringUtil.ucfirst(r.getAlias()),foreignPkArgs);
							
				Var pkSet = ifIsRowIndexNotNull.thenBlock()._declareNewArray(Types.array(Types.Mixed), "pkSet"+StringUtil.ucfirst(r.getAlias()));
				pkArrayIndex = pkSet.arrayIndex(foreignPk.callMethod(MethodPkHash.getMethodName()));
				
			} else {
				Column colPk = r.getDestTable().getPrimaryKey().getFirstColumn();
				
				
				ArrayAccessExpression arrayIndex = row.arrayIndex(new PhpStringLiteral(EntityCls.getTypeMapper().filterFetchAssocArrayKey( r.getAlias()+"__"+colPk.getName())));
				ifIsRowIndexNotNull = ifRowNotNull.thenBlock()._if(arrayIndex.isNotNull());
				
				Var pkSet = ifIsRowIndexNotNull.thenBlock()._declareNewArray(Types.array(Types.Mixed), "pkSet"+StringUtil.ucfirst(r.getAlias()));
				
				pkArrayIndex = pkSet.arrayIndex(arrayIndex);
			}
			IfBlock ifNotIssetPk = ifIsRowIndexNotNull.thenBlock()._if(_not(PhpFunctions.isset.call(pkArrayIndex)));
			Var foreignBean = ifNotIssetPk.thenBlock()._declare(foreignCls, "b" + r.getAlias(),  Types.EntityRepository.callStaticMethod(MethodGetFromQueryAssocArray.getMethodName(Entities.get(r.getDestTable())),  row, new PhpStringLiteral(r.getAlias())));
			ifNotIssetPk.thenBlock()._assign(pkArrayIndex, foreignBean);
			ifNotIssetPk.thenBlock()._callMethodInstr(e1, EntityCls.getAddRelatedBeanMethodName(r), foreignBean);
			doWhileRowIsNotNull.addInstr(ifIsRowIndexNotNull);
		}
		
		ifRowNotNull.addIfInstr(doWhileRowIsNotNull);
		doWhileRowIsNotNull.setCondition(ifRowNotNull.getCondition());
		doWhileRowIsNotNull.addInstr(row.assign(getFetchExpression(res)));
		}
		_return(e1);
	}

}
