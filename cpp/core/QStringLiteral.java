package cpp.core;

import codegen.CodeUtil;
import cpp.core.expression.StringConstant;

public class QStringLiteral extends QString {

	public QStringLiteral(String strConstant) {
		super(new StringConstant(strConstant));
	}

	@Override
	public String toString() {
		String s = expression.toString();
		if(s.equals("\"\"")) {
			return "QLatin1Literal(\"\")"; 
		/*} else if(s.matches("\\A\\p{ASCII}*\\z")) {
			return "QLatin1Literal"+CodeUtil.parentheses(s);
			-> .arg(...) is missing in QLatin1String
			*/
		} else {
			return "QStringLiteral"+CodeUtil.parentheses(s);
		}
		
		
	
	}
}
