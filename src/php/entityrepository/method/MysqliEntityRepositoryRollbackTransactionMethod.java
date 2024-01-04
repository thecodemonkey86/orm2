package php.entityrepository.method;

import php.core.Attr;
import php.core.PhpCls;
import php.core.Types;
import php.lib.ClsMysqli;

public class MysqliEntityRepositoryRollbackTransactionMethod extends AbstractEntityRepositoryRollbackTransactionMethod{

	public MysqliEntityRepositoryRollbackTransactionMethod() {
		super(Types.Bool);
	}

	@Override
	public void addImplementation() {
		PhpCls repo = (PhpCls) parent;
		_return(
		repo.accessStaticAttribute(new Attr(Types.Resource, "sqlCon")).callMethod(ClsMysqli.rollback)
		);
		
	}

}
