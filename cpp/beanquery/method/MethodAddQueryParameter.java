package cpp.beanquery.method;

import cpp.CoreTypes;
import cpp.beanquery.ClsBeanQuery;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.Type;

public class MethodAddQueryParameter extends Method {

	Param pParam;
	
	public MethodAddQueryParameter(Type paramType) {
		super(Public, CoreTypes.Void, "addQueryParameter");
		pParam = addParam(new Param(paramType.isPrimitiveType() ? paramType : paramType.toConstRef() , "param"));
	}

	@Override
	public void addImplementation() {
		addInstr(_this().accessAttr(ClsBeanQuery.params).binOp("+=", pParam).asInstruction());

	}

}
