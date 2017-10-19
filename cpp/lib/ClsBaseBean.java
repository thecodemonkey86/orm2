package cpp.lib;

import cpp.Types;
import cpp.cls.Attr;
import cpp.cls.Cls;
import cpp.cls.method.MethodAttributeSetter;

public class ClsBaseBean extends Cls{

	
	
	public ClsBaseBean() {
		super("BaseBean");
		
		Attr attrAutoIncrement = new Attr(Types.Bool, "autoIncrement");
		addAttr(attrAutoIncrement);
		Attr attrInsert = new Attr(Types.Bool, "insert");
		addAttr(new Attr(Types.Bool, "primaryKeyModified"));
		addAttr(attrInsert);
		addMethod(new LibMethod(Types.Void, "setInsertNew"));
		Attr aLoaded = new Attr(Types.Bool, "loaded");
		addAttr(aLoaded);
		addMethod(new MethodAttributeSetter(aLoaded));
	}
}
