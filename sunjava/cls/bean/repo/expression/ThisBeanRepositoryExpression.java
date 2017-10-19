package sunjava.cls.bean.repo.expression;

import sunjava.cls.JavaString;
import sunjava.cls.bean.BeanCls;
import sunjava.cls.bean.repo.ClsBeanRepository;
import sunjava.cls.expression.Expression;
import sunjava.cls.expression.ThisExpression;
import sunjava.cls.expression.Var;

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
