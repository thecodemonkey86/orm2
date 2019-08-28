package php.bean.method;

import java.util.ArrayList;
import java.util.List;

import database.column.Column;
import php.core.Types;
import php.core.expression.PhpStringLiteral;
import php.core.instruction.Instructions;
import php.core.method.Method;
import util.CodeUtil2;

public class MethodGetInsertFields extends Method {

	public static final String METHOD_NAME = "getInsertFields";
	List<Column> columns;
	
	public MethodGetInsertFields(List<Column> columns) {
		super(Method.Public, Types.String, METHOD_NAME);
		this.columns = columns;
	}

	@Override
	public void addImplementation() {
		ArrayList<String> columns=new ArrayList<>(this.columns.size());
		for(Column c:this.columns) {
			columns.add(c.getEscapedName());
		}
		addInstr(Instructions._return(new PhpStringLiteral(CodeUtil2.commaSep(columns))));
		
	}

}
