package cpp.lib;

import cpp.Namespaces;
import cpp.Types;
import cpp.core.Attr;
import cpp.core.Cls;

public class ClsBaseEntity extends Cls{

	
	
	public ClsBaseEntity() {
		super("BaseEntity");
		
		Attr attrAutoIncrement = new Attr(Types.Bool, "autoIncrement");
		addAttr(attrAutoIncrement);
		Attr attrInsert = new Attr(Types.Bool, "insert");
		addAttr(new Attr(Types.Bool, "primaryKeyModified"));
		addAttr(attrInsert);
		addMethod(new LibMethod(Types.Void, "setInsertNew"));
		setUseNamespace(Namespaces.ORM2);
		//addAttr(new Attr(Attr.Public, Types.QLatin1String, "_EMPTYSTRING",null, true));
	}
}
