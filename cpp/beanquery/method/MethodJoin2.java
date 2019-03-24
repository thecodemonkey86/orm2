package cpp.beanquery.method;

import cpp.Types;
import cpp.beanquery.ClsBeanQuery;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.instruction.Instruction;

public class MethodJoin2 extends Method{

	public MethodJoin2(ClsBeanQuery parentType) {
		super(Public, parentType.toRef(), "join");
		addParam(new Param(Types.QString.toConstRef(),"joinTableAlias"));
		addParam(new Param(Types.QString.toConstRef(),"on"));
		
	}

	@Override
	public void addImplementation() {
		addInstr(new Instruction() {
			@Override
			public String toString() {
				return "this->joinTables.append(QStringLiteral(\" JOIN %1 ON %2\").arg( joinTableAlias,on));\r\n" + 
						"return *this;";
			}
		});
	}

}
