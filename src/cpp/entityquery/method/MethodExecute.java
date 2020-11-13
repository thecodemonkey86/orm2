package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.instruction.Instruction;
import cpp.util.ClsDbPool;

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
				return String.format("SqlUtil3::Sql::execute(%s,toString(),params);",ClsDbPool.instance.callStaticMethod(ClsDbPool.getDatabase).toString());
			}
		});
	}

}
