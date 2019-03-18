package sunjava.lib;

import java.util.ArrayList;

import sunjava.core.JavaCls;
import sunjava.core.expression.JavaStringPlusEqOperator;

public class ClsJavaString extends JavaCls {
	public static final String METHOD_NAME_LENGTH = "length";
	public static final String format = "format";
	public static final String METHOD_NAME_SUBSTRING = "substring";

	public ClsJavaString() {
		super("String", "java.lang");
		
	}

	public void addDeclarations() {
		operators = new ArrayList<>();
		operators.add(new JavaStringPlusEqOperator());
		
		addMethod(new LibMethod(this, METHOD_NAME_LENGTH));
		addMethod(new LibMethod(this, METHOD_NAME_SUBSTRING));
		addMethod(new StaticLibMethod(this, format));
	}
}
