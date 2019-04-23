package cpp.beanquery.method;

import cpp.Types;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.instruction.Instruction;

public class MethodAndWhere4 extends Method{

	Param pWhereCond;
	Param pParam1;
	Param pParam2;
	Param pParam3;
	
	public MethodAndWhere4(Cls parentType) {
		super(Public, parentType.toRef(), "andWhere");
		pWhereCond = addParam(Types.QString.toConstRef(),"whereCond");
		pParam1 = addParam(Types.Int,"param1");
		pParam2 = addParam(Types.Int,"param2");
		pParam3 = addParam(Types.Int,"param3");
		
	}

	@Override
	public void addImplementation() {
		addInstr(new Instruction() {
			@Override
			public String toString() {
				return "if (!conditions.empty()) {\r\n" + 
						"            this->conditions.append(SqlQuery::AND);\r\n" + 
						"        }\r\n" + 
						"        this->params.append(QVariant(param1));\r\n" + 
						"        this->params.append(QVariant(param2));\r\n" + 
						"        this->params.append(QVariant(param3));\r\n" + 
						"        this->conditions.append(whereCond);\r\n" + 
						"        return *this;";
			}
		});
	}

}
