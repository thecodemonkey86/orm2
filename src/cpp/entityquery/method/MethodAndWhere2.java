package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.instruction.Instruction;

public class MethodAndWhere2 extends Method{

	Param pWhereCond;
	Param pParam;
	
	public MethodAndWhere2(Cls parentType) {
		super(Public, parentType.toRef(), "andWhere");
		pWhereCond = addParam(Types.QString.toConstRef(),"whereCond");
		pParam = addParam(Types.Int,"param");
		
	}

	@Override
	public void addImplementation() {
		addInstr(new Instruction() {
			@Override
			public String toString() {
				return "if (!conditions.empty()) {\r\n" + 
						"            this->conditions.append(SqlUtil3::SqlQuery::AND);\r\n" + 
						"        }\r\n" + 
						"        this->conditions.append(whereCond);\r\n" + 
						"        this->params.append(param);\r\n" + 
						"        return *this;";
			}
		});
	}

}
