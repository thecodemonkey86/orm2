package sunjava.bean.method;

import java.util.ArrayList;
import java.util.List;

import codegen.CodeUtil;
import database.column.Column;
import database.relation.PrimaryKey;
import sunjava.core.JavaString;
import sunjava.core.Method;
import sunjava.core.Types;

public class MethodGetUpdateCondition extends Method {
	protected List<Column> cols;
	public MethodGetUpdateCondition(PrimaryKey pk) {
		super(Protected, Types.String, "getUpdateCondition");
		this.cols = pk.getColumns();
	}

	@Override
	public void addImplementation() {
		ArrayList<String> colsEscaped = new ArrayList<>();
		for(Column col:cols) {
			colsEscaped.add(col.getEscapedName()+"=?");
		}
		_return(JavaString.stringConstant(CodeUtil.concat(colsEscaped, " AND ")));
	}

}
