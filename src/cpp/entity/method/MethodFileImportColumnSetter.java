package cpp.entity.method;

import cpp.CoreTypes;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.core.Param;
import database.column.Column;
import util.StringUtil;

public class MethodFileImportColumnSetter extends Method {

	Param pFileName;
	Attr a;
	
	public MethodFileImportColumnSetter(Attr a,Column col) {
		super(Public, CoreTypes.Void, getMethodName(col));
		pFileName = addParam(new Param(CoreTypes.QString.toConstRef(), col.getCamelCaseName()+"FilePath"));
		this.a = a;
	}

	@Override
	public void addImplementation() {
		_assign(_this().accessAttr(a),pFileName);
	}

	public static String getMethodName(Column col) {
		return "set"+StringUtil.ucfirst(col.getCamelCaseName());
	}

}
