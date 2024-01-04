package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.instruction.Instruction;

public class MethodWhere8 extends Method{

	Param pWhereCond;
	Param pParam;
	
	public MethodWhere8(Cls parentType,boolean qLatin1String) {
		super(Public, parentType.toRef(), "where");
		pWhereCond = addParam(Types.QString.toConstRef(),"whereCond");
		pParam = addParam(qLatin1String ? Types.QString.toConstRef() : Types.QLatin1String.toConstRef(),"param");
		
	}

	@Override
	public void addImplementation() {
		addInstr(new Instruction() {
			@Override
			public String toString() {
				return "if (!conditions.empty()) {\r\n" + 
						"            this->conditions.append(SqlUtil4::SqlQuery::AND);\r\n" + 
						"        }\r\n" + 
						"        this->conditions.append(whereCond);\r\n" + 
						"        this->params.append(param);\r\n" + 
						"        return *this;";
			}
		});
	}

}
