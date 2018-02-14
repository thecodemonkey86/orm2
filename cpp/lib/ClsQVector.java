package cpp.lib;

import cpp.CoreTypes;
import cpp.core.TplCls;
import cpp.core.Type;

public class ClsQVector extends TplCls {

	public static final String CLSNAME="QVector";
	
	public static final String clear = "clear";
	public static final String append = "append";
	
	public ClsQVector(Type element) {
		super(CLSNAME, element);
		addMethod(new LibMethod(CoreTypes.Void, append));
		addMethod(new LibMethod(CoreTypes.Void, "remove"));
		addMethod(new LibMethod(CoreTypes.Void, "removeOne"));
		addMethod(new LibMethod(CoreTypes.Bool, "empty"));
		addMethod(new LibMethod(CoreTypes.Int, "size"));
		addMethod(new LibMethod(CoreTypes.Void, clear));
		addMethod(new LibMethod(element.toRef(), "at"));
	}

}
