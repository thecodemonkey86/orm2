package php.beanrepository.method;

import php.core.Param;
import php.core.PhpFunctions;
import php.core.Types;

public class FirebirdBeanRepositoryRollbackTransactionMethod extends AbstractBeanRepositoryRollbackTransactionMethod{

	Param transactionHandle;
	
	public FirebirdBeanRepositoryRollbackTransactionMethod() {
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
