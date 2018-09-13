package php.beanrepository.query.method;

import java.util.ArrayList;
import java.util.List;

import database.column.Column;
import database.relation.AbstractRelation;
import database.relation.OneRelation;
import database.relation.PrimaryKey;
import php.bean.BeanCls;
import php.bean.Beans;
import php.bean.method.MethodAttrSetterInternal;
import php.bean.method.MethodOneRelationBeanIsNull;
import php.beanrepository.method.MethodGetFromQueryAssocArray;
import php.core.PhpFunctions;
import php.core.Type;
import php.core.Types;
import php.core.expression.BoolExpression;
import php.core.expression.Expression;
import php.core.expression.Expressions;
import php.core.expression.NewOperator;
import php.core.expression.PhpStringLiteral;
import php.core.expression.Var;
import php.core.instruction.DoWhile;
import php.core.instruction.IfBlock;
import php.core.instruction.InstructionBlock;
import php.core.method.Method;
import php.lib.ClsBaseBean;
import php.lib.ClsSqlQuery;
import php.orm.OrmUtil;
import util.StringUtil;

public class MethodBeanQueryFetch extends Method{
	BeanCls bean;
	
	public MethodBeanQueryFetch(BeanCls bean) {
		super(Public, Types.array(bean).toNullable(), "fetch");
		this.bean=bean;
	}
	
	private Expression getFetchExpression(Var res) {
		return BeanCls.getTypeMapper().getDefaultFetchExpression(res);
		
	}

