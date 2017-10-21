package sunjava.core.expression;

import generate.CodeUtil2;
import sunjava.core.AbstractJavaCls;
import sunjava.core.JavaCls;
import sunjava.core.PrimitiveType;
import sunjava.core.Type;
import sunjava.lib.ClsJavaString;

public class NewOperator extends Expression {

	protected Type type;
	protected Expression[] args;
	
	public NewOperator(Type type, Expression...args) {
		if (type instanceof ClsJavaString && (args.length == 0 || args[0].getType() instanceof ClsJavaString)) {
			throw new RuntimeException("warning null string");
		}
		if(type instanceof PrimitiveType) {
			throw new RuntimeException("cannot instantiate primitive type");
		}
		if(((JavaCls)type).isAbstract()) {
			throw new RuntimeException("cannot instantiate abstract class");
		}
		this.type = type;		
		this.args = args;
	}
	
	@Override
	public String toString() {
		return CodeUtil2.sp("new",type.toDeclarationString()+CodeUtil2.parentheses(CodeUtil2.commaSep((Object[])args)));
	}
	
	@Override
	public Type getType() {
		return type;
	}

	
	@Override
	public void collectImports(AbstractJavaCls cls) {
		type.collectImports(cls);
		for(Expression a : args) {
			a.collectImports(cls);
		}
	}

}
