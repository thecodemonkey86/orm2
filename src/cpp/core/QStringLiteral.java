package cpp.core;

import codegen.CodeUtil;
import cpp.core.expression.StringConstant;

public class QStringLiteral extends QString {
	//private boolean argCalled;
	
	public QStringLiteral(String strConstant) {
		super(new StringConstant(strConstant));
	}

	@Override
	public String toString() {
		String s = expression.toString();
		if(s.equals("\"\"")) {
			return "QLatin1String(\"\")";
		} else if(s.matches("\\A\\p{ASCII}*\\z")) {
			// TODO causes ambiguous method calls: disabled temporarily
			
			//  because .arg(...) is missing in QLatin1String
			
			if(s.contains("%1")) {
				return "QStringLiteral"+CodeUtil.parentheses(s);
			}
			
			return "QLatin1String"+CodeUtil.parentheses(s);
			
		} else {
			return "QStringLiteral"+CodeUtil.parentheses(s);
		}
		
		
	
	}
	
//	@Override
//	public MethodCall arg(Expression...args ) {
//		argCalled = true;
//		return super.arg(args);
//	}
}
