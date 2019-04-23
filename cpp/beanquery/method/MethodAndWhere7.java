package cpp.beanquery.method;

import cpp.Types;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.instruction.Instruction;

public class MethodAndWhere7 extends Method{

	Param pWhereCond;
	Param pParams;
	
	public MethodAndWhere7(Cls parentType) {
		super(Public, parentType.toRef(), "andWhere");
		pWhereCond = addParam(Types.QString.toConstRef(),"whereCond");
		pParams = addParam(Types.qvector(Types.QString),"params");
		
	}

	@Override
	public void addImplementation() {
		addInstr(new Instruction() {
			@Override
			public String toString() {
				return "for(const QString & s : params)\r\n" + 
						"            this->params.append(s);\r\n" + 
						"        if (!conditions.empty()) {\r\n" + 
						"            this->conditions.append(SqlQuery::AND);\r\n" + 
						"        }\r\n" + 
						"        this->conditions.append(whereCond);\r\n" + 
						"        return *this;";
			}
		});
	}

}
