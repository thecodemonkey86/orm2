package php.beanrepository.query.method;

import java.util.ArrayList;
import java.util.List;

import database.column.Column;
import database.relation.AbstractRelation;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import php.bean.BeanCls;
import php.bean.Beans;
import php.bean.method.MethodOneRelationAttrSetter;
import php.bean.method.MethodOneRelationBeanIsNull;
import php.beanrepository.method.MethodGetFromQueryAssocArray;
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
import php.lib.ClsBaseBeanQuery;
import php.orm.OrmUtil;
import util.StringUtil;
import util.pg.PgCppUtil;

public class MethodBeanQueryFetchOne extends Method{
	BeanCls bean;
	
	public MethodBeanQueryFetchOne(BeanCls bean) {
		super(Public, bean.toNullable(), "fetchOne");
		this.bean=bean;
	}
	
	private Expression getFetchExpression(Var res) {
		return BeanCls.getTypeMapper().getDefaultFetchExpression(res);
		
	}

	@Override
	public void addImplementation() {

		List<OneRelation> oneRelations = bean.getOneRelations();
		List<OneToManyRelation> oneToManyRelations = bean.getOneToManyRelations();
		List<ManyRelation> manyToManyRelations = bean.getManyToManyRelations();
		
		Var b1 = _declare(returnType, "b1", Expressions.Null);
		Var res =_declare(BeanCls.getTypeMapper().getDatabaseResultType() , "res",_this().callMethod(ClsBaseBeanQuery.query) );
		
		Var row = _declare(Types.array(Types.Mixed), "row", getFetchExpression(res) );
		IfBlock ifRowNotNull =
				_if(row)
				

					.setIfInstr(
							b1.assign(Types.BeanRepository.callStaticMethod(MethodGetFromQueryAssocArray.getMethodName(bean),  row, new PhpStringLiteral("b1")))
							,
							b1.callAttrSetterMethodInstr("loaded", BoolExpression.TRUE)//_assignInstruction(b1.accessAttr("loaded"), BoolExpression.TRUE)
							)
							;
		
		
		
		DoWhile doWhileQueryNext = DoWhile.create();
		
		
		ArrayList<AbstractRelation> manyRelations = new ArrayList<>(oneToManyRelations.size()+manyToManyRelations.size());
		manyRelations.addAll(oneToManyRelations);
		manyRelations.addAll(manyToManyRelations);
		
		for(OneRelation r:oneRelations) {
//			BeanCls foreignCls = Beans.get(r.getDestTable()); 
			IfBlock ifBlock= doWhileQueryNext._if(Expressions.and( b1.callMethod(new MethodOneRelationBeanIsNull(r)),row.arrayIndex(new PhpStringLiteral(BeanCls.getTypeMapper().filterFetchAssocArrayKey(r.getAlias() + "__" + r.getDestTable().getPrimaryKey().getFirstColumn().getName()))).isNotNull()) );
			ifBlock.thenBlock().
			_callMethodInstr(b1, new MethodOneRelationAttrSetter( b1.getClassConcreteType().getAttrByName(PgCppUtil.getOneRelationDestAttrName(r)), true,r.isPartOfPk()), 
					Types.BeanRepository.callStaticMethod(MethodGetFromQueryAssocArray.getMethodName(Beans.get(r.getDestTable())),  row, new PhpStringLiteral(r.getAlias())));
		}
		
		for(AbstractRelation r:manyRelations) {
			BeanCls foreignCls = Beans.get(r.getDestTable()); 
			Type beanPk=OrmUtil.getRelationForeignPrimaryKeyType(r);
			
			
			Expression pkArrayIndex = null;
			IfBlock ifNotPkForeignIsNull= doWhileQueryNext._if( row.arrayIndex(new PhpStringLiteral(BeanCls.getTypeMapper().filterFetchAssocArrayKey(r.getAlias()+"__"+ r.getDestTable().getPrimaryKey().getFirstColumn().getName()))).isNotNull());
			if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
				Expression[] foreignPkArgs = new Expression[r.getDestTable().getPrimaryKey().getColumnCount()];
				
				for(int i=0; i < r.getDestTable().getPrimaryKey().getColumnCount(); i++) {
					foreignPkArgs[i] = row.arrayIndex(new PhpStringLiteral( BeanCls.getTypeMapper().filterFetchAssocArrayKey(r.getAlias()+"__"+r.getDestTable().getPrimaryKey().getColumn(i).getName())));
				}
				
				
				
				Var foreignPk = ifNotPkForeignIsNull.thenBlock()._declareNew(beanPk, "foreignPk"+StringUtil.ucfirst(r.getAlias()),foreignPkArgs);
							
				Var pkSet = ifRowNotNull.thenBlock()._declareNewArray(Types.array(Types.Mixed), "pkSet"+StringUtil.ucfirst(r.getAlias()));
				pkArrayIndex = pkSet.arrayIndex(PhpFunctions.md5.call(PhpFunctions.serialize.call(foreignPk)));
				
			} else {
				Column colPk = r.getDestTable().getPrimaryKey().getColumns().get(0);
				
				Var pkSet = ifNotPkForeignIsNull.thenBlock()._declareNewArray(Types.array(Types.Mixed), "pkSet"+StringUtil.ucfirst(r.getAlias()));
				pkArrayIndex = pkSet.arrayIndex(row.arrayIndex(new PhpStringLiteral( BeanCls.getTypeMapper().filterFetchAssocArrayKey(r.getAlias()+"__"+colPk.getName()))));
			}
			IfBlock ifNotIssetPk = ifNotPkForeignIsNull.thenBlock()._if(_not(PhpFunctions.isset.call(pkArrayIndex)));
			Var foreignBean = ifNotIssetPk.thenBlock()._declare(foreignCls, "b" + r.getAlias(),  Types.BeanRepository.callStaticMethod(MethodGetFromQueryAssocArray.getMethodName(Beans.get(r.getDestTable())),  row, new PhpStringLiteral(r.getAlias())));
			ifNotIssetPk.thenBlock()._assign(pkArrayIndex, foreignBean);
			ifNotIssetPk.thenBlock()._callMethodInstr(b1, BeanCls.getAddRelatedBeanMethodName(r), foreignBean);
			
		}
		
		ifRowNotNull.addIfInstr(doWhileQueryNext);
		
		ArrayList<Expression> condExpressions = new ArrayList<>();
		condExpressions.add(ifRowNotNull.getCondition());
		for(Column colPk :  bean.getTbl().getPrimaryKey()) {
			condExpressions.add(row.arrayIndex(new PhpStringLiteral(BeanCls.getTypeMapper().filterFetchAssocArrayKey("b1__" + colPk.getName()))).cast(BeanCls.getTypeMapper().columnToType(colPk))._equals(b1.callAttrGetter(colPk.getCamelCaseName())));
		}
		
		doWhileQueryNext.setCondition(Expressions.and( condExpressions ));
		
		doWhileQueryNext.addInstr(row.assign(getFetchExpression(res)));
		_return(b1);
	}
	@Override
	public boolean includeIfEmpty() {
		return true;
	}
}
