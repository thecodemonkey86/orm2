package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.instruction.Instruction;
import util.CodeUtil2;

public class MethodAndWhere6 extends Method{

	Param pWhereCond;
	Param pParams;
	
	public MethodAndWhere6(Cls parentType) {
		super(Public, parentType.toRef(), "andWhere");
		pWhereCond = addParam(Types.QString.toConstRef(),"whereCond");
		pParams = addParam(Types.QStringList.toConstRef(),"params");
		
	}

	@Override
	public void addImplementation() {
		addInstr(new Instruction() {
			@Override
			public String toString() {
				return "for(const auto & p : params)\t" + CodeUtil2.NEWLINE + " this->params.append(p);" + CodeUtil2.NEWLINE + 
						"        if (!conditions.empty()) {" + CodeUtil2.NEWLINE + 
						"            this->conditions.append(SqlUtil3::SqlQuery::AND);" + CodeUtil2.NEWLINE + 
						"        }" + CodeUtil2.NEWLINE + 
						"        this->conditions.append(whereCond);" + CodeUtil2.NEWLINE + 
						"        return *this;";
			}
		});
	}

}
