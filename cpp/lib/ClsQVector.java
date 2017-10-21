package cpp.lib;

import cpp.Types;
import cpp.core.TplCls;
import cpp.core.Type;

public class ClsQVector extends TplCls {

	public static final String CLSNAME="QVector";
	
	public ClsQVector(Type element) {
		super(CLSNAME, element);
		addMethod(new LibMethod(Types.Void, "append"));
		addMethod(new LibMethod(Types.Void, "remove"));
		addMethod(new LibMethod(Types.Void, "removeOne"));
		addMethod(new LibMethod(Types.Bool, "empty"));
		addMethod(new LibMethod(Types.Int, "size"));
		addMethod(new LibMethod(element.toRef(), "at"));
	}

}
