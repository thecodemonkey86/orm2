package sunjava.cls.bean;

import sunjava.Types;
import sunjava.cls.Attr;
import sunjava.cls.JavaCls;
import sunjava.cls.MethodCall;
import sunjava.cls.expression.Expression;

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
