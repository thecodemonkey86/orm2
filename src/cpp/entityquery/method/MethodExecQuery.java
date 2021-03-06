package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.instruction.Instruction;
import cpp.util.ClsDbPool;

public class MethodExecQuery extends Method{
	Param pRewriteSql;;
	Param pSqlCon;
	public MethodExecQuery() {
		this(false);
	}
	
	public static String getMethodName() {
		return "execQuery";
	}

	public MethodExecQuery(boolean withRewriteSql) {
		super(Public, Types.QSqlQuery, getMethodName());
		setConstQualifier();
		
		if(withRewriteSql) {
			pRewriteSql = addParam(Types.QString.toConstRef(), "rewriteSql") ;
		}
		pSqlCon = addParam(Types.QSqlDatabase.toConstRef(),"sqlCon",ClsDbPool.instance.callStaticMethod(ClsDbPool.getDatabase));
	}

	@Override
	public void addImplementation() {
		addInstr(new Instruction() {
			@Override
			public String toString() {
				return String.format("QSqlQuery q(sqlCon);\r\n" + 
						"        q.setForwardOnly(true);\r\n" + 
						"        if (q.prepare(%s)) {\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"            for(int i=0;i<params.size();i++) {\r\n" + 
						"                q.addBindValue(params.at(i));\r\n" + 
						"\r\n" + 
						"            }\r\n" + 
						"            if (!q.exec()) {\r\n" + 
						"                QString msg=q.lastError().text();\r\n" +
						"				 #ifdef QT_DEBUG\r\n	"+
						"                qDebug()<<msg;\r\n" + 
						"                qDebug()<<q.driver()->lastError().text();\r\n"+ 
						"				 #endif	\r\n" + 
						"                throw SqlUtil4::SqlException(sqlCon.lastError().nativeErrorCode(), sqlCon.driver()->lastError().text(),toString());\r\n" + 
						"            }\r\n" + 
						"            return q;\r\n" + 
						"\r\n" + 
						"        } else {\r\n" + 
						"            QString msg=q.lastError().text();\r\n" + 
						"            #ifdef QT_DEBUG\r\nqDebug()<<msg;\r\n"
						+ "			 #endif\r\n" + 
						"            throw SqlUtil4::SqlException(sqlCon.lastError().nativeErrorCode(), sqlCon.driver()->lastError().text(),toString());\r\n" + 
						"        }",pRewriteSql==null?"toString()": pRewriteSql );
			}
		});
	}

}
