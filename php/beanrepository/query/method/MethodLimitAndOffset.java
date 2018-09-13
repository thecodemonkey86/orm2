package php.beanrepository.query.method;

import database.relation.PrimaryKey;
import php.bean.BeanCls;
import php.beanrepository.query.ClsBeanQuery;
import php.core.Param;
import php.core.PhpFunctions;
import php.core.Types;
import php.core.expression.Expressions;
import php.core.expression.InlineIfExpression;
import php.core.expression.IntExpression;
import php.core.expression.PhpStringLiteral;
import php.core.expression.Var;
import php.core.instruction.IfBlock;
import php.core.method.Method;
import php.lib.ClsBaseBeanQuery;

public class MethodLimitAndOffset extends Method {

	Param pJoinTableAlias, pOn, pQueryParams, pCondition;
	Param pLimit, pOffset;
	BeanCls bean;

	public MethodLimitAndOffset(BeanCls bean, ClsBeanQuery parentType) {
		super(Public, parentType, "limitAndOffset");
		this.pLimit = addParam(Types.Int, "limit");
		this.pOffset = addParam(Types.Int, "offset");
		this.pCondition = addParam(new Param(Types.String, "condition", Expressions.Null));
		this.pQueryParams = addParam(new Param(Types.Mixed, "params", Expressions.Null));
		this.bean = bean;
	}

	@Override
	public void addImplementation() {
		//Expression aSqlQuery = _this().accessAttr("sqlQuery");
		
		PrimaryKey pk = bean.getTbl().getPrimaryKey();
		//String mainBeanAlias = "b1.";
		StringBuilder sql = new StringBuilder();
//		if (pk.isMultiColumn()) {
//			sql.append('(').append(mainBeanAlias).append(pk.getFirstColumn().getEscapedName());
//			for (int i = 1; i < pk.getColumnCount(); i++) {
//				sql.append(',').append(mainBeanAlias).append(pk.getColumn(i).getEscapedName());
//			}
//			sql.append(')');
//		} else {
//			sql.append(mainBeanAlias).append(pk.getFirstColumn().getEscapedName());
//		}
		sql.append(" (SELECT ").append(pk.getFirstColumn().getEscapedName());
		for (int i = 1; i < pk.getColumnCount(); i++) {
			sql.append(',').append(pk.getColumn(i).getEscapedName());
		}
		sql.append(" FROM ").append(bean.getTbl().getEscapedName()).append(" WHERE %s");

		Var varSql = _declare(returnType, "sql", PhpFunctions.sprintf.call(new PhpStringLiteral(sql.toString()),
				new InlineIfExpression(pCondition.isNull(),
						new PhpStringLiteral(BeanCls.getDatabase().getBooleanExpressionTrue()),
						pCondition)));
	
		IfBlock ifIsSetLimit = _if(pLimit.greaterThan(new IntExpression(-1)));
		ifIsSetLimit.addIfInstr(varSql
				.binOp(".=", PhpFunctions.sprintf.call(new PhpStringLiteral(" LIMIT %s"), pLimit)).asInstruction());
		IfBlock ifIsSetOffset = _if(pOffset.greaterThan(new IntExpression(-1)));
		ifIsSetOffset.addIfInstr(
				varSql.binOp(".=", PhpFunctions.sprintf.call(new PhpStringLiteral(" OFFSET %s"), pOffset))
						.asInstruction());
		addInstr(varSql.binOp(".=", new PhpStringLiteral(") _limitjoin")).asInstruction());

		addInstr(_this().assignAttr("beanQueryLimit", pLimit));
		addInstr(_this().assignAttr("beanQueryOffset", pOffset));
		_return(_this().callMethod(ClsBaseBeanQuery.join,varSql,new PhpStringLiteral("_limitjoin."+pk.getFirstColumn().getEscapedName()+" = b1."+pk.getFirstColumn().getEscapedName()),pQueryParams));

	}

}
