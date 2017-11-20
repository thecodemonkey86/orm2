package cpp.core;

import codegen.CodeUtil;
import cpp.core.expression.Expression;

public class TplMethodCall extends MethodCall{
	Type[] tplTypes;
	
	public TplMethodCall(Expression expression, Method method, Expression[] args, Type[] tplTypes) {
		super(expression, method, args);
		if(tplTypes == null) {
			throw new NullPointerException(); 
		}
		this.tplTypes = tplTypes;
	}

	@Override
	public String toString() {
		String[] strArgs=new String[args.length];
		for(int i=0;i<args.length;i++) {
			strArgs[i] = args[i].getReadAccessString();
		}
		String[] tplTypes=new String[this.tplTypes.length];
		for(int i=0;i<tplTypes.length;i++) {
			tplTypes[i] = this.tplTypes[i].getName();
		}
		
		return expression + (expression.getType().isPtr() ? "\n->" : ".") +method.getName()+ CodeUtil.abr(CodeUtil.commaSep(tplTypes))+CodeUtil.parentheses(CodeUtil.commaSep(strArgs));
	}
}
