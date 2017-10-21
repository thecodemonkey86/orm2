package cpp.core;

import codegen.CodeUtil;
import cpp.core.expression.StringConstant;

public class QStringLiteral extends QString {

	public QStringLiteral(String strConstant) {
		super(new StringConstant(strConstant));
	}

	@Override
	public String toString() {
		return "QStringLiteral"+CodeUtil.parentheses(expression.toString());
	}
}
