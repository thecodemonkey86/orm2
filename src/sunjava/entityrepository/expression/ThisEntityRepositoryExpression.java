package sunjava.entityrepository.expression;

import sunjava.core.JavaString;
import sunjava.core.expression.Expression;
import sunjava.core.expression.ThisExpression;
import sunjava.core.expression.Var;
import sunjava.entity.EntityCls;
import sunjava.entityrepository.ClsEntityRepository;

public class ThisEntityRepositoryExpression extends ThisExpression{

	public ThisEntityRepositoryExpression(ClsEntityRepository parent) {
		super(parent);
	}

	public Expression callGetByRecordMethod(EntityCls entity, Var record, JavaString alias) {
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
