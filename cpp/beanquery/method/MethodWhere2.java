package cpp.beanquery.method;

import cpp.Types;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.instruction.Instruction;

public class MethodWhere2 extends Method{

	Param pWhereCond;
	Param pParam;
	
	public MethodWhere2(Cls parentType) {
		super(Public, parentType.toRef(), "where");
		pWhereCond = addParam(Types.QString.toConstRef(),"whereCond");
		pParam = addParam(Types.Int,"param");
		
	}

	@Override
	public void addImplementation() {
		addInstr(new Instruction() {
			@Override
			public String toString() {
				return " if (!conditions.empty()) {\r\n" + 
						"            this->conditions.append(SqlQuery::AND);\r\n" + 
						"        }\r\n" + 
						"        this->conditions.append(whereCond);\r\n" + 
						"        this->params.append(param);\r\n" + 
						"        return *this;";
			}
		});
	}

}
