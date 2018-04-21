package cpp.beanquery.method;

import cpp.Types;
import cpp.beanquery.ClsBeanQuery;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.instruction.Instruction;

public class MethodLeftJoin6 extends Method{

	public MethodLeftJoin6(ClsBeanQuery parentType) {
		super(Public, parentType.toRef(), "leftJoin");
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
						"return leftJoin(joinTableAlias,on);";
			}
		});
	}

}
