package cpp.jsonentityrepository.method;

import cpp.core.Method;
import cpp.core.expression.CreateObjectExpression;
import cpp.jsonentity.JsonEntity;
import cpp.jsonentityquery.ClsJsonEntityQueryDelete;

public class MethodJsonDelete extends Method{

	public MethodJsonDelete(JsonEntity entity)	{
		super(Public, new ClsJsonEntityQueryDelete(entity), "delete"+entity.getName());
		setStatic(true);
	}

	@Override
	public void addImplementation() {
		_return(new CreateObjectExpression(returnType)); 
		
	}

}
