package cpp.entityquery;

import cpp.Types;
import cpp.core.Attr;
import cpp.core.Cls;
import cpp.entity.EntityCls;

public class ClsEntityQueryIterator extends Cls{

	public ClsEntityQueryIterator(EntityCls entity) {
		super(entity.getName()+"QueryIterator");
		
		addAttr(new Attr(Attr.Private,Types.QSqlQuery,"query", null, false));
	}

}
