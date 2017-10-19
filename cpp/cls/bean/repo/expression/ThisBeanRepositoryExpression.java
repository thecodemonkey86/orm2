package cpp.cls.bean.repo.expression;

import cpp.cls.QString;
import cpp.cls.bean.BeanCls;
import cpp.cls.bean.repo.ClsBeanRepository;
import cpp.cls.bean.repo.method.MethodGetFromRecord;
import cpp.cls.expression.Expression;
import cpp.cls.expression.ThisExpression;
import cpp.cls.expression.Var;

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
