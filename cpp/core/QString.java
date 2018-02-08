package cpp.core;

import codegen.CodeUtil;
import cpp.CoreTypes;
import cpp.core.expression.Expression;
import cpp.core.expression.QChar;
import cpp.core.expression.QStringPlusOperatorExpression;

public class QString extends Expression {

	protected Expression expression;
	
	public QString(Expression expression) {
		this.expression = expression;
	}
	
	public static QString fromStringConstant(String str) {
		return new QStringLiteral(str);
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
		return expression.getType() == CoreTypes.QString ? expression.toString() : "QString"+CodeUtil.parentheses(expression.toString());
	}
	
	public Expression getExpression() {
		return expression;
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


}
