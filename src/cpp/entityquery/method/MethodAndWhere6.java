package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.instruction.Instruction;

public class MethodAndWhere6 extends Method{

	Param pWhereCond;
	Param pParams;
	
	public MethodAndWhere6(Cls parentType) {
		super(Public, parentType.toRef(), "andWhere");
		pWhereCond = addParam(Types.QString.toConstRef(),"whereCond");
		pParams = addParam(Types.QStringList,"params");
		
	}

	@Override
	public void addImplementation() {
		addInstr(new Instruction() {
			@Override
			public String toString() {
				return "for(const auto & p : params)\t\r\n this->params.append(p);\r\n" + 
						"        if (!conditions.empty()) {\r\n" + 
						"            this->conditions.append(SqlUtil4::SqlQuery::AND);\r\n" + 
						"        }\r\n" + 
						"        this->conditions.append(whereCond);\r\n" + 
						"        return *this;";
			}
		});
	}

}
