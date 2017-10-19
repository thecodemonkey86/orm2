package php.cls.bean.repo.query.method;

import java.util.ArrayList;
import java.util.List;

import model.AbstractRelation;
import model.Column;
import model.OneRelation;
import model.PrimaryKey;
import php.PhpFunctions;
import php.Types;
import php.cls.Method;
import php.cls.Type;
import php.cls.bean.BeanCls;
import php.cls.bean.Beans;
import php.cls.bean.method.MethodAttrSetterInternal;
import php.cls.bean.method.MethodOneRelationBeanIsNull;
import php.cls.bean.repo.method.MethodGetFromQueryAssocArray;
import php.cls.expression.BoolExpression;
import php.cls.expression.Expression;
import php.cls.expression.Expressions;
import php.cls.expression.NewOperator;
import php.cls.expression.PhpStringLiteral;
import php.cls.expression.Var;
import php.cls.instruction.DoWhile;
import php.cls.instruction.IfBlock;
import php.cls.instruction.InstructionBlock;

import php.lib.ClsBaseBean;
import php.lib.ClsBaseBeanQuery;
import php.lib.ClsMysqliResult;
import php.orm.OrmUtil;
import util.StringUtil;

public class MethodBeanQueryFetch extends Method{
	BeanCls bean;
	
	public MethodBeanQueryFetch(BeanCls bean) {
		super(Public, Types.array(bean), "fetch");
		this.bean=bean;
	}

