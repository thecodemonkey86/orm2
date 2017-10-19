package cpp.cls.bean.method;

import java.util.ArrayList;
import java.util.List;

import codegen.CodeUtil;
import model.Column;
import model.PrimaryKey;
import cpp.Types;
import cpp.cls.Method;
import cpp.cls.QString;

public class MethodGetUpdateCondition extends Method {
	protected List<Column> cols;
	public MethodGetUpdateCondition(PrimaryKey pk) {
		super(Public, Types.QString, "getUpdateCondition");
		this.cols = pk.getColumns();
	}

	@Override
	public void addImplementation() {
		ArrayList<String> colsEscaped = new ArrayList<>();
		for(Column col:cols) {
			colsEscaped.add(col.getEscapedName()+"=?");
		}
		_return(QString.fromStringConstant(CodeUtil.concat(colsEscaped, " AND ")));
	}

}
