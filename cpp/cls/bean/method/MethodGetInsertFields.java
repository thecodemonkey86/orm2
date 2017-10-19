package cpp.cls.bean.method;

import generate.CodeUtil2;

import java.util.ArrayList;
import java.util.List;

import model.Column;
import cpp.Types;
import cpp.cls.Method;
import cpp.cls.QString;
import cpp.cls.instruction.Instructions;

public class MethodGetInsertFields extends Method {

	List<Column> columns;
	
	public MethodGetInsertFields(List<Column> columns) {
		super(Method.Public, Types.QString, "getInsertFields");
		this.columns = columns;
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
