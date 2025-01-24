package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.instruction.Instruction;
import cpp.entity.EntityCls;
import util.CodeUtil2;

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
				return "QString query = QStringLiteral(\"DELETE FROM %1\").arg(table);" + CodeUtil2.NEWLINE +
						CodeUtil2.NEWLINE + 
						"        if (!conditions.empty()) {" + CodeUtil2.NEWLINE + 
						CodeUtil2.NEWLINE + 
				        "        query += QStringLiteral(\" WHERE \");" + CodeUtil2.NEWLINE + 
						CodeUtil2.NEWLINE + 
						"        for(const QString &cond: conditions) {" + CodeUtil2.NEWLINE + 
						"             query += cond;" + CodeUtil2.NEWLINE + 
						"         }" + CodeUtil2.NEWLINE + 
						"     	 }" + CodeUtil2.NEWLINE + 
						CodeUtil2.NEWLINE + 
						"        return query;";
			}
		});
		
	}

}
