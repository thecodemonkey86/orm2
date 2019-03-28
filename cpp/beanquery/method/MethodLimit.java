package cpp.beanquery.method;

import cpp.Types;
import cpp.beanquery.ClsBeanQuery;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.instruction.Instruction;

public class MethodLimit extends Method{

	Param pJoinTableAlias, pOn;
		
	public MethodLimit(ClsBeanQuery parentType) {
		super(Public, parentType.toRef(), "limit");
		addParam(Types.Int64, "limit");
		addParam(Types.QString.toConstRef(),"condition");
		addParam(new Param(Types.QString.toConstRef(),"orderBy", new QString()));
		
	}

	@Override
	public void addImplementation() {
		addInstr(new Instruction() {
			@Override
			public String toString() {
				return "this->limitResults = limit;\r\n" + 
						"        this->resultOffset = -1;\r\n" + 
						"        this->limitOffsetCondition = condition;\r\n" + 
						"        this->limitOffsetOrderBy = orderBy;\r\n" + 
						"        return *this;";
			}
		});
	}

}
