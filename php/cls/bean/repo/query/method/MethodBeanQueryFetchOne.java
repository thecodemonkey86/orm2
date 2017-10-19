package php.cls.bean.repo.query.method;

import java.util.ArrayList;
import java.util.List;

import codegen.CodeUtil;
import pg.PgCppUtil;
import model.AbstractRelation;
import model.Column;
import model.ManyRelation;
import model.OneRelation;
import model.OneToManyRelation;
import php.PhpFunctions;
import php.Types;
import php.cls.Method;
import php.cls.Type;
import php.cls.bean.BeanCls;
import php.cls.bean.Beans;
import php.cls.bean.method.MethodOneRelationAttrSetter;
import php.cls.bean.method.MethodOneRelationBeanIsNull;
import php.cls.bean.repo.method.MethodGetFromQueryAssocArray;
import php.cls.expression.BoolExpression;
import php.cls.expression.Expression;
import php.cls.expression.Expressions;
import php.cls.expression.PhpStringLiteral;
import php.cls.expression.Var;
import php.cls.instruction.DoWhile;
import php.cls.instruction.IfBlock;
import php.lib.ClsBaseBeanQuery;
import php.lib.ClsMysqliResult;
import php.orm.OrmUtil;
import util.StringUtil;

public class MethodBeanQueryFetchOne extends Method{
	BeanCls bean;
	
	public MethodBeanQueryFetchOne(BeanCls bean) {
		super(Public, bean, "fetchOne");
		this.bean=bean;
	}

	@Override
	public void addImplementation() {

		List<OneRelation> oneRelations = bean.getOneRelations();
		List<OneToManyRelation> oneToManyRelations = bean.getOneToManyRelations();
		List<ManyRelation> manyToManyRelations = bean.getManyToManyRelations();
		
		Var b1 = _declare(returnType, "b1", Expressions.Null);
		Var res =_declare(Types.mysqli_result, "res",_this().callMethod(ClsBaseBeanQuery.query) );
		
		Var row = _declare(Types.array(Types.Mixed), "row", res.callMethod(ClsMysqliResult.fetch_assoc) );
		IfBlock ifRowNotNull =
				_if(row.isNotNull())
				

					.setIfInstr(
							b1.assign(Types.BeanRepository.callStaticMethod(MethodGetFromQueryAssocArray.getMethodName(bean),  row, new PhpStringLiteral("b1")))
							,
							b1.callAttrSetterMethodInstr("loaded", BoolExpression.TRUE)//_assignInstruction(b1.accessAttr("loaded"), BoolExpression.TRUE)
							)
							;
		
		
		
		DoWhile doWhileQSqlQueryNext = DoWhile.create();
		
		
		ArrayList<AbstractRelation> manyRelations = new ArrayList<>(oneToManyRelations.size()+manyToManyRelations.size());
		manyRelations.addAll(oneToManyRelations);
		manyRelations.addAll(manyToManyRelations);
		
		for(OneRelation r:oneRelations) {
//			BeanCls foreignCls = Beans.get(r.getDestTable()); 
			IfBlock ifBlock= doWhileQSqlQueryNext._if(Expressions.and( b1.callMethod(new MethodOneRelationBeanIsNull(r)),row.arrayIndex(new PhpStringLiteral(r.getAlias() + "__" + r.getDestTable().getPrimaryKey().getFirstColumn().getName())).isNotNull()) );
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
				Column colPk = r.getDestTable().getPrimaryKey().getColumns().get(0);
				
				Var pkSet = ifRowNotNull.thenBlock()._declareNewArray(Types.array(Types.Mixed), "pkSet"+StringUtil.ucfirst(r.getAlias()));
				pkArrayIndex = pkSet.arrayIndex(row.arrayIndex(new PhpStringLiteral( r.getAlias()+"__"+colPk.getName())));
			}
			IfBlock ifNotIssetPk = doWhileQSqlQueryNext._if(_not(PhpFunctions.isset.call(pkArrayIndex)));
			Var foreignBean = ifNotIssetPk.thenBlock()._declare(foreignCls, "b" + r.getAlias(),  Types.BeanRepository.callStaticMethod(MethodGetFromQueryAssocArray.getMethodName(Beans.get(r.getDestTable())),  row, new PhpStringLiteral(r.getAlias())));
			ifNotIssetPk.thenBlock()._assign(pkArrayIndex, foreignBean);
			ifNotIssetPk.thenBlock()._callMethodInstr(b1, BeanCls.getAddRelatedBeanMethodName(r), foreignBean);
			
		}
		
		ifRowNotNull.addIfInstr(doWhileQSqlQueryNext);
		doWhileQSqlQueryNext.setCondition(ifRowNotNull.getCondition());
		doWhileQSqlQueryNext.addInstr(row.assign(res.callMethod(ClsMysqliResult.fetch_assoc)));
		_return(b1);
	}
	@Override
	public boolean includeIfEmpty() {
		return true;
	}
}
