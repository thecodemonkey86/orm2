package php.entityrepository.query.method;

import java.util.ArrayList;

import codegen.CodeUtil;
import database.column.Column;
import database.relation.PrimaryKey;
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
import php.entity.EntityCls;
import php.entityrepository.query.ClsEntityQuery;
import php.lib.ClsBaseEntityQuery;

public class MethodLimitAndOffset extends Method {

	Param pJoinTableAlias, pOn, pQueryParams, pCondition;
	Param pLimit, pOffset;
	EntityCls entity;

	public MethodLimitAndOffset(EntityCls entity, ClsEntityQuery parentType) {
		super(Public, parentType, "limitAndOffset");
		this.pLimit = addParam(Types.Int, "limit");
		this.pOffset = addParam(Types.Int, "offset");
		this.pCondition = addParam(new Param(Types.String, "condition", Expressions.Null));
		this.pQueryParams = addParam(new Param(Types.Mixed, "params", Expressions.Null));
		this.entity = entity;
	}

	@Override
	public void addImplementation() {
		//Expression aSqlQuery = _this().accessAttr("sqlQuery");
		
		PrimaryKey pk = entity.getTbl().getPrimaryKey();
		//String mainEntityAlias = "e1.";
		StringBuilder sql = new StringBuilder();
//		if (pk.isMultiColumn()) {
//			sql.append('(').append(mainEntityAlias).append(pk.getFirstColumn().getEscapedName());
//			for (int i = 1; i < pk.getColumnCount(); i++) {
//				sql.append(',').append(mainEntityAlias).append(pk.getColumn(i).getEscapedName());
//			}
//			sql.append(')');
//		} else {
//			sql.append(mainEntityAlias).append(pk.getFirstColumn().getEscapedName());
//		}
		sql.append(" (SELECT ").append(pk.getFirstColumn().getEscapedName());
		for (int i = 1; i < pk.getColumnCount(); i++) {
			sql.append(',').append(pk.getColumn(i).getEscapedName());
		}
		sql.append(" FROM ").append(entity.getTbl().getEscapedName()).append(" WHERE %s");

		Var varSql = _declare(returnType, "sql", PhpFunctions.sprintf.call(new PhpStringLiteral(sql.toString()),
				new InlineIfExpression(pCondition.isNull(),
						new PhpStringLiteral(EntityCls.getDatabase().getBooleanExpressionTrue()),
						pCondition)));
	
		IfBlock ifIsSetLimit = _if(pLimit.greaterThan(new IntExpression(-1)));
		ifIsSetLimit.addIfInstr(varSql
				.binOp(".=", PhpFunctions.sprintf.call(new PhpStringLiteral(" LIMIT %s"), pLimit)).asInstruction());
		IfBlock ifIsSetOffset = _if(pOffset.greaterThan(new IntExpression(-1)));
		ifIsSetOffset.addIfInstr(
				varSql.binOp(".=", PhpFunctions.sprintf.call(new PhpStringLiteral(" OFFSET %s"), pOffset))
						.asInstruction());
		addInstr(varSql.binOp(".=", new PhpStringLiteral(") _limitjoin")).asInstruction());

		addInstr(_this().assignAttr("entityQueryLimit", pLimit));
		addInstr(_this().assignAttr("entityQueryOffset", pOffset));
		ArrayList<String> listJoinCondition = new ArrayList<>();
		
		for(Column pkCol : pk) {
			listJoinCondition.add("_limitjoin."+pkCol.getEscapedName()+" = e1."+pkCol.getEscapedName());
		}
		
		_return(_this().callMethod(ClsBaseEntityQuery.join,varSql,new PhpStringLiteral(CodeUtil.concat(listJoinCondition, " AND ")),pQueryParams));

	}

}
