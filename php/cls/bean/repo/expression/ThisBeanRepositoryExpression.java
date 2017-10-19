package php.cls.bean.repo.expression;

import php.cls.bean.BeanCls;
import php.cls.bean.repo.ClsBeanRepository;
import php.cls.expression.Expression;

import php.cls.expression.ThisExpression;
import php.cls.expression.Var;

public class ThisBeanRepositoryExpression extends ThisExpression{

	public ThisBeanRepositoryExpression(ClsBeanRepository parent) {
		super(parent);
	}

	public Expression callGetByRecordMethod(BeanCls bean, Var record,  Expression alias) {
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
