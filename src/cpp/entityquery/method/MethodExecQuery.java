package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.instruction.Instruction;
import util.CodeUtil2;

public class MethodExecQuery extends Method{
	Param pRewriteSql;;
	
	public MethodExecQuery() {
		this(false);
	}

	public MethodExecQuery(boolean withRewriteSql) {
		super(Public, Types.QSqlQuery, "execQuery");
		setConstQualifier();
		
		if(withRewriteSql) {
			pRewriteSql = addParam(Types.QString.toConstRef(), "rewriteSql") ;
		}
	}

	@Override
	public void addImplementation() {
		addInstr(new Instruction() {
			@Override
			public String toString() {
				return String.format("QSqlQuery q(sqlCon);" + CodeUtil2.NEWLINE + 
						"        q.setForwardOnly(true);" + CodeUtil2.NEWLINE + 
						"        if (q.prepare(%s)) {" + CodeUtil2.NEWLINE + 
						CodeUtil2.NEWLINE + 
						CodeUtil2.NEWLINE + 
						"            for(int i=0;i<params.size();i++) {" + CodeUtil2.NEWLINE + 
						"                q.addBindValue(params.at(i));" + CodeUtil2.NEWLINE + 
						CodeUtil2.NEWLINE + 
						"            }" + CodeUtil2.NEWLINE + 
						"            if (!q.exec()) {" + CodeUtil2.NEWLINE + 
						"                QString msg=q.lastError().text();" + CodeUtil2.NEWLINE +
						"                throwSqlExceptionWithLine(q.lastError().nativeErrorCode(),!msg.isEmpty() ? msg : sqlCon.driver()->lastError().text(),q.lastQuery());" + CodeUtil2.NEWLINE 	+		
						"            }" + CodeUtil2.NEWLINE + 
						"            return q;" + CodeUtil2.NEWLINE + 
						CodeUtil2.NEWLINE + 
						"        } else {" + CodeUtil2.NEWLINE + 
						"            QString msg=q.lastError().text();" + CodeUtil2.NEWLINE + 
						"            throwSqlExceptionWithLine(q.lastError().nativeErrorCode(),!msg.isEmpty() ? msg : sqlCon.driver()->lastError().text(),q.lastQuery());" + CodeUtil2.NEWLINE 	+
						"        }",pRewriteSql==null?"toString()": pRewriteSql );
			}
		});
	}

}
