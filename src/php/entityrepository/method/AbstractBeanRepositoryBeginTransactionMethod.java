package php.entityrepository.method;

import php.core.Type;
import php.core.method.Method;

public abstract class AbstractBeanRepositoryBeginTransactionMethod extends Method{

	public AbstractBeanRepositoryBeginTransactionMethod(Type returnType) {
		super(Public, returnType, "beginTransaction");
		setStatic(true);
	}
	
	
}
