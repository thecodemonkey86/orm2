package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.instruction.Instruction;

public class MethodJoin3 extends Method{

	public MethodJoin3(Cls parentType) {
		super(Public, parentType.toRef(), "join");
	    addParam(Types.QString.toConstRef(),"joinTable");
		addParam(Types.QString.toConstRef(),"alias");
		addParam(Types.QString.toConstRef(),"on");
		addParam(Types.QVariant.toConstRef(),"param");
		
	}

	@Override
	public void addImplementation() {
		addInstr(new Instruction() {
			@Override
			public String toString() {
				return "this->params.append(param);\r\n" + 
						"return join(joinTable,alias,on);";
			}
		});
	}

}
