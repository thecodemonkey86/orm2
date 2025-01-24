package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.instruction.Instruction;
import util.CodeUtil2;

public class MethodLeftJoin3 extends Method{

	public MethodLeftJoin3(Cls parentType) {
		super(Public, parentType.toRef(), "leftJoin");
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
				return "this->params.append(param);" + CodeUtil2.NEWLINE + 
						"return leftJoin(joinTable,alias,on);";
			}
		});
	}

}
