package cpp.cls;

import codegen.CodeUtil;
import cpp.Types;
import cpp.cls.expression.Expression;
import cpp.cls.expression.QChar;
import cpp.cls.expression.QStringPlusOperatorExpression;

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
		return Types.QString;
	}
	
	@Override
	public String toString() {
		return expression.getType() == Types.QString ? expression.toString() : "QString"+CodeUtil.parentheses(expression.toString());
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


}
