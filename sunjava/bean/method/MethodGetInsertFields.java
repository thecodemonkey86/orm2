package sunjava.bean.method;

import generate.CodeUtil2;

import java.util.ArrayList;
import java.util.List;

import database.column.Column;
import sunjava.core.JavaString;
import sunjava.core.Method;
import sunjava.core.Types;
import sunjava.core.instruction.Instructions;

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
		addInstr(Instructions._return(JavaString.stringConstant(CodeUtil2.commaSep(columns))));
		
	}

}
