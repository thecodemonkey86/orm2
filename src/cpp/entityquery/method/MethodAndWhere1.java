package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.instruction.Instruction;
import util.CodeUtil2;

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
				return "if (!conditions.empty()) {" + CodeUtil2.NEWLINE + 
						"            this->conditions.append(SqlUtil3::SqlQuery::AND);" + CodeUtil2.NEWLINE + 
						"        }" + CodeUtil2.NEWLINE + 
						"        this->conditions.append(whereCond);" + CodeUtil2.NEWLINE + 
						"        return *this;";
			}
		});
	}

}
