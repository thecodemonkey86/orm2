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
	

}
