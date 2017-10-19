package sunjava.cls.bean.method;

import java.util.ArrayList;
import java.util.List;

import codegen.CodeUtil;
import model.Column;
import model.PrimaryKey;
import sunjava.Types;
import sunjava.cls.JavaString;
import sunjava.cls.Method;

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
