package cpp.beanquery.method;

import cpp.Types;
import cpp.bean.BeanCls;
import cpp.core.Method;
import cpp.core.instruction.Instruction;

public class MethodToString extends Method{

	BeanCls bean;
	
	public MethodToString(BeanCls bean) {
		super(Method.Public, Types.QString, "toString");
		setConstQualifier(true);
		this.bean = bean;
	}

	@Override
	public void addImplementation() {
		addInstr(new Instruction() {
			@Override
			public String toString() {
				return "QString query;\r\n" + 
						"        if (!selectFields.isEmpty()) {\r\n" + 
						"            query += QStringLiteral(\"SELECT %1 FROM %2\").arg(selectFields, fromTable);\r\n" + 
						"        } else if (!deleteFromTable.isEmpty()) {\r\n" + 
						"             query+= QStringLiteral(\"DELETE FROM %1\").arg(deleteFromTable);\r\n" + 
						"        }\r\n" + 
						"\r\n" + 
						"        for(int i=0;i<joinTables.size();i++) {\r\n" + 
						"            query+=joinTables.at(i);\r\n" + 
						"        }\r\n" + 
						"\r\n" + 
						"        if (!conditions.empty()) {\r\n" + 
						"\r\n" + 
						"            if (limitResults > 0 || resultOffset > -1) {\r\n" + 
						"                query += QStringLiteral(\" WHERE (\");\r\n" + 
						"            } else {\r\n" + 
						"                query += QStringLiteral(\" WHERE \");\r\n" + 
						"            }\r\n" + 
						"\r\n" + 
						"            for(const QString &cond: conditions) {\r\n" + 
						"                query += cond;\r\n" + 
						"            }\r\n" + 
						"        }\r\n" + 
						"\r\n" + 
						"        if (group.size()>0) {\r\n" + 
						"            query += QStringLiteral(\" GROUP BY %1\").arg(group.at(0));\r\n" + 
						"            for(int i=1;i<group.size();i++) {\r\n" + 
						"                query += QStringLiteral(\", %1\").arg(group.at(i));\r\n" + 
						"            }\r\n" + 
						"        }\r\n" + 
						"        if (limitResults > 0 || resultOffset > -1) {\r\n" + 
						"            if (!conditions.empty()) {\r\n" + 
						"                query += QStringLiteral(\") AND %1\").arg("+  MethodToString.this.bean.getName() +"::getLimitQueryString(limitResults,resultOffset,limitOffsetCondition));\r\n" + 
						"            } else {\r\n" + 
						"                query += QStringLiteral(\" WHERE %1\").arg("+ MethodToString.this.bean.	getName() +"::getLimitQueryString(limitResults,resultOffset,limitOffsetCondition));\r\n" + 
						"            }\r\n" + 
						"            if (!limitOffsetOrderBy.isEmpty()) {\r\n" + 
						"                query += QStringLiteral(\" ORDER BY %1\").arg(limitOffsetOrderBy);\r\n" + 
						"            }\r\n" + 
						"        }\r\n" + 
						"        if (!selectFields.isEmpty()) {\r\n" + 
						"            query += QStringLiteral(\" ORDER BY \");\r\n" + 
						"            for(auto order : this->orderByExpressions) {\r\n" + 
						"                query += QStringLiteral(\"%1,\").arg(order);\r\n" + 
						"            }\r\n" + 
						"            query += this->orderByPrimaryKey();\r\n" + 
						"        }\r\n" + 
						"        return query;";
			}
		});
		
	}

}
