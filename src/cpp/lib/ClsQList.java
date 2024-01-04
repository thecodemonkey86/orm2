package cpp.lib;

import cpp.CoreTypes;
import cpp.core.TplCls;
import cpp.core.Type;
import cpp.core.expression.IArrayAccessible;

public class ClsQList extends TplCls implements IArrayAccessible{

	public static final String CLSNAME="QList";
	
	public static final String clear = "clear";
	public static final String append = "append";
	public static final String removeOne = "removeOne";
	public static final String contains = "contains";
	public static final String size = "size";
	public static final String last = "last";
	public static final String empty = "empty";
	
	public ClsQList(Type element) {
		super(CLSNAME, element);
		addMethod(new LibMethod(CoreTypes.Void, append));
		addMethod(new LibMethod(CoreTypes.Void, "remove"));
		addMethod(new LibMethod(CoreTypes.Void, removeOne));
		addMethod(new LibMethod(CoreTypes.Bool, empty));
		addMethod(new LibMethod(CoreTypes.SizeT, size));
		addMethod(new LibMethod(CoreTypes.Void, clear));
		addMethod(new LibMethod(CoreTypes.Bool, contains));
		addMethod(new LibMethod(element.toRef(), last));
		addMethod(new LibMethod(element.toRef(), "at"));
	}

	@Override
	public Type getAccessType() {
		return element;
	}

}
