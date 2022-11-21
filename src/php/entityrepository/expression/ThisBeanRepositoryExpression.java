package php.entityrepository.expression;

import php.core.expression.Expression;
import php.core.expression.ThisExpression;
import php.core.expression.Var;
import php.entity.EntityCls;
import php.entityrepository.ClsEntityRepository;

public class ThisBeanRepositoryExpression extends ThisExpression{

	public ThisBeanRepositoryExpression(ClsEntityRepository parent) {
		super(parent);
	}

	public Expression callGetByRecordMethod(EntityCls bean, Var record,  Expression alias) {
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
