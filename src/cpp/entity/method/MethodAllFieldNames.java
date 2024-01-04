package cpp.entity.method;


import java.util.ArrayList;

import cpp.Types;
import cpp.core.Method;
import cpp.core.expression.Var;
import cpp.entity.EntityCls;
import cpp.lib.ClsQStringList;
import database.column.Column;

public class MethodAllFieldNames extends Method {
	public MethodAllFieldNames() {
		super(Public, Types.QStringList, "getAllFieldNames");
	}

	@Override
	public void addImplementation() {
		Var result = _declare(returnType, "_result");
		ArrayList<Column> columns = ((EntityCls) parent).getTbl().getAllColumns();
		if(columns.isEmpty()) {
			throw new RuntimeException();
		}
		for(Column c:columns) {
			if(!c.hasRelation()) {
				addInstr( result.callMethodInstruction(ClsQStringList.append, parent.callStaticMethod(MethodGetFieldName.getMethodName(c))));
			}
		}
		
		_return(result);
		setStatic(true);

	}

}
