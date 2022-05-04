package php.entityrepository.method;

import php.core.Type;
import php.core.method.Method;

public abstract class AbstractBeanRepositoryCommitTransactionMethod extends Method{

	public AbstractBeanRepositoryCommitTransactionMethod(Type returnType) {
		super(Public, returnType, "commitTransaction");
		setStatic(true);
	}
	
	
}
