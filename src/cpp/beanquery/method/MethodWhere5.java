package cpp.beanquery.method;

import cpp.Types;
import cpp.beanquery.ClsBeanQuery;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.instruction.Instruction;

public class MethodWhere5 extends Method{

	Param pWhereCond;
	Param pParams;
	
	public MethodWhere5(ClsBeanQuery parentType) {
		super(Public, parentType.toRef(), "where");
		pWhereCond = addParam(Types.QString.toConstRef(),"whereCond");
		pParams = addParam(Types.QVariantList,"params");
		
	}

	@Override
	public void addImplementation() {
		addInstr(new Instruction() {
			@Override
			public String toString() {
				return "this->params.append(params);\r\n" + 
						"        if (!conditions.empty()) {\r\n" + 
						"            this->conditions.append(SqlQuery::AND);\r\n" + 
						"        }\r\n" + 
						"        this->conditions.append(whereCond);\r\n" + 
						"        return *this;";
			}
		});
	}

}
