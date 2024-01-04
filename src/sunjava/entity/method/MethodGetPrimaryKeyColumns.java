package sunjava.entity.method;

import database.column.Column;
import database.relation.PrimaryKey;
import sunjava.core.JavaString;
import sunjava.core.Method;
import sunjava.core.Types;
import sunjava.core.expression.ArrayInitList;

public class MethodGetPrimaryKeyColumns extends Method {
	PrimaryKey pk;
	
	public MethodGetPrimaryKeyColumns(PrimaryKey pk) {
		super(Public, Types.array(Types.String), "getPrimaryKeyColumns");
		this.pk = pk;
		setStatic(true);
	}

	@Override
	public void addImplementation() {
		ArrayInitList init = new ArrayInitList();
		for(Column col : pk.getColumns()) {
			init.addElement(JavaString.stringConstant(col.getEscapedName()));
		}
		
		_return(_declare(returnType, "pkCols", init));
	}
	
	
}
