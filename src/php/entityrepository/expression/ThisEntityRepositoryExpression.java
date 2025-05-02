package php.entityrepository.expression;

import php.core.expression.Expression;
import php.core.expression.ThisExpression;
import php.core.expression.Var;
import php.entity.EntityCls;
import php.entityrepository.ClsEntityRepository;

public class ThisEntityRepositoryExpression extends ThisExpression{

	public ThisEntityRepositoryExpression(ClsEntityRepository parent) {
		super(parent);
	}

	public Expression callGetByRecordMethod(EntityCls entity, Var record,  Expression alias) {
		return entity.callStaticMethod("getByRecord", accessAttr("sqlCon"), record, alias);
	}
	
//	public MethodCall callGetByRecordMethod(EntityCls entity, Expression...args) {
//		try{
//			return new MethodCall(this, ((Cls)getType()).getMethod("get"+entity.getName()+"ByRecord"),args);
//		} catch (Exception e) {
//			System.out.println(e);
//			throw e;
//		}
//	} 

}
