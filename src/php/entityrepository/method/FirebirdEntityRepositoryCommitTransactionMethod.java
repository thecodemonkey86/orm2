package php.entityrepository.method;

import php.core.Param;
import php.core.PhpFunctions;
import php.core.Types;

public class FirebirdEntityRepositoryCommitTransactionMethod extends AbstractEntityRepositoryCommitTransactionMethod{

	Param transactionHandle;
	
	public FirebirdEntityRepositoryCommitTransactionMethod() {
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
