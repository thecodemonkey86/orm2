package cpp.beanquery.method;

import cpp.Types;
import cpp.beanquery.ClsBeanQuery;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.instruction.Instruction;

public class MethodLeftJoin1 extends Method{

	public MethodLeftJoin1(ClsBeanQuery parentType) {
		super(Public, parentType.toRef(), "leftJoin");
		addParam(new Param(Types.QString.toConstRef(),"joinTable"));
		addParam(new Param(Types.QString.toConstRef(),"alias"));
		addParam(new Param(Types.QString.toConstRef(),"on"));
		
	}

	@Override
	public void addImplementation() {
		addInstr(new Instruction() {
			@Override
			public String toString() {
				return "this->joinTables.append(QStringLiteral(\" LEFT JOIN %1 %2 ON %3\").arg( joinTable, alias, on));\r\n" + 
						"return *this;";
			}
		});
	}

}
