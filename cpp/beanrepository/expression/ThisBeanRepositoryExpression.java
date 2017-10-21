package cpp.beanrepository.expression;

import cpp.bean.BeanCls;
import cpp.beanrepository.ClsBeanRepository;
import cpp.beanrepository.method.MethodGetFromRecord;
import cpp.core.QString;
import cpp.core.expression.Expression;
import cpp.core.expression.ThisExpression;
import cpp.core.expression.Var;

public class ThisBeanRepositoryExpression extends ThisExpression{

	public ThisBeanRepositoryExpression(ClsBeanRepository parent) {
		super(parent);
	}

	public Expression callGetByRecordMethod(BeanCls bean, Var record, QString alias) {
		return parent._this().callMethod(MethodGetFromRecord.getMethodName(bean), record, alias);
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
