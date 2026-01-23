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

public class MethodGetUpsertPlaceholders extends Method {

	Table table;
	
	public MethodGetUpsertPlaceholders(Table table) {
		super(Method.Public,Types.QString, "getUpsertPlaceholders");
		this.table = table;
		setStatic(true);
	}

	@Override
	public void addImplementation() {
		EntityCls bean = (EntityCls) this.parent;
		List<Column> columns = this.table.getColumnsWithoutPrimaryKey();
		boolean hasColumnWithRawValueEnabled = table.hasColumnWithRawValueEnabled();
		boolean hasColumnWithFileStreamEnabled = table.hasColumnWithFileStreamEnabled();
		if(hasColumnWithFileStreamEnabled && !EntityCls.getDatabase().supportsLoadingFiles()) {
			throw new RuntimeException("not implemented file stream without database native import function"); 
		}
		ArrayList<String> placeholders = new ArrayList<>();
		ArrayList<Expression> placeholderExpressions = new ArrayList<>();
		
		for(Column c : columns) {
			if(c.isRawValueEnabled()) {
				placeholderExpressions.add(bean.getAttrByName("insertExpression"+c.getUc1stCamelCaseName()));
				placeholders.add(c.getEscapedName()+ "=%"+placeholderExpressions.size());
			} else if(c.isFileImportEnabled()){
				placeholders.add(c.getEscapedName()+ "="+EntityCls.getDatabase().getFileLoadFunction()+CodeUtil2.parentheses("?") );
			} else {
				placeholders.add(c.getEscapedName()+ "=?" );
			}
			
		}
		if(hasColumnWithRawValueEnabled) {
			_return(QString.fromStringConstant(CodeUtil2.commaSep(placeholders)).callMethod(ClsQString.arg, placeholderExpressions));
		} else {
			_return(QString.fromStringConstant(CodeUtil2.commaSep(placeholders)));
		}
		
	}

}
