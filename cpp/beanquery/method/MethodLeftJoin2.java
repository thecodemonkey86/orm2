package cpp.beanquery.method;

import cpp.Types;
import cpp.beanquery.ClsBeanQuery;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.instruction.Instruction;

public class MethodLeftJoin2 extends Method{

	public MethodLeftJoin2(ClsBeanQuery parentType) {
		super(Public, parentType.toRef(), "leftJoin");
		addParam(new Param(Types.QString.toConstRef(),"joinTableAlias"));
		addParam(new Param(Types.QString.toConstRef(),"on"));
		
	}

	@Override
	public void addImplementation() {
		addInstr(new Instruction() {
			@Override
			public String toString() {
				return "this->joinTables.append(QStringLiteral(\" LEFT JOIN %1 ON %2\").arg( joinTableAlias,on));\r\n" + 
						"return *this;";
			}
		});
	}

}
