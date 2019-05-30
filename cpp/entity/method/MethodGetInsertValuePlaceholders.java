package cpp.entity.method;

import java.util.ArrayList;
import java.util.List;

import cpp.Types;
import cpp.core.Method;
import cpp.core.QString;
import cpp.core.expression.Expression;
import cpp.entity.EntityCls;
import cpp.lib.ClsQString;
import database.column.Column;
import database.table.Table;
import util.CodeUtil2;

public class MethodGetInsertValuePlaceholders extends Method {

	Table table;
	
	public MethodGetInsertValuePlaceholders(Table table) {
		super(Method.Public,Types.QString, "getInsertValuePlaceholders");
		this.table = table;
	}

	@Override
	public void addImplementation() {
		EntityCls bean = (EntityCls) this.parent;
		List<Column> columns = this.table.getColumns(!this.table.getPrimaryKey().isAutoIncrement());
		if(!table.hasColumnWithRawValueEnabled()) {
			_return(QString.fromStringConstant(CodeUtil2.strMultiply("?", ",", columns.size())));
		} else {
			ArrayList<String> placeholders = new ArrayList<>();
			ArrayList<Expression> placeholderExpressions = new ArrayList<>();
			for(Column c : columns) {
				if(c.isRawValueEnabled()) {
					placeholderExpressions.add(bean.getAttrByName("insertExpression"+c.getUc1stCamelCaseName()));
					placeholders.add("%"+placeholderExpressions.size());
				} else {
					placeholders.add("?" );
				}
				
			}
			_return(QString.fromStringConstant(CodeUtil2.commaSep(placeholders)).callMethod(ClsQString.arg, placeholderExpressions));
		}
		
		
	}

}
