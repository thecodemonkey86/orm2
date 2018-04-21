package cpp.beanquery.method;

import cpp.Types;
import cpp.beanquery.ClsBeanQuery;
import cpp.core.Method;
import cpp.core.instruction.Instruction;

public class MethodLeftJoin3 extends Method{

	public MethodLeftJoin3(ClsBeanQuery parentType) {
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
				return "this->params.append(param);\r\n" + 
						"return leftJoin(joinTable,alias,on);";
			}
		});
	}

}
