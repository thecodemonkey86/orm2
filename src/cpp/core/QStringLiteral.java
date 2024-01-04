package cpp.core;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import codegen.CodeUtil;
import cpp.core.expression.StringConstant;

public class QStringLiteral extends QString {
	//private boolean argCalled;
	
	private static boolean utf8Bom;
	
	public static void setUtf8Bom(boolean utf8Bom) {
		QStringLiteral.utf8Bom = utf8Bom;
	}
	
	public QStringLiteral(String strConstant) {
		super(new StringConstant(strConstant));
	}

	@Override
	public String toString() {
		/*String s = expression.toString();
		if(s.equals("\"\"")) {
			return "QLatin1String(\"\")";
		} else if(s.matches("\\A\\p{ASCII}*\\z")) {
			
			
//			if(EntityCls.getCfg().getQtVersion().ordinal()<CppOrmConfig.QtVersion.V5_14.ordinal()) {
		//  because .arg(...) is missing in QLatin1String (before 5.14), or currently, 5.15, cannot handle integer args
				if(s.contains("%1")) {
					return "QStringLiteral"+CodeUtil.parentheses(s);
				}
//			}
			
			return "QLatin1String"+CodeUtil.parentheses(s);
			
		} else {
			return "QStringLiteral"+CodeUtil.parentheses(s);
		}*/
		if(!utf8Bom) {
			String s = expression.toString();
			byte[] latin1 = s.getBytes(StandardCharsets.US_ASCII);
			byte[] utf8 = s.getBytes(StandardCharsets.UTF_8);
			if(!Arrays.equals(utf8, latin1)) {
				return "QString::fromUtf8"+CodeUtil.parentheses(expression);
			}
		}
		
		return "QStringLiteral"+CodeUtil.parentheses(expression);
	}
	
//	@Override
//	public MethodCall arg(Expression...args ) {
//		argCalled = true;
//		return super.arg(args);
//	}
}
