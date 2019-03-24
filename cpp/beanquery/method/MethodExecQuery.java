package cpp.beanquery.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.instruction.Instruction;

public class MethodExecQuery extends Method{

	public MethodExecQuery() {
		super(Public, Types.QSqlQuery, "execQuery");
		setConstQualifier();
	}

	@Override
	public void addImplementation() {
		addInstr(new Instruction() {
			@Override
			public String toString() {
				return "QSqlQuery q(sqlCon->getCon());\r\n" + 
						"        q.setForwardOnly(true);\r\n" + 
						"        if (q.prepare(toString())) {\r\n" + 
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
						"                throw SqlException(sqlCon->getErrorNr(),q.driver()->lastError().text(), toString());\r\n" + 
						"            }\r\n" + 
						"            return q;\r\n" + 
						"\r\n" + 
						"        } else {\r\n" + 
						"            QString msg=q.lastError().text();\r\n" + 
						"            #ifdef QT_DEBUG\r\nqDebug()<<msg;\r\n"
						+ "			 #endif\r\n" + 
						"            throw SqlException(sqlCon->getErrorNr(), q.driver()->lastError().text(),toString());\r\n" + 
						"        }";
			}
		});
	}

}
