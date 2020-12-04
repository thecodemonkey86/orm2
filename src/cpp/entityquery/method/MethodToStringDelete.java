package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.instruction.Instruction;
import cpp.entity.EntityCls;

public class MethodToStringDelete extends Method{

	EntityCls bean;
	
	public MethodToStringDelete(EntityCls bean) {
		super(Method.Public, Types.QString, "toString");
		setConstQualifier(true);
		this.bean = bean;
	}

	@Override
	public void addImplementation() {
		addInstr(new Instruction() {
			@Override
			public String toString() {
				return "QString query = QLatin1String(\"DELETE FROM %1\").arg(table);\r\n" +
						"\r\n" + 
						"        if (!conditions.empty()) {\r\n" + 
						"\r\n" + 
				        "        query += QLatin1String(\" WHERE \");\r\n" + 
						"\r\n" + 
						"        for(const QString &cond: conditions) {\r\n" + 
						"             query += cond;\r\n" + 
						"         }\r\n" + 
						"     	 }\r\n" + 
						"\r\n" + 
						"        return query;";
			}
		});
		
	}

}
