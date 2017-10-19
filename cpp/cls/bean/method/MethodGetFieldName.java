package cpp.cls.bean.method;

import model.Column;
import cpp.Types;
import cpp.cls.Method;
import cpp.cls.Param;
import cpp.cls.QString;

public class MethodGetFieldName extends Method {

	protected Column col;
	protected boolean prefix;
	
	public MethodGetFieldName(Column col) {
		this(col,false);
	}
	
	public MethodGetFieldName(Column col, boolean prefix) {
		super(Public, Types.QString, getMethodName(col));
		setStatic(true);
		this.col = col;
		this.prefix = prefix;
		
		if (prefix) {
			addParam(new Param(Types.QString.toConstRef(), "prefix"));	
		}
	}

	@Override
	public void addImplementation() {
		if (prefix) {
			_return(QString.fromStringConstant("%1_%2").callMethod("arg", getParam("prefix"), QString.fromStringConstant(col.getName())));
		} else {
			_return(QString.fromStringConstant(col.getName()));
		}
		
		
	}
	
	public static String getMethodName(Column col) {
		return "fieldName" + col.getUc1stCamelCaseName();
	}

}
