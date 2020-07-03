package cpp.core;

import codegen.CodeUtil;

public class QLatin1StringLiteral extends QStringLiteral {

	public QLatin1StringLiteral(String strConstant) {
		super(strConstant);
	}
	
	@Override
	public String toString() {
		return "QLatin1String"+CodeUtil.parentheses(expression.toString());
	}
}
