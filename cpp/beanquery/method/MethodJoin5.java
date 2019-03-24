package cpp.beanquery.method;

import cpp.Types;
import cpp.beanquery.ClsBeanQuery;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.instruction.Instruction;

public class MethodJoin5 extends Method{

	Param pJoinTableAlias, pOn, pParams;
	
	public MethodJoin5(ClsBeanQuery parentType) {
		super(Public, parentType.toRef(), "join");
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
						"return join(joinTable,alias,on);";
			}
		});
	}

}
