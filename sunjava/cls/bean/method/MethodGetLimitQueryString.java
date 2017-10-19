package sunjava.cls.bean.method;

import sunjava.Types;
import sunjava.cls.JavaString;
import sunjava.cls.Method;
import sunjava.cls.Param;
import sunjava.cls.bean.BeanCls;
import sunjava.cls.expression.CharExpression;
import sunjava.cls.expression.InlineIfExpression;
import sunjava.cls.expression.IntExpression;
import sunjava.cls.expression.Var;
import sunjava.cls.instruction.IfBlock;
import sunjava.lib.ClsJavaString;
import model.PrimaryKey;

public class MethodGetLimitQueryString extends Method {
	PrimaryKey pk;
	
	public MethodGetLimitQueryString(PrimaryKey pk) {
		super(Public, Types.String, "getLimitQueryString");
		this.pk = pk;
		setStatic(true);
		addParam(new Param(Types.Long, "limit"));
		addParam(new Param(Types.Long, "offset"));
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
				Types.String.callStaticMethod(ClsJavaString.format, JavaString.stringConstant(sql.toString()), new InlineIfExpression(getParam("condition").callMethod("length").equalsOp(new IntExpression(0)), JavaString.stringConstant(BeanCls.getDatabase().getBooleanExpressionTrue()), getParam("condition")) ));
		Param paramLimit = getParam("limit");
		Param paramOffset = getParam("offset");
		IfBlock ifIsSetLimit = _if(paramLimit.greaterThan(new IntExpression(-1)));
		ifIsSetLimit.addIfInstr(varSql.binOp("+=", Types.String.callStaticMethod(ClsJavaString.format,JavaString.stringConstant(" LIMIT %s"), paramLimit)).asInstruction());
		IfBlock ifIsSetOffset = _if(paramOffset.greaterThan(new IntExpression(-1)));
		ifIsSetOffset.addIfInstr(varSql.binOp("+=", Types.String.callStaticMethod(ClsJavaString.format,JavaString.stringConstant(" OFFSET %s"), paramOffset)).asInstruction());
		addInstr( varSql.binOp("+=", CharExpression.fromChar(')')).asInstruction());
		_return(varSql);
	}
	
	
}
