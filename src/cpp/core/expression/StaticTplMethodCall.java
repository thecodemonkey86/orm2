package cpp.core.expression;

import codegen.CodeUtil;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.Type;

public class StaticTplMethodCall extends Expression{
	protected Cls cls;
	protected Method method;
	protected Expression[] args;
	Type[] tplTypes;
	public StaticTplMethodCall(Cls cls, Method method, Expression[] args, Type[] tplTypes) {
		super();
		this.cls = cls;
		this.method = method;
		this.args = args;
		this.tplTypes = tplTypes;
	}

	@Override
	public Type getType() {
		return method.getReturnType();
	}

	@Override
	public String toString() {
		String[] tplTypes=new String[this.tplTypes.length];
		for(int i=0;i<tplTypes.length;i++) {
			tplTypes[i] = this.tplTypes[i].getName();
		}
		return cls.toDeclarationString()+"::"+method.getName()+ CodeUtil.abr(CodeUtil.commaSep(tplTypes))+CodeUtil.parentheses(CodeUtil.commaSep((Object[])args));
	}
}
