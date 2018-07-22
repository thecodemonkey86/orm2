package php.beanrepository.method;

import php.core.Types;
import php.core.method.Method;

public class MethodBeginTransaction extends Method{

	public MethodBeginTransaction() {
		super(Public, Types.Void, "beginTransaction");
		setStatic(true);
	}

	@Override
	public void addImplementation() {
		
		/*addInstr(
				((PhpCls) parent).accessStaticAttribute
				(new Attr(Types.mysqli, "sqlCon")).callMethodInstruction(, args);*/
		
	}

}
