package cpp.beanquery.method;

import cpp.Types;
import cpp.beanquery.ClsBeanQuery;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.instruction.Instruction;
import cpp.lib.EnumSqlQueryOrderDirection;

public class MethodOrderBy extends Method{

	Param pOrder;
	Param pDirection;
	
	public MethodOrderBy(ClsBeanQuery parentType) {
		super(Public, parentType.toRef(), "orderBy");
		pOrder = addParam(Types.QString.toConstRef(),"order");
		pDirection = addParam(new Param( Types.OrderDirection,"direction",EnumSqlQueryOrderDirection.ORDER_ASC));
		
	}

	@Override
	public void addImplementation() {
		
        addInstr(new Instruction() {
			@Override
			public String toString() {
				return "this->orderByExpressions.append(QStringLiteral(\"%1 %2 \").arg(order, (direction == SqlQuery::ORDER_ASC ? QStringLiteral(\"asc\") : QStringLiteral(\"desc\")) ));\r\n" + 
						"return *this;";
			}
		});
	}

}
