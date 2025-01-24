package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.instruction.Instruction;
import util.CodeUtil2;

public class MethodJoin4 extends Method{

	public MethodJoin4(Cls parentType) {
		super(Public, parentType.toRef(), "join");
		addParam(new Param(Types.QString.toConstRef(),"joinTableAlias"));
		addParam(new Param(Types.QString.toConstRef(),"on"));
		addParam(Types.QVariant.toConstRef(),"param");
	}

	@Override
	public void addImplementation() {
		addInstr(new Instruction() {
			@Override
			public String toString() {
				return "this->params.append(param);" + CodeUtil2.NEWLINE + 
						"return join(joinTableAlias,on);";
			}
		});
	}

}
