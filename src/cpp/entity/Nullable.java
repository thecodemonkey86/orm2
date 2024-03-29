package cpp.entity;

import cpp.Namespaces;
import cpp.Types;
import cpp.core.TplCls;
import cpp.core.Type;
import cpp.lib.LibMethod;

public class Nullable extends TplCls{
	public static final String val = "val";
	public static final String isNull = "isNull";
	public static final String setNull = "setNull";
	
	public Nullable( Type element) {
		super("Nullable", element);
		setUseNamespace(Namespaces.ORM2);
		if(element!=null) {
		
			addMethod(new LibMethod(Types.Void,setNull));
			addMethod(new LibMethod(Types.Bool, isNull));
		
			addMethod(new LibMethod(element.toConstRef(),val));
		}
		headerInclude="nullable";
	}

}
