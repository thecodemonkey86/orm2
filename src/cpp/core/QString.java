package cpp.core;

import codegen.CodeUtil;
import config.cpp.CppOrmConfig;
import cpp.CoreTypes;
import cpp.core.expression.Expression;
import cpp.core.expression.MethodCall;
import cpp.core.expression.QChar;
import cpp.core.expression.QStringPlusOperatorExpression;
import cpp.entity.EntityCls;
import cpp.lib.ClsQString;

public class QString extends Expression {

	protected Expression expression;
	public QString() {
		
	}
	public QString(Expression expression) {
		this.expression = expression;
	}
	
	public static Expression fromStringConstant(String str,Expression emptyConstant) {
		return str.equals("") ? emptyConstant : new QStringLiteral(str);
	}
	
	public static QString fromStringConstant(String str) {
		return new QStringLiteral(str);
	}
	
	public static QString fromLatin1StringConstant(String str) {
		if(EntityCls.getCfg().getQtVersion().ordinal() < CppOrmConfig.QtVersion.V5_14.ordinal()) {
			return new QStringLiteral(str);
		}
		return new QLatin1StringLiteral(str);
	}
	
	public static QString fromExpression(Expression expression) {
		return new QString(expression);
	}
	
	@Override
	public Type getType() {
		return CoreTypes.QString;
	}
	
	@Override
	public String toString() {
		return expression != null ? expression.getType() == CoreTypes.QString ? expression.toString() : "QString"+CodeUtil.parentheses(expression.toString()) : "QString()";
	}
	
	public Expression getExpression() {
		return expression;
	}

	public static Expression concat(Expression e1,Expression e2) {
		return new QStringPlusOperatorExpression(e1, e2);
	}
	
	public QStringPlusOperatorExpression concat(QString qString) {
		return new QStringPlusOperatorExpression(this, qString);
	}
	
	public QStringPlusOperatorExpression concat(QChar chr) {
		return new QStringPlusOperatorExpression(this, chr);
	}
	
	public QStringPlusOperatorExpression concat(Expression e) {
		return new QStringPlusOperatorExpression(this, e);
	}
	
	public MethodCall arg(Expression...args ) {
		return callMethod(ClsQString.arg, args);
	}


}
