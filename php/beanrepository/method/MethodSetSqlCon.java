package php.beanrepository.method;

import php.bean.BeanCls;
import php.core.Attr;
import php.core.Param;
import php.core.PhpCls;
import php.core.Types;
import php.core.instruction.AssignInstruction;
import php.core.method.Method;

public class MethodSetSqlCon extends Method {

	public MethodSetSqlCon() {
		super(Public, Types.Void, "setSqlConnection");
		addParam(new Param(BeanCls.getTypeMapper().getDatabaseLinkType(), "sqlCon"));
		setStatic(true);
	}

	@Override
	public void addImplementation() {
		addInstr(new AssignInstruction(
				((PhpCls) parent).accessStaticAttribute(new Attr(Types.mysqli, "sqlCon")), getParam("sqlCon")));
		
	}

}
