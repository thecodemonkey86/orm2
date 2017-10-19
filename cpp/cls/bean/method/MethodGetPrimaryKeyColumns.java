package cpp.cls.bean.method;

import cpp.Types;
import cpp.cls.Method;
import cpp.cls.QString;
import cpp.cls.expression.QStringInitList;
import model.Column;
import model.PrimaryKey;

public class MethodGetPrimaryKeyColumns extends Method {
	PrimaryKey pk;
	
	public MethodGetPrimaryKeyColumns(PrimaryKey pk) {
		super(Public, Types.QStringList, "getPrimaryKeyColumns");
		this.pk = pk;
		setStatic(true);
	}

	@Override
	public void addImplementation() {
		QStringInitList init = new QStringInitList();
		for(Column col : pk.getColumns()) {
			init.addString(QString.fromStringConstant(col.getEscapedName()));
		}
		
		_return(_declare(returnType, "pkCols", init));
	}
	
	
}
