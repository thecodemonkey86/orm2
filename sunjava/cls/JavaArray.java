package sunjava.cls;

public class JavaArray extends JavaGenericClass{

	public JavaArray( Type element) {
		super(null, element, "java.lang");
	}

	@Override
	public String toDeclarationString() {
		return element.getName()+"[]";
	}
}
