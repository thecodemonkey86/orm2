package cpp.core.expression;

import codegen.CodeUtil;
import cpp.core.Cls;
import cpp.core.Method;

public class BaseClassMethodCall extends MethodCall {
	Cls parent;
	
	public BaseClassMethodCall(Cls parent, Method method, Expression... args) {
		super(null, method, args);
		this.parent = parent;
	}

	public String toString() {
		String[] strArgs=new String[args.length];
		for(int i=0;i<args.length;i++) {
			strArgs[i] = args[i].getReadAccessString();
		}
		if(expression instanceof ThisExpression) {
			if(method.isStatic()) {
				throw new RuntimeException("this in static method");
			}
		}
		return parent.toUsageString()+"::"+ method.getName()+CodeUtil.parentheses(CodeUtil.commaSep(strArgs));
	}
}
