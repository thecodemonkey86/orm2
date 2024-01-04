package sunjava.entityrepository.expression;

import sunjava.core.JavaString;
import sunjava.core.expression.Expression;
import sunjava.core.expression.ThisExpression;
import sunjava.core.expression.Var;
import sunjava.entity.EntityCls;
import sunjava.entityrepository.ClsEntityRepository;

public class ThisBeanRepositoryExpression extends ThisExpression{

	public ThisBeanRepositoryExpression(ClsEntityRepository parent) {
		super(parent);
	}

	public Expression callGetByRecordMethod(EntityCls bean, Var record, JavaString alias) {
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
