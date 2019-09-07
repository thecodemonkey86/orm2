package cpp.entityrepository.expression;

import cpp.core.QString;
import cpp.core.expression.Expression;
import cpp.core.expression.ThisExpression;
import cpp.core.expression.Var;
import cpp.entity.EntityCls;
import cpp.entityrepository.ClsEntityRepository;
import cpp.entityrepository.method.MethodGetFromRecord;

public class ThisEntityRepositoryExpression extends ThisExpression{

	public ThisEntityRepositoryExpression(ClsEntityRepository parent) {
		super(parent);
	}

	public Expression callGetByRecordMethod(EntityCls bean, Var record, QString alias) {
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
