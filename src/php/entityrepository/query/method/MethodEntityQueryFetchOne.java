package php.entityrepository.query.method;

import java.util.ArrayList;
import java.util.List;

import database.column.Column;
import database.relation.AbstractRelation;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
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
import php.entity.Entities;
import php.entity.EntityCls;
import php.entity.method.MethodOneRelationAttrSetter;
import php.entity.method.MethodOneRelationBeanIsNull;
import php.entityrepository.method.MethodGetFromQueryAssocArray;
import php.lib.ClsSqlQuery;
import php.orm.OrmUtil;
import util.StringUtil;
import util.pg.PgCppUtil;

public class MethodEntityQueryFetchOne extends Method{
	EntityCls bean;
	
	public MethodEntityQueryFetchOne(EntityCls bean) {
		super(Public, bean.toNullable(), "fetchOne");
		this.bean=bean;
	}
	
	private Expression getFetchExpression(Var res) {
		return EntityCls.getTypeMapper().getDefaultFetchExpression(res);
		
	}

	@Override
	public void addImplementation() {

		List<OneRelation> oneRelations = bean.getOneRelations();
		List<OneToManyRelation> oneToManyRelations = bean.getOneToManyRelations();
		List<ManyRelation> manyToManyRelations = bean.getManyToManyRelations();
		
		Var e1 = _declare(returnType, "e1", Expressions.Null);
		Var res =_declare(EntityCls.getTypeMapper().getDatabaseResultType() , "res",_this().accessAttr("sqlQuery").callMethod(ClsSqlQuery.query) );
		
		Var row = _declare(Types.array(Types.Mixed), "row", getFetchExpression(res) );
		IfBlock ifRowNotNull =
				_if(row)
				

					.setIfInstr(
							e1.assign(Types.BeanRepository.callStaticMethod(MethodGetFromQueryAssocArray.getMethodName(bean),  row, new PhpStringLiteral("e1")))
							,
							e1.callAttrSetterMethodInstr("loaded", BoolExpression.TRUE)//_assignInstruction(e1.accessAttr("loaded"), BoolExpression.TRUE)
							)
							;
		
		
		
		DoWhile doWhileQueryNext = DoWhile.create();
		
		
		ArrayList<AbstractRelation> manyRelations = new ArrayList<>(oneToManyRelations.size()+manyToManyRelations.size());
		manyRelations.addAll(oneToManyRelations);
		manyRelations.addAll(manyToManyRelations);
		
		for(OneRelation r:oneRelations) {
//			BeanCls foreignCls = Beans.get(r.getDestTable()); 
			IfBlock ifBlock= doWhileQueryNext._if(Expressions.and( e1.callMethod(new MethodOneRelationBeanIsNull(r)),row.arrayIndex(new PhpStringLiteral(EntityCls.getTypeMapper().filterFetchAssocArrayKey(r.getAlias() + "__" + r.getDestTable().getPrimaryKey().getFirstColumn().getName()))).isNotNull()) );
			ifBlock.thenBlock().
			_callMethodInstr(e1, new MethodOneRelationAttrSetter( e1.getClassConcreteType().getAttrByName(PgCppUtil.getOneRelationDestAttrName(r)), true), 
					Types.BeanRepository.callStaticMethod(MethodGetFromQueryAssocArray.getMethodName(Entities.get(r.getDestTable())),  row, new PhpStringLiteral(r.getAlias())));
		}
		
		for(AbstractRelation r:manyRelations) {
			EntityCls foreignCls = Entities.get(r.getDestTable()); 
			Type beanPk=OrmUtil.getRelationForeignPrimaryKeyType(r);
			
			
			Expression pkArrayIndex = null;
			IfBlock ifNotPkForeignIsNull= doWhileQueryNext._if( row.arrayIndex(new PhpStringLiteral(EntityCls.getTypeMapper().filterFetchAssocArrayKey(r.getAlias()+"__"+ r.getDestTable().getPrimaryKey().getFirstColumn().getName()))).isNotNull());
			if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
				Expression[] foreignPkArgs = new Expression[r.getDestTable().getPrimaryKey().getColumnCount()];
				
				for(int i=0; i < r.getDestTable().getPrimaryKey().getColumnCount(); i++) {
					foreignPkArgs[i] = row.arrayIndex(new PhpStringLiteral( EntityCls.getTypeMapper().filterFetchAssocArrayKey(r.getAlias()+"__"+r.getDestTable().getPrimaryKey().getColumn(i).getName())));
				}
				
				
				
				Var foreignPk = ifNotPkForeignIsNull.thenBlock()._declareNew(beanPk, "foreignPk"+StringUtil.ucfirst(r.getAlias()),foreignPkArgs);
							
				Var pkSet = ifRowNotNull.thenBlock()._declareNewArray(Types.array(Types.Mixed), "pkSet"+StringUtil.ucfirst(r.getAlias()));
				pkArrayIndex = pkSet.arrayIndex(PhpFunctions.md5.call(PhpFunctions.serialize.call(foreignPk)));
				
			} else {
				Column colPk = r.getDestTable().getPrimaryKey().getColumns().get(0);
				
				Var pkSet = ifNotPkForeignIsNull.thenBlock()._declareNewArray(Types.array(Types.Mixed), "pkSet"+StringUtil.ucfirst(r.getAlias()));
				pkArrayIndex = pkSet.arrayIndex(row.arrayIndex(new PhpStringLiteral( EntityCls.getTypeMapper().filterFetchAssocArrayKey(r.getAlias()+"__"+colPk.getName()))));
			}
			IfBlock ifNotIssetPk = ifNotPkForeignIsNull.thenBlock()._if(_not(PhpFunctions.isset.call(pkArrayIndex)));
			Var foreignBean = ifNotIssetPk.thenBlock()._declare(foreignCls, "b" + r.getAlias(),  Types.BeanRepository.callStaticMethod(MethodGetFromQueryAssocArray.getMethodName(Entities.get(r.getDestTable())),  row, new PhpStringLiteral(r.getAlias())));
			ifNotIssetPk.thenBlock()._assign(pkArrayIndex, foreignBean);
			ifNotIssetPk.thenBlock()._callMethodInstr(e1, EntityCls.getAddRelatedBeanMethodName(r), foreignBean);
			
		}
		
		ifRowNotNull.addIfInstr(doWhileQueryNext);
		
		ArrayList<Expression> condExpressions = new ArrayList<>();
		condExpressions.add(ifRowNotNull.getCondition());
		for(Column colPk :  bean.getTbl().getPrimaryKey()) {
			condExpressions.add(row.arrayIndex(new PhpStringLiteral(EntityCls.getTypeMapper().filterFetchAssocArrayKey("e1__" + colPk.getName()))).cast(EntityCls.getTypeMapper().columnToType(colPk))._equals(e1.callAttrGetter(colPk.getCamelCaseName())));
		}
		
		doWhileQueryNext.setCondition(Expressions.and( condExpressions ));
		
		doWhileQueryNext.addInstr(row.assign(getFetchExpression(res)));
		_return(e1);
	}
	@Override
	public boolean includeIfEmpty() {
		return true;
	}
}
