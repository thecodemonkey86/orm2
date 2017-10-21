package php.bean.method;

import database.relation.PrimaryKey;
import php.bean.BeanCls;
import php.core.Param;
import php.core.PhpFunctions;
import php.core.Types;
import php.core.expression.InlineIfExpression;
import php.core.expression.IntExpression;
import php.core.expression.PhpStringLiteral;
import php.core.expression.Var;
import php.core.instruction.IfBlock;
import php.core.method.Method;
import php.lib.PhpStringType;

public class MethodGetLimitQueryString extends Method {
	PrimaryKey pk;
	
	public MethodGetLimitQueryString(PrimaryKey pk) {
		super(Public, Types.String, "getLimitQueryString");
		this.pk = pk;
		setStatic(true);
		addParam(new Param(Types.Int, "limit"));
		addParam(new Param(Types.Int , "offset"));
		addParam(new Param(Types.String, "condition"));
	}

	@Override
	public void addImplementation() {
		String mainBeanAlias = "b1.";
		BeanCls bean = (BeanCls) parent;
		StringBuilder sql = new StringBuilder();
		if (pk.isMultiColumn()) {
			sql.append('(').append(mainBeanAlias).append(pk.getFirstColumn().getEscapedName());
			for(int i=1;i<pk.getColumnCount();i++) {
				sql.append(',').append(mainBeanAlias).append(pk.getColumn(i).getEscapedName());
			}
			sql.append(')');
		} else {
			sql.append(mainBeanAlias).append(pk.getFirstColumn().getEscapedName());
		}
		sql.append(" IN (SELECT ").append(pk.getFirstColumn().getEscapedName());
		for(int i=1;i<pk.getColumnCount();i++) {
			sql.append(',').append(pk.getColumn(i).getEscapedName());
		}
		sql.append(" FROM ").append(bean.getTbl().getEscapedName()).append(" WHERE %1" );
		
		Var varSql  = _declare(returnType, "sql", 
				PhpFunctions.sprintf.call( new PhpStringLiteral(sql.toString()), new InlineIfExpression(getParam("condition").callMethod("length").equalsOp(new IntExpression(0)), new PhpStringLiteral(BeanCls.getDatabase().getBooleanExpressionTrue()), getParam("condition")) ));
		Param paramLimit = getParam("limit");
		Param paramOffset = getParam("offset");
		IfBlock ifIsSetLimit = _if(paramLimit.greaterThan(new IntExpression(-1)));
		ifIsSetLimit.addIfInstr(varSql.binOp("+=", PhpFunctions.sprintf.call(new PhpStringLiteral(" LIMIT %s"), paramLimit)).asInstruction());
		IfBlock ifIsSetOffset = _if(paramOffset.greaterThan(new IntExpression(-1)));
		ifIsSetOffset.addIfInstr(varSql.binOp("+=", PhpFunctions.sprintf.call(new PhpStringLiteral(" OFFSET %s"), paramOffset)).asInstruction());
		addInstr( varSql.binOp("+=", new PhpStringLiteral(')')).asInstruction());
		_return(varSql);
	}
	
	
}
