package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.instruction.Instruction;
import util.CodeUtil2;

public class MethodAndWhere3 extends Method{

	Param pWhereCond;
	Param pParam1;
	Param pParam2;
	
	public MethodAndWhere3(Cls parentType) {
		super(Public, parentType.toRef(), "andWhere");
		pWhereCond = addParam(Types.QString.toConstRef(),"whereCond");
		pParam1 = addParam(Types.Int,"param1");
		pParam2 = addParam(Types.Int,"param2");
		
	}

	@Override
	public void addImplementation() {
		addInstr(new Instruction() {
			@Override
			public String toString() {
				return "if (!conditions.empty()) {" + CodeUtil2.NEWLINE + 
						"            this->conditions.append(SqlUtil3::SqlQuery::AND);" + CodeUtil2.NEWLINE + 
						"        }" + CodeUtil2.NEWLINE + 
						"        this->params.append(QVariant(param1));" + CodeUtil2.NEWLINE + 
						"        this->params.append(QVariant(param2));" + CodeUtil2.NEWLINE + 
						"        this->conditions.append(whereCond);" + CodeUtil2.NEWLINE + 
						"        return *this;";
			}
		});
	}

}
