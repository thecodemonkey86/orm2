package php.entity.method;

import java.util.ArrayList;
import java.util.List;

import codegen.CodeUtil;
import database.column.Column;
import database.relation.PrimaryKey;
import php.core.Types;
import php.core.expression.PhpStringLiteral;
import php.core.method.Method;


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
		_return(new PhpStringLiteral(CodeUtil.concat(colsEscaped, " AND ")));
	}

}
