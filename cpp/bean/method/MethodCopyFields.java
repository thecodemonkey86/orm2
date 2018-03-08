package cpp.bean.method;

import cpp.CoreTypes;
import cpp.Types;
import cpp.bean.BeanCls;
import cpp.core.Method;
import cpp.core.Param;
import database.column.Column;

public class MethodCopyFields extends Method{

	Param pSrc,pDest,pExclude;
	
	public MethodCopyFields(BeanCls bean) {
		super(Public, CoreTypes.Void, "copyFields");
		pSrc = addParam(new Param(bean.toSharedPtr().toConstRef(), "src"));
		pDest = addParam(new Param(bean.toSharedPtr().toConstRef(), "dest"));
		pExclude = addParam(new Param(Types.qset(Types.QString).toConstRef(), "exclude"));
	}

	@Override
	public void addImplementation() {
		BeanCls bean = (BeanCls) parent;
		for(Column col : bean.getTbl().getColumnsWithoutPrimaryKey()) {
			
		}
	}

}
