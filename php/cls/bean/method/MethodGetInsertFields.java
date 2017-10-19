package php.cls.bean.method;

import generate.CodeUtil2;

import java.util.ArrayList;
import java.util.List;

import model.Column;
import php.Types;
import php.cls.Method;
import php.cls.expression.PhpStringLiteral;
import php.cls.instruction.Instructions;

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
