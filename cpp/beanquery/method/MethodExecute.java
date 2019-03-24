package cpp.beanquery.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.instruction.Instruction;

public class MethodExecute extends Method{

	public MethodExecute() {
		super(Public, Types.Void, "execute");
		setConstQualifier();
	}

	@Override
	public void addImplementation() {
		addInstr(new Instruction() {
			@Override
			public String toString() {
				return "this->sqlCon->execute(toString(),params);";
			}
		});
	}

}
