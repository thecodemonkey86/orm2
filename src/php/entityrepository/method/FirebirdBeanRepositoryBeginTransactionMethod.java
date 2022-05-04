package php.entityrepository.method;

import php.core.Attr;
import php.core.PhpCls;
import php.core.PhpFunctions;
import php.core.Types;
import php.core.expression.Expressions;

public class FirebirdBeanRepositoryBeginTransactionMethod extends AbstractBeanRepositoryBeginTransactionMethod{

	public FirebirdBeanRepositoryBeginTransactionMethod() {
		super(Types.Resource);
	}

	@Override
	public void addImplementation() {
		PhpCls repo = (PhpCls) parent;
		_return(
		PhpFunctions.ibase_trans.call(Expressions.Null, repo.accessStaticAttribute(new Attr(Types.Resource, "sqlCon")))
		);
		
	}

}
