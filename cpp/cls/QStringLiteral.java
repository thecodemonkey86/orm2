package cpp.cls;

import codegen.CodeUtil;
import cpp.cls.expression.StringConstant;

public class QStringLiteral extends QString {

	public QStringLiteral(String strConstant) {
		super(new StringConstant(strConstant));
	}

	@Override
	public String toString() {
		return "QStringLiteral"+CodeUtil.parentheses(expression.toString());
	}
}
