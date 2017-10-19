package cpp.cls.bean.method;

import cpp.Types;
import cpp.cls.Method;
import cpp.cls.Param;
import cpp.cls.QString;
import cpp.cls.bean.BeanCls;
import cpp.cls.expression.InlineIfExpression;
import cpp.cls.expression.IntExpression;
import cpp.cls.expression.QChar;
import cpp.cls.expression.Var;
import cpp.cls.instruction.IfBlock;
import model.PrimaryKey;

public class MethodGetLimitQueryString extends Method {
	PrimaryKey pk;
	
	public MethodGetLimitQueryString(PrimaryKey pk) {
		super(Public, Types.QString, "getLimitQueryString");
		this.pk = pk;
		setStatic(true);
		addParam(new Param(Types.Int64, "limit"));
		addParam(new Param(Types.Int64, "offset"));
		addParam(new Param(Types.QString.toConstRef(), "condition"));
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
		
		Var varSql  = _declareInitConstructor(returnType, "sql", QString.fromStringConstant(sql.toString()).callMethod("arg", new InlineIfExpression(getParam("condition").callMethod("isEmpty"), QString.fromStringConstant(BeanCls.getDatabase().getBooleanExpressionTrue()), getParam("condition")) ));
		Param paramLimit = getParam("limit");
		Param paramOffset = getParam("offset");
		IfBlock ifIsSetLimit = _if(paramLimit.greaterThan(new IntExpression(-1)));
		ifIsSetLimit.addIfInstr(varSql.binOp("+=", QString.fromStringConstant(" LIMIT %1").callMethod("arg", paramLimit)).asInstruction());
		IfBlock ifIsSetOffset = _if(paramOffset.greaterThan(new IntExpression(-1)));
		ifIsSetOffset.addIfInstr(varSql.binOp("+=", QString.fromStringConstant(" OFFSET %1").callMethod("arg", paramOffset)).asInstruction());
		addInstr( varSql.binOp("+=", QChar.fromChar(')')).asInstruction());
		_return(varSql);
	}
	
	
}
