package cpp.core;

import codegen.CodeUtil;
import cpp.core.expression.Expression;
import cpp.core.expression.Var;

public class Param extends Var{

	Expression defaultValue;
	
	public Param(Type type, String name) {
		super(type,name);
	}
	public Param(Type type, String name,Expression defaultValue) {
		super(type,name);
		this.defaultValue=defaultValue;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	
	public String toSourceString() {
		return CodeUtil.sp(type.toDeclarationString(),name);
		
	}
	
	@Override
	public String toDeclarationString() {
		return defaultValue == null? CodeUtil.sp(type.toDeclarationString(),name):CodeUtil.sp(type.toDeclarationString(),name,'=',defaultValue);
	}
	
}
