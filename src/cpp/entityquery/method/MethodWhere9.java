package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.instruction.Instruction;
import util.CodeUtil2;

public class MethodWhere9 extends Method{

	Param pWhereCond;
	Param pParam;
	
	public MethodWhere9(Cls parentType) {
		super(Public, parentType.toRef(), "where");
		pWhereCond = addParam(Types.QString.toConstRef(),"whereCond");
		pParam = addParam(Types.QVariant.toConstRef(),"param");
		
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
						"        this->params.append(param);" + CodeUtil2.NEWLINE + 
						"        return *this;";
			}
		});
	}

}
