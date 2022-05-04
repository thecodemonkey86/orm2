package php.entityrepository.method;

import php.core.Param;
import php.core.PhpFunctions;
import php.core.Types;

public class FirebirdBeanRepositoryCommitTransactionMethod extends AbstractBeanRepositoryCommitTransactionMethod{

	Param transactionHandle;
	
	public FirebirdBeanRepositoryCommitTransactionMethod() {
		super(Types.Bool);
		transactionHandle = addParam(new Param(Types.Resource, "transactionHandle"));
	}

	@Override
	public void addImplementation() {
		_return(
		PhpFunctions.ibase_commit.call(transactionHandle)
		);
		
	}

}
