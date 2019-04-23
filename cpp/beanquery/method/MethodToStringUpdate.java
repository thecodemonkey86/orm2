package cpp.beanquery.method;

import cpp.Types;
import cpp.bean.BeanCls;
import cpp.core.Method;
import cpp.core.instruction.Instruction;

public class MethodToStringUpdate extends Method{

	BeanCls bean;
	
	public MethodToStringUpdate(BeanCls bean) {
		super(Method.Public, Types.QString, "toString");
		setConstQualifier(true);
		this.bean = bean;
	}

	@Override
	public void addImplementation() {
		addInstr(new Instruction() {
			@Override
			public String toString() {
				return "QString query = QStringLiteral(\"UPDATE %1 SET\").arg(table);\r\n" +
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
						"        return query;";
			}
		});
		
	}

}
