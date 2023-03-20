package php.entity;

import php.core.Attr;
import php.core.PhpCls;
import php.core.Types;
import php.core.expression.Expression;
import php.core.expression.MethodCall;

public class RepositoryAttr extends Attr{

	public RepositoryAttr() {
		super(Types.EntityRepository, "repository");
	}

	public MethodCall callGetByRecordMethod(EntityCls bean, Expression...args) {
		try{
			return new MethodCall(this, ((PhpCls)getType()).getMethod("get"+bean.getName()+"ByRecord"),args);
		} catch (Exception e) {
			System.out.println(e);
			throw e;
		}
	} 
}
