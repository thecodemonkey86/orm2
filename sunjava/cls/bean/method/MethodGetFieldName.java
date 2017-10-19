package sunjava.cls.bean.method;

import model.Column;
import sunjava.Types;
import sunjava.cls.JavaString;
import sunjava.cls.Method;
import sunjava.cls.Param;
import sunjava.lib.ClsJavaString;

public class MethodGetFieldName extends Method {

	protected Column col;
	protected boolean prefix;
	
	public MethodGetFieldName(Column col) {
		this(col,false);
	}
	
	public MethodGetFieldName(Column col, boolean prefix) {
		super(Public, Types.String, getMethodName(col));
		setStatic(true);
		this.col = col;
		this.prefix = prefix;
		
		if (prefix) {
			addParam(new Param(Types.String, "prefix"));	
		}
	}

	@Override
	public void addImplementation() {
		if (prefix) {
			_return(Types.String.callStaticMethod(ClsJavaString.format, JavaString.stringConstant("%s_%s"), getParam("prefix"), JavaString.stringConstant(col.getName())));
		} else {
			_return(JavaString.stringConstant(col.getName()));
		}
		
		
	}
	
	public static String getMethodName(Column col) {
		return "fieldName" + col.getUc1stCamelCaseName();
	}

}
