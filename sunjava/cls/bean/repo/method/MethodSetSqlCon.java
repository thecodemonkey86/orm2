package sunjava.cls.bean.repo.method;

import sunjava.Types;
import sunjava.cls.Attr;
import sunjava.cls.JavaCls;
import sunjava.cls.Method;
import sunjava.cls.Param;
import sunjava.cls.instruction.AssignInstruction;

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
