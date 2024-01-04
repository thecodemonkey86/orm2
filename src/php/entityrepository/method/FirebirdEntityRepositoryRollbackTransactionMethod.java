package php.entityrepository.method;

import php.core.Param;
import php.core.PhpFunctions;
import php.core.Types;

public class FirebirdEntityRepositoryRollbackTransactionMethod extends AbstractEntityRepositoryRollbackTransactionMethod{

	Param transactionHandle;
	
	public FirebirdEntityRepositoryRollbackTransactionMethod() {
		super(Types.Bool);
		transactionHandle = addParam(new Param(Types.Resource, "transactionHandle"));
	}

	@Override
	public void addImplementation() {
		_return(
		PhpFunctions.ibase_rollback.call(transactionHandle)
		);
		
	}

}
