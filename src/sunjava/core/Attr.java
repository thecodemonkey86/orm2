package sunjava.core;

import codegen.CodeUtil;
import sunjava.core.expression.Expression;
import sunjava.core.expression.Var;

public class Attr extends Var {
	Expression initValue;
	boolean isStatic;
	String visibility;
	
	public static final String Protected = "protected";
	public static final String Private = "private";
	public static final String Public = "public";
	
	public Attr(Type type, String name) {
		this(Protected, type, name, null, false);
	}
	
	public Attr(String visibility, Type type, String name, Expression initValue, boolean isStatic ) {
		super(type,name);
		this.initValue = initValue;
		this.isStatic = isStatic;
		this.visibility = visibility;
	}
	
	
	public boolean isStatic() {
		return isStatic;
	}
	
	@Override
	public String toDeclarationString() {
		return CodeUtil.sp(visibility,isStatic?"static":"" ,type.toDeclarationString(),getName(),(initValue!=null?'=':null),initValue)+";";
	}
	
	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}
	
	public void setInitValue(Expression initValue) {
		this.initValue = initValue;
	}
	
	public Expression getInitValue() {
		return initValue;
	}
	
	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}
	
	public String getVisibility() {
		return visibility;
	}
	
	public boolean isPublic() {
		return visibility.equals(Public);
	}
	
}
