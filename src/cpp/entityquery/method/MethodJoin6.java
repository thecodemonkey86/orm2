package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.instruction.Instruction;

public class MethodJoin6 extends Method{

	public MethodJoin6(Cls parentType) {
		super(Public, parentType.toRef(), "join");
		addParam(new Param(Types.QString.toConstRef(),"joinTableAlias"));
		addParam(new Param(Types.QString.toConstRef(),"on"));
		addParam(Types.QVariantList.toConstRef(),"params");
	}

	@Override
	public void addImplementation() {
        addInstr(new Instruction() {
			@Override
			public String toString() {
				return "this->params.append(params);\r\n" + 
						"return join(joinTableAlias,on);";
			}
		});
	}

}
