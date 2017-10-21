package cpp.bean;

import cpp.Types;
import cpp.core.Attr;
import cpp.core.Cls;
import cpp.core.MethodCall;
import cpp.core.expression.Expression;

public class RepositoryAttr extends Attr{

	public RepositoryAttr() {
		super(Types.BeanRepository.toSharedPtr(), "repository");
	}

	public MethodCall callGetByRecordMethod(BeanCls bean, Expression...args) {
		try{
			return new MethodCall(this, ((Cls)getType()).getMethod("get"+bean.getName()+"ByRecord"),args);
		} catch (Exception e) {
			System.out.println(e);
			throw e;
		}
	} 
}
