package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.instruction.Instruction;
import cpp.util.ClsDbPool;

public class MethodExecute extends Method{

	protected Param pSqlCon;
	
	public MethodExecute() {
		super(Public, Types.Void, "execute");
		setConstQualifier();
		pSqlCon = addParam(Types.QSqlDatabase.toConstRef(),"sqlCon",ClsDbPool.instance.callStaticMethod(ClsDbPool.getDatabase));
	}

	@Override
	public void addImplementation() {
		addInstr(new Instruction() {
			@Override
			public String toString() {
				return String.format("SqlUtil4::Sql::execute(%s,toString(),params);",pSqlCon.toString());
			}
		});
	}

}
