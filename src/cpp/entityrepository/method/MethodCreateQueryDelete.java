package cpp.entityrepository.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.QString;
import cpp.core.expression.CreateObjectExpression;
import cpp.entity.EntityCls;

public class MethodCreateQueryDelete extends Method {
	EntityCls entity;
	
	public MethodCreateQueryDelete(EntityCls cls) {
		super(Public, Types.entityQueryDelete(cls),getMethodName(cls)
				);
		setStatic(true);
		this.entity=cls;
	}

	@Override
	public void addImplementation() {
		_return(new CreateObjectExpression(returnType,
				EntityCls.getDatabase().supportsDeleteTableAlias() 
				? QString.fromStringConstant(entity.getTbl().getEscapedName()+" e1") 
				: QString.fromStringConstant(entity.getTbl().getEscapedName()) 
				
				
				));
	}

	public static String getMethodName(EntityCls cls) {
		return "delete"+cls.getName();
	}

}
