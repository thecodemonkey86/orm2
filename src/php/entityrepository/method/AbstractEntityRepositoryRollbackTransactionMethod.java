package php.entityrepository.method;

import php.core.Type;
import php.core.method.Method;

public abstract class AbstractEntityRepositoryRollbackTransactionMethod extends Method{

	public AbstractEntityRepositoryRollbackTransactionMethod(Type returnType) {
		super(Public, returnType, "rollbackTransaction");
		setStatic(true);
	}
	
	
}