	@Override
	public void addImplementation() {

		List<OneRelation> oneRelations = bean.getOneRelations();
		PrimaryKey pk=bean.getTbl().getPrimaryKey();
		
		Var result = _declareNewArray( "result");
		Var res =_declare(Types.mysqli_result, "res",_this().callMethod(ClsBaseBeanQuery.query) );
		
		Type b1PkType = pk.isMultiColumn() ? bean.getPkType() : BeanCls.getTypeMapper().columnToType( pk.getFirstColumn());

		
		ArrayList<AbstractRelation> manyRelations = new ArrayList<>();
		
		manyRelations.addAll(bean.getOneToManyRelations());
		manyRelations.addAll(bean.getManyToManyRelations());
		
		Var row = _declare(Types.array(Types.Mixed), "row", res.callMethod(ClsMysqliResult.fetch_assoc) );
		IfBlock ifRowNotNull =	_if(row.isNotNull());
		InstructionBlock ifInstr = ifRowNotNull.thenBlock();
		Var b1Map =  ifInstr._declareNewArray((!manyRelations.isEmpty()) ? Types.array(b1PkType, bean.getFetchListHelperCls()) : Types.array(b1PkType), "b1Map");
		
		DoWhile doWhileQueryNext = ifRowNotNull.thenBlock()._doWhile();
		doWhileQueryNext.setCondition(ifRowNotNull.getCondition());
		Var b1DoWhile = doWhileQueryNext._declare(bean, "b1", Expressions.Null);
		Expression b1ArrayIndexExpression = null;
		if (pk.isMultiColumn()) {
			
			Expression[] b1PkArgs = new Expression[pk.getColumnCount()];
			for(int i = 0; i < pk.getColumnCount(); i++) {
				b1PkArgs[i] = row.arrayIndex(new PhpStringLiteral("b1__" + pk.getColumn(i).getName()));
			}
			
			Var b1Pk = doWhileQueryNext._declareNew(bean.getPkType(), "b1pk", b1PkArgs);
			b1ArrayIndexExpression = PhpFunctions.spl_object_hash.call(b1Pk);
			//throw new RuntimeException("not implemented");
		} else {
			b1ArrayIndexExpression = row.arrayIndex( new PhpStringLiteral("b1__"+ pk.getFirstColumn().getName()));
		}
		
		IfBlock ifNotB1SetContains = doWhileQueryNext._if(Expressions.not(PhpFunctions.isset.call(b1Map.arrayIndex(b1ArrayIndexExpression))));
		
		
	
		
		ifNotB1SetContains.thenBlock()._assign(b1DoWhile, Types.BeanRepository.callStaticMethod(MethodGetFromQueryAssocArray.getMethodName(bean), res,  new PhpStringLiteral("b1")));
		
		if (!manyRelations.isEmpty()) {
			
			Var fetchListHelper = doWhileQueryNext._declare(bean.getFetchListHelperCls(), "fetchListHelper", Expressions.Null);
			ifNotB1SetContains.thenBlock()._assign(fetchListHelper, new NewOperator(fetchListHelper.getType(), b1DoWhile));
			ifNotB1SetContains.thenBlock()._arraySet(b1Map, b1ArrayIndexExpression, fetchListHelper );
			
			ifNotB1SetContains.elseBlock()._assign(fetchListHelper, b1Map.arrayIndex( b1ArrayIndexExpression));		
			ifNotB1SetContains.elseBlock()._assign(b1DoWhile, fetchListHelper.callMethod("getB1"));		
			
			for(AbstractRelation r:manyRelations) {
				Type beanPk=OrmUtil.getRelationForeignPrimaryKeyType(r);
				Expression foreignBeanExpression = Types.BeanRepository.callStaticMethod(MethodGetFromQueryAssocArray.getMethodName(bean), res,  new PhpStringLiteral(r.getAlias()));
//				IfBlock ifRecValueIsNotNull = null;
				Var foreignBean = null;				
				
				IfBlock ifNotPkForeignIsNull= doWhileQueryNext._if( row.arrayIndex(new PhpStringLiteral(r.getAlias()+"__"+ r.getDestTable().getPrimaryKey().getFirstColumn().getName())).isNotNull());
				
				Var pkForeign = null;
				if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
					Expression[] argsRelationPk = new Expression[r.getDestTable().getPrimaryKey().getColumnCount()];
					for(int i = 0; i < r.getDestTable().getPrimaryKey().getColumnCount();i++) {
						argsRelationPk[i] = row.arrayIndex(new PhpStringLiteral(r.getAlias()+"__"+ r.getDestTable().getPrimaryKey().getColumn(i).getName()));
					}
					pkForeign = ifNotPkForeignIsNull.thenBlock()._declareNew(beanPk, "pkForeignB"+r.getAlias(), argsRelationPk);
					
					
					
				} else {
					Column colPk=r.getDestTable().getPrimaryKey().getFirstColumn();
					pkForeign = ifNotPkForeignIsNull.thenBlock()._declare(beanPk, "pkForeignB"+r.getAlias(), row.arrayIndex(new PhpStringLiteral(r.getAlias()+"__"+ colPk.getName())));
					
				}
				
				IfBlock ifNotContainsRelationPk = ifNotPkForeignIsNull.thenBlock()._if(
						Expressions.not(fetchListHelper
										.callMethod("containsPk" + StringUtil.ucfirst(r.getAlias()),
												pkForeign													
												))
								
						
						
					);
				
				foreignBean =ifNotContainsRelationPk.thenBlock()._declare(foreignBeanExpression.getType(), "foreignB"+r.getAlias(),foreignBeanExpression) ;
				
								
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
			Expression foreignBeanExpression = Types.BeanRepository.callStaticMethod(MethodGetFromQueryAssocArray.getMethodName(foreignCls), res, new PhpStringLiteral(r.getAlias()));
			
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
		doWhileQueryNext._assign(row, res.callMethod(ClsMysqliResult.fetch_assoc));
		ifNotB1SetContains.thenBlock()._callMethodInstr(b1DoWhile, ClsBaseBean.setLoaded, BoolExpression.TRUE);
		ifNotB1SetContains.thenBlock()._arrayPush(result, b1DoWhile);
		_return(result);
	}
	@Override
	public boolean includeIfEmpty() {
		return true;
	}
}
