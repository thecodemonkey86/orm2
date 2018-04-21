package cpp.beanquery.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.instruction.Instruction;

public class MethodPrintDebug extends Method{

	public MethodPrintDebug() {
		super(Method.Public, Types.Void, "printDebug");
		setConstQualifier(true);
	}

	@Override
	public void addImplementation() {
		addInstr(new Instruction() {
			@Override
			public String toString() {
				return "std::cout<<getDebugString().toUtf8().data();";
			}
		});
	}

}
