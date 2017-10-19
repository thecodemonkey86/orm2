package sunjava.lib;

import codegen.CodeUtil;
import sunjava.Types;
import sunjava.cls.JavaGenericClass;
import sunjava.cls.Type;
import sunjava.cls.expression.IArrayAccessible;

public class ClsHashMap extends JavaGenericClass implements IArrayAccessible {
	protected Type valType;
	public static final String put = "put"; 
	public static final String get = "get"; 
	public static final String containsKey = "containsKey"; 
	
	public ClsHashMap(Type key, Type val) {
		super("HashMap", key, "java.util");
		if (val == null) {
			throw new NullPointerException();
		}
		valType= val;
		addMethod(new LibMethod(Types.Void, put));
		addMethod(new LibMethod(valType, get));
		addMethod(new LibMethod(Types.Bool, containsKey));
	}
	
	
	@Override
	public String toString() {
		return type + CodeUtil.abr(CodeUtil.commaSep(element.toString(),valType.toString()));
	}

	@Override
	public String toDeclarationString() {
		return type + CodeUtil.abr(CodeUtil.commaSep(element.toString(),valType.toString()));
	}
	
	@Override
	public Type getAccessType() {
		return valType;
	}

}
