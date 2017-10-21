package php.core;

import codegen.CodeUtil;
import php.core.expression.Expression;
import php.core.expression.Var;

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
		return super.toDeclarationString();
		
	}
	
	@Override
	public String toDeclarationString() {
		//return super.toDeclarationString();
		if (name.equals("noLoading")) {
			System.out.println();
		}
		return defaultValue == null? CodeUtil.sp(type.toDeclarationString(),"$"+name):CodeUtil.sp(type.toDeclarationString(),"$"+name,'=',defaultValue);
	}
	
	

	
}
