package php.entityrepository.method;

import php.core.Attr;
import php.core.PhpCls;
import php.core.PhpFunctions;
import php.core.Types;
import php.core.expression.PhpStringLiteral;

public class PgEntityRepositoryRollbackTransactionMethod extends AbstractEntityRepositoryRollbackTransactionMethod{

	public PgEntityRepositoryRollbackTransactionMethod() {
		super(Types.Resource);
	}

	@Override
	public void addImplementation() {
		PhpCls repo = (PhpCls) parent;
		_return(PhpFunctions.pg_query.call(repo.accessStaticAttribute(new Attr(Types.Resource, "sqlCon")), new PhpStringLiteral("ROLLBACK")));
		
	}

}
