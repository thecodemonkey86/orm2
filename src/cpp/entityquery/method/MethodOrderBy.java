package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.instruction.Instruction;
import cpp.lib.EnumSqlQueryOrderDirection;

public class MethodOrderBy extends Method{

	Param pOrder;
	Param pDirection;
	
	public MethodOrderBy(Cls parentType) {
		super(Public, parentType.toRef(), "orderBy");
		pOrder = addParam(Types.QString.toConstRef(),"order");
		pDirection = addParam(new Param( Types.OrderDirection,"direction",EnumSqlQueryOrderDirection.ORDER_ASC));
		
	}

	@Override
	public void addImplementation() {
		
        addInstr(new Instruction() {
			@Override
			public String toString() {
				return "this->orderByExpressions.append(QLatin1String(\"%1 %2\").arg(order, (direction == SqlUtil3::SqlQuery::ORDER_ASC ? QLatin1String(\"asc\") : QLatin1String(\"desc\")) ));\r\n" + 
						"return *this;";
			}
		});
	}

}
