package sunjava.bean;

import sunjava.core.Attr;
import sunjava.core.JavaCls;
import sunjava.core.MethodCall;
import sunjava.core.Types;
import sunjava.core.expression.Expression;

public class RepositoryAttr extends Attr{

	public RepositoryAttr() {
		super(Types.BeanRepository, "repository");
	}

	public MethodCall callGetByRecordMethod(BeanCls bean, Expression...args) {
		try{
			return new MethodCall(this, ((JavaCls)getType()).getMethod("get"+bean.getName()+"ByRecord"),args);
		} catch (Exception e) {
			System.out.println(e);
			throw e;
		}
	} 
}
