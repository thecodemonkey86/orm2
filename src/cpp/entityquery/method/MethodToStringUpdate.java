package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.instruction.Instruction;
import cpp.entity.EntityCls;

public class MethodToStringUpdate extends Method{

	EntityCls bean;
	
	public MethodToStringUpdate(EntityCls bean) {
		super(Method.Public, Types.QString, "toString");
		setConstQualifier(true);
		this.bean = bean;
	}

	@Override
	public void addImplementation() {
		addInstr(new Instruction() {
			@Override
			public String toString() {
				return "QString query = QStringLiteral(\"UPDATE %1 SET %2\").arg(table, updateFields.join(QChar(\',\')));\r\n" +
						"\r\n" + 
						"        if (!conditions.empty()) {\r\n" + 
						"\r\n" + 
						"           query += QStringLiteral(\" WHERE \");\r\n" + 
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
