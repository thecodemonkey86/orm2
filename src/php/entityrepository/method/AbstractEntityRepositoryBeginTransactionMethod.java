package php.entityrepository.method;

import php.core.Type;
import php.core.method.Method;

public abstract class AbstractEntityRepositoryBeginTransactionMethod extends Method{

	public AbstractEntityRepositoryBeginTransactionMethod(Type returnType) {
		super(Public, returnType, "beginTransaction");
		setStatic(true);
	}
	
	
}
