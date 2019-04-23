package cpp.beanquery.method;

import cpp.Types;
import cpp.beanquery.BeanQueryType;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.instruction.Instruction;

public class MethodLimit extends Method{

	Param pJoinTableAlias, pOn;
	BeanQueryType beanQueryType;
	
	public MethodLimit(Cls parentType,BeanQueryType beanQueryType) {
		super(Public, parentType.toRef(), "limit");
		addParam(Types.Int64, "limit");
		if(beanQueryType == BeanQueryType.Select) {
			addParam(Types.QString.toConstRef(),"condition");
			addParam(new Param(Types.QString.toConstRef(),"orderBy", new QString()));
		}
		this.beanQueryType = beanQueryType;
		
	}

	@Override
	public void addImplementation() {
		addInstr(new Instruction() {
			@Override
			public String toString() {
				if(beanQueryType == BeanQueryType.Select) {
					return "this->limitResults = limit;\r\n" + 
							"        this->resultOffset = -1;\r\n" + 
							"        this->limitOffsetCondition = condition;\r\n" + 
							"        this->limitOffsetOrderBy = orderBy;\r\n" + 
							"        return *this;";
				} else {
					return "this->limitResults = limit;\r\n" + 
							"        this->resultOffset = -1;\r\n" + 
							"        return *this;";
				}
				
			}
		});
	}

}
