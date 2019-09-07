package cpp.lib;

import cpp.Types;
import cpp.core.Attr;
import cpp.core.Cls;
import cpp.core.method.MethodAttributeSetter;

public class ClsBaseEntity extends Cls{

	
	
	public ClsBaseEntity() {
		super("BaseEntity");
		
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
