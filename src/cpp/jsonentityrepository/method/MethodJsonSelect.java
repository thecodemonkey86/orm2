package cpp.jsonentityrepository.method;

import cpp.core.Method;
import cpp.core.expression.CreateObjectExpression;
import cpp.jsonentity.JsonEntity;
import cpp.jsonentityquery.ClsJsonEntityQuerySelect;

public class MethodJsonSelect extends Method{

	public MethodJsonSelect(JsonEntity entity)	{
		super(Public, new ClsJsonEntityQuerySelect(entity), "select"+entity.getName());
		setStatic(true);
	}

	@Override
	public void addImplementation() {
		_return(new CreateObjectExpression(returnType)); 
		
	}

}
