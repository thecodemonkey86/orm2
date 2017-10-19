package cpp.cls.bean;

import cpp.Types;
import cpp.cls.Attr;
import cpp.cls.Cls;
import cpp.cls.MethodCall;
import cpp.cls.expression.Expression;

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
