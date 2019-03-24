package php.core;

import codegen.CodeUtil;
import php.Php;
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
		String v = "$"+name;
		String def = defaultValue != null ? CodeUtil.sp('=',defaultValue): null;
		if(type.typeHinting()) {
			if(type instanceof INullable && !Php.phpVersion.supportsNullableTypeHint()) {
				return CodeUtil.sp(v,def);
			}
			return CodeUtil.sp(type.toDeclarationString(),v,def);  
		} else {
			return CodeUtil.sp(v,def);
		}
		
	
		
	}
	
	

	
}
