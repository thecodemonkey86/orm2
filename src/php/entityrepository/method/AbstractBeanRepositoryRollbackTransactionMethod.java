package php.entityrepository.method;

import php.core.Type;
import php.core.method.Method;

public abstract class AbstractBeanRepositoryRollbackTransactionMethod extends Method{

	public AbstractBeanRepositoryRollbackTransactionMethod(Type returnType) {
		super(Public, returnType, "rollbackTransaction");
		setStatic(true);
	}
	
	
}
