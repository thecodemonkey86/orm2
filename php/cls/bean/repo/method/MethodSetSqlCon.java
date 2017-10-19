package php.cls.bean.repo.method;

import php.Types;
import php.cls.Attr;
import php.cls.PhpCls;
import php.cls.Method;
import php.cls.Param;
import php.cls.instruction.AssignInstruction;

public class MethodSetSqlCon extends Method {

	public MethodSetSqlCon() {
		super(Public, Types.Void, "setSqlConnection");
		addParam(new Param(Types.mysqli, "sqlCon"));
		setStatic(true);
	}

	@Override
	public void addImplementation() {
		addInstr(new AssignInstruction(
				((PhpCls) parent).accessStaticAttribute(new Attr(Types.mysqli, "sqlCon")), getParam("sqlCon")));
		
	}

}
