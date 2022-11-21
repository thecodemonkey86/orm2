package php.entityrepository.method;

import php.core.Type;
import php.core.method.Method;

public abstract class AbstractEntityRepositoryCommitTransactionMethod extends Method{

	public AbstractEntityRepositoryCommitTransactionMethod(Type returnType) {
		super(Public, returnType, "commitTransaction");
		setStatic(true);
	}
	
	
}
