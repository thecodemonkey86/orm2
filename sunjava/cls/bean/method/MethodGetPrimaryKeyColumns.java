package sunjava.cls.bean.method;

import sunjava.Types;
import sunjava.cls.JavaString;
import sunjava.cls.Method;
import sunjava.cls.expression.ArrayInitList;
import model.Column;
import model.PrimaryKey;

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
