package php.cls.bean;

import php.Types;
import php.cls.Attr;
import php.cls.PhpCls;
import php.cls.MethodCall;
import php.cls.expression.Expression;

public class RepositoryAttr extends Attr{

	public RepositoryAttr() {
		super(Types.BeanRepository, "repository");
	}

	public MethodCall callGetByRecordMethod(BeanCls bean, Expression...args) {
		try{
			return new MethodCall(this, ((PhpCls)getType()).getMethod("get"+bean.getName()+"ByRecord"),args);
		} catch (Exception e) {
			System.out.println(e);
			throw e;
		}
	} 
}