	@Override
	public void addImplementation() {

		List<OneRelation> oneRelations = bean.getOneRelations();
		PrimaryKey pk=bean.getTbl().getPrimaryKey();
		
		Var result = _declareNewArray( "result");
		Var res =_declare(Types.mysqli_result, "res",_this().accessAttr("sqlQuery").callMethod(ClsSqlQuery.query) );
		
		Type b1PkType = pk.isMultiColumn() ? bean.getPkType() : BeanCls.getTypeMapper().columnToType( pk.getFirstColumn());

		
		ArrayList<AbstractRelation> manyRelations = new ArrayList<>();
		
		manyRelations.addAll(bean.getOneToManyRelations());
		manyRelations.addAll(bean.getManyToManyRelations());
		
		Var row = _declare(Types.array(Types.Mixed), "row", getFetchExpression(res) );
		IfBlock ifRowNotNull =	_if(row);
		InstructionBlock ifInstr = ifRowNotNull.thenBlock();
		Var b1Map =  ifInstr._declareNewArray((!manyRelations.isEmpty()) ? Types.array(b1PkType, bean.getFetchListHelperCls()) : Types.array(b1PkType), "b1Map");
		
		DoWhile doWhileQueryNext = ifRowNotNull.thenBlock()._doWhile();
		doWhileQueryNext.setCondition(ifRowNotNull.getCondition());
		
		Var fetchListHelper = null;
		if (!manyRelations.isEmpty()) {
			fetchListHelper = doWhileQueryNext._declare(bean.getFetchListHelperCls(), "fetchListHelper", Expressions.Null);
		}
		Var b1DoWhile = doWhileQueryNext._declare(bean, "b1", Expressions.Null);
		Expression b1ArrayIndexExpression = null;
		if (pk.isMultiColumn()) {
			
			Expression[] b1PkArgs = new Expression[pk.getColumnCount()];
			for(int i = 0; i < pk.getColumnCount(); i++) {
				b1PkArgs[i] = row.arrayIndex(new PhpStringLiteral(BeanCls.getTypeMapper().filterFetchAssocArrayKey("b1__" + pk.getColumn(i).getName())));
			}
			
			Var b1Pk = doWhileQueryNext._declareNew(bean.getPkType(), "b1pk", b1PkArgs);
			b1ArrayIndexExpression = PhpFunctions.md5.call(PhpFunctions.serialize.call(b1Pk));
			//throw new RuntimeException("not implemented");
		} else {
			b1ArrayIndexExpression = row.arrayIndex( new PhpStringLiteral(BeanCls.getTypeMapper().filterFetchAssocArrayKey("b1__"+ pk.getFirstColumn().getName())));
		}
		
		IfBlock ifNotB1SetContains = doWhileQueryNext._if(Expressions.not(PhpFunctions.isset.call(b1Map.arrayIndex(b1ArrayIndexExpression))));
		
		
	
		
		ifNotB1SetContains.thenBlock()._assign(b1DoWhile, Types.BeanRepository.callStaticMethod(MethodGetFromQueryAssocArray.getMethodName(bean), row,  new PhpStringLiteral("b1")));
		
		if (!manyRelations.isEmpty()) {
			
			
			ifNotB1SetContains.thenBlock()._assign(fetchListHelper, new NewOperator(fetchListHelper.getType(), b1DoWhile));
			ifNotB1SetContains.thenBlock()._arraySet(b1Map, b1ArrayIndexExpression, fetchListHelper );
			
			ifNotB1SetContains.elseBlock()._assign(fetchListHelper, b1Map.arrayIndex( b1ArrayIndexExpression));		
			ifNotB1SetContains.elseBlock()._assign(b1DoWhile, fetchListHelper.callMethod("getB1"));		
			
			for(AbstractRelation r:manyRelations) {
				Type beanPk=OrmUtil.getRelationForeignPrimaryKeyType(r);
				Expression foreignBeanExpression = Types.BeanRepository.callStaticMethod(MethodGetFromQueryAssocArray.getMethodName(Beans.get(r.getDestTable())), row,  new PhpStringLiteral(r.getAlias()));
//				IfBlock ifRecValueIsNotNull = null;
				Var foreignBean = null;				
				
				IfBlock ifNotPkForeignIsNull= doWhileQueryNext._if( row.arrayIndex(new PhpStringLiteral(BeanCls.getTypeMapper().filterFetchAssocArrayKey(r.getAlias()+"__"+ r.getDestTable().getPrimaryKey().getFirstColumn().getName()))).isNotNull());
				
				Var pkForeign = null;
				if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
					Expression[] argsRelationPk = new Expression[r.getDestTable().getPrimaryKey().getColumnCount()];
					for(int i = 0; i < r.getDestTable().getPrimaryKey().getColumnCount();i++) {
						argsRelationPk[i] = row.arrayIndex(new PhpStringLiteral(BeanCls.getTypeMapper().filterFetchAssocArrayKey(r.getAlias()+"__"+ r.getDestTable().getPrimaryKey().getColumn(i).getName())));
					}
					pkForeign = ifNotPkForeignIsNull.thenBlock()._declareNew(beanPk, "pkForeignB"+r.getAlias(), argsRelationPk);
					
					
					
				} else {
					Column colPk=r.getDestTable().getPrimaryKey().getFirstColumn();
					pkForeign = ifNotPkForeignIsNull.thenBlock()._declare(beanPk, "pkForeignB"+r.getAlias(), row.arrayIndex(new PhpStringLiteral(BeanCls.getTypeMapper().filterFetchAssocArrayKey(r.getAlias()+"__"+ colPk.getName()))));
					
				}
				
				IfBlock ifNotContainsRelationPk = ifNotPkForeignIsNull.thenBlock()._if(
						Expressions.not(fetchListHelper
										.callMethod("containsPk" + StringUtil.ucfirst(r.getAlias()),
												pkForeign													
												))
								
						
						
					);
				
				foreignBean =ifNotContainsRelationPk.thenBlock()
						
						
						._declare(foreignBeanExpression.getType(), "foreignB"+r.getAlias(),foreignBeanExpression) ;
				
								
				ifNotContainsRelationPk.thenBlock().addInstr(b1DoWhile
						.callMethodInstruction(BeanCls.getAddRelatedBeanMethodName(r), foreignBean));
				ifNotContainsRelationPk.thenBlock().addInstr(
						fetchListHelper.callMethod("addPk" + StringUtil.ucfirst(r.getAlias()), 
								pkForeign
									).asInstruction())
					 ;
				
				
			}
			

			
		} else {
			/* manyRelations.isEmpty() */
			ifNotB1SetContains.thenBlock()._arraySet(b1Map,  b1ArrayIndexExpression, b1DoWhile);
		}
		for(OneRelation r:oneRelations) {
			BeanCls foreignCls = Beans.get(r.getDestTable());
			Expression foreignBeanExpression = Types.BeanRepository.callStaticMethod(MethodGetFromQueryAssocArray.getMethodName(foreignCls), row, new PhpStringLiteral(r.getAlias()));
			
			IfBlock ifRelatedBeanIsNull= ifNotB1SetContains.thenBlock().
					_if(b1DoWhile.callMethod(new MethodOneRelationBeanIsNull(r)));
			
			Var foreignBean =ifRelatedBeanIsNull.thenBlock()._declare(foreignBeanExpression.getType(), "foreignB"+r.getAlias(),foreignBeanExpression) ;
			ifRelatedBeanIsNull.thenBlock()
				._callMethodInstr(
						b1DoWhile ,
						new MethodAttrSetterInternal(foreignCls,
								bean.getAttrByName(OrmUtil.getOneRelationDestAttrName(r)))
						,  foreignBean);
			
		
			for (OneRelation foreignOneRelation: foreignCls.getOneRelations()) {
				if (foreignOneRelation.getDestTable().equals(bean.getTbl())) {
					ifRelatedBeanIsNull.thenBlock().addInstr(foreignBean.callMethodInstruction("set"+r.getSourceTable().getUc1stCamelCaseName()+"Internal", b1DoWhile));
				}
			}
			
		}
		doWhileQueryNext._assign(row, getFetchExpression(res));
		ifNotB1SetContains.thenBlock()._callMethodInstr(b1DoWhile, ClsBaseBean.setLoaded, BoolExpression.TRUE);
		ifNotB1SetContains.thenBlock()._arrayPush(result, b1DoWhile);
		_return(result);
	}
	@Override
	public boolean includeIfEmpty() {
		return true;
	}
}
