package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.instruction.Instruction;

public class MethodAndWhere1 extends Method{

	public MethodAndWhere1(Cls parentType) {
		super(Public, parentType.toRef(), "andWhere");
		addParam(Types.QString.toConstRef(),"whereCond");
		
		
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
						"        return *this;";
			}
		});
	}

}