package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.instruction.Instruction;
import cpp.entityquery.EntityQueryType;
import util.CodeUtil2;

public class MethodLimit extends Method{

	Param pJoinTableAlias, pOn;
	EntityQueryType beanQueryType;
	
	public MethodLimit(Cls parentType,EntityQueryType beanQueryType) {
		super(Public, parentType.toRef(), "limit");
		addParam(Types.Int64, "limit");
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
					return "this->limitResults = limit;" + CodeUtil2.NEWLINE + 
							"        this->resultOffset = 0;" + CodeUtil2.NEWLINE + 
							"        this->limitOffsetCondition = condition;" + CodeUtil2.NEWLINE + 
							"        this->limitOffsetOrderBy = orderBy;" + CodeUtil2.NEWLINE + 
							"        return *this;";
				} else {
					return "this->limitResults = limit;" + CodeUtil2.NEWLINE + 
							"        this->resultOffset = 0;" + CodeUtil2.NEWLINE + 
							"        return *this;";
				}
				
			}
		});
	}

}
