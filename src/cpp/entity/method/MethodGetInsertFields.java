package cpp.entity.method;

import java.util.ArrayList;
import java.util.List;

import cpp.Types;
import cpp.core.Method;
import cpp.core.QString;
import cpp.core.instruction.Instructions;
import database.column.Column;
import util.CodeUtil2;

public class MethodGetInsertFields extends Method {

	List<Column> columns;
	
	public MethodGetInsertFields(List<Column> columns) {
		super(Method.Public, Types.QString, "getInsertFields");
		this.columns = columns;
		setStatic(true);
	}

	@Override
	public void addImplementation() {
		ArrayList<String> columns=new ArrayList<>(this.columns.size());
		for(Column c:this.columns) {
			columns.add(c.getEscapedName());
		}
		addInstr(Instructions._return(QString.fromStringConstant(CodeUtil2.commaSep(columns))));
		
	}

}
