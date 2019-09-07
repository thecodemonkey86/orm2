package sunjava.beanrepository.expression;

import sunjava.bean.BeanCls;
import sunjava.beanrepository.ClsBeanRepository;
import sunjava.core.JavaString;
import sunjava.core.expression.Expression;
import sunjava.core.expression.ThisExpression;
import sunjava.core.expression.Var;

public class ThisBeanRepositoryExpression extends ThisExpression{

	public ThisBeanRepositoryExpression(ClsBeanRepository parent) {
		super(parent);
	}

	public Expression callGetByRecordMethod(BeanCls bean, Var record, JavaString alias) {
		return bean.callStaticMethod("getByRecord", accessAttr("sqlCon"), record, alias);
	}
	
//	public MethodCall callGetByRecordMethod(BeanCls bean, Expression...args) {
//		try{
//			return new MethodCall(this, ((Cls)getType()).getMethod("get"+bean.getName()+"ByRecord"),args);
//		} catch (Exception e) {
//			System.out.println(e);
//			throw e;
//		}
//	} 

}
