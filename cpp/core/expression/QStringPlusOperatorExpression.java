package cpp.core.expression;

import cpp.core.QString;
import cpp.core.QStringConcatOperator;

public class QStringPlusOperatorExpression extends BinaryOperatorExpression {

	public QStringPlusOperatorExpression(Var q1, Var q2) {
		super(q1, new QStringConcatOperator(), q2);
	}
	
	public QStringPlusOperatorExpression(Var q1, QString q2) {
		super(q1, new QStringConcatOperator(), q2);
	}
	
	public QStringPlusOperatorExpression(QString q1, Var q2) {
		super(q1, new QStringConcatOperator(), q2);
	}
	
	public QStringPlusOperatorExpression(QString q1, QString q2) {
		super(q1, new QStringConcatOperator(), q2);
	}
	
	public QStringPlusOperatorExpression(QStringPlusOperatorExpression q1, QString q2) {
		super(q1, new QStringConcatOperator(), q2);
	}
	
	public QStringPlusOperatorExpression(QStringPlusOperatorExpression q1, QStringPlusOperatorExpression q2) {
		super(q1, new QStringConcatOperator(), q2);
	}
	
	public QStringPlusOperatorExpression(QStringPlusOperatorExpression q1, Var q2) {
		super(q1, new QStringConcatOperator(), q2);
	}

	public QStringPlusOperatorExpression(QString q1, QChar chr) {
		super(q1, new QStringConcatOperator(), chr);
	}

	public QStringPlusOperatorExpression concat(QString qs) {
		return new QStringPlusOperatorExpression(this, qs);
	}
	
	public QStringPlusOperatorExpression concat(Var qs) {
		return new QStringPlusOperatorExpression(this, qs);
	}
	
	public QStringPlusOperatorExpression concat(QStringPlusOperatorExpression qs) {
		return new QStringPlusOperatorExpression(this, qs);
	}

}
