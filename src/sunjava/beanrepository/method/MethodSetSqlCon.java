package sunjava.beanrepository.method;

import sunjava.core.Attr;
import sunjava.core.JavaCls;
import sunjava.core.Method;
import sunjava.core.Param;
import sunjava.core.Types;
import sunjava.core.instruction.AssignInstruction;

public class MethodSetSqlCon extends Method {

	public MethodSetSqlCon() {
		super(Public, Types.Void, "setSqlConnection");
		addParam(new Param(Types.Connection, "sqlCon"));
		setStatic(true);
	}

	@Override
	public void addImplementation() {
		addInstr(new AssignInstruction(
				((JavaCls) parent).accessStaticAttribute(new Attr(Types.Connection, "sqlCon")), getParam("sqlCon")));
		
	}

}
