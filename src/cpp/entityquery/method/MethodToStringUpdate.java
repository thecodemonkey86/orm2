package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.instruction.Instruction;
import cpp.entity.EntityCls;
import util.CodeUtil2;

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
				return "QString query = QStringLiteral(\"UPDATE %1 SET %2\").arg(table, updateFields.join(QChar(\',\')));" + CodeUtil2.NEWLINE +
						CodeUtil2.NEWLINE + 
						"        if (!conditions.empty()) {" + CodeUtil2.NEWLINE + 
						CodeUtil2.NEWLINE + 
						"           query += QStringLiteral(\" WHERE \");" + CodeUtil2.NEWLINE + 
						CodeUtil2.NEWLINE + 
						"            for(const QString &cond: conditions) {" + CodeUtil2.NEWLINE + 
						"                query += cond;" + CodeUtil2.NEWLINE + 
						"            }" + CodeUtil2.NEWLINE + 
						"        }" + CodeUtil2.NEWLINE + 
						CodeUtil2.NEWLINE + 
						"        return query;";
			}
		});
		
	}

}
