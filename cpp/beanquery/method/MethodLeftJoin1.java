package cpp.beanquery.method;

import cpp.Types;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.instruction.Instruction;

public class MethodLeftJoin1 extends Method{

	public MethodLeftJoin1(Cls parentType,boolean qLatin1StringParamJoinTable,boolean qLatin1StringParamAlias,boolean qLatin1StringParamOn) {
		super(Public, parentType.toRef(), "leftJoin");
		addParam(new Param(qLatin1StringParamJoinTable ? Types.QLatin1String.toConstRef() : Types.QString.toConstRef(),"joinTable"));
		addParam(new Param(qLatin1StringParamAlias ? Types.QLatin1String.toConstRef() : Types.QString.toConstRef(),"alias"));
		addParam(new Param(qLatin1StringParamOn ? Types.QLatin1String.toConstRef() : Types.QString.toConstRef(),"on"));
		
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
