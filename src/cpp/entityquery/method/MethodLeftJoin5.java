package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.instruction.Instruction;

public class MethodLeftJoin5 extends Method{

	Param pJoinTableAlias, pOn, pParams;
	
	public MethodLeftJoin5(Cls parentType) {
		super(Public, parentType.toRef(), "leftJoin");
		addParam(new Param(Types.QString.toConstRef(),"joinTable"));
		addParam(new Param(Types.QString.toConstRef(),"alias"));
		addParam(new Param(Types.QString.toConstRef(),"on"));
		addParam(Types.QVariantList.toConstRef(),"params");
	}

	@Override
	public void addImplementation() {
		addInstr(new Instruction() {
			@Override
			public String toString() {
				return "this->params.append(params);\r\n" + 
						"return leftJoin(joinTable,alias,on);";
			}
		});
	}

}
