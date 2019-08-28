package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.instruction.Instruction;
import cpp.entityquery.EntityQueryType;

public class MethodOffset extends Method{

	Param pJoinTableAlias, pOn;
	EntityQueryType beanQueryType;
	
	public MethodOffset(Cls parentType, EntityQueryType beanQueryType) {
		super(Public, parentType.toRef(), "offset");
		addParam(Types.Int64, "offset");
		if(beanQueryType == EntityQueryType.Select) {
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
				if(beanQueryType == EntityQueryType.Select) {
					return "this->limitResults = -1;\r\n" + 
							"        this->resultOffset = offset;\r\n" + 
							"        this->limitOffsetCondition = condition;\r\n" + 
							"        this->limitOffsetOrderBy = orderBy;\r\n" + 
							"        return *this;";
				} else {
					return "this->limitResults = -1;\r\n" + 
							"        this->resultOffset = offset;\r\n" + 
							"        return *this;";
				}
				
			}
		});
	}

}
