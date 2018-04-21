package cpp.beanquery.method;

import cpp.Types;
import cpp.beanquery.ClsBeanQuery;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.instruction.Instruction;

public class MethodWhere3 extends Method{

	Param pWhereCond;
	Param pParam1;
	Param pParam2;
	
	public MethodWhere3(ClsBeanQuery parentType) {
		super(Public, parentType.toRef(), "where");
		pWhereCond = addParam(Types.QString.toConstRef(),"whereCond");
		pParam1 = addParam(Types.Int,"param1");
		pParam2 = addParam(Types.Int,"param2");
		
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
						"        this->conditions.append(whereCond);\r\n" + 
						"        return *this;";
			}
		});
	}

}
