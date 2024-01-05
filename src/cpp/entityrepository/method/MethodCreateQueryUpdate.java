package cpp.entityrepository.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.QString;
import cpp.core.expression.CreateObjectExpression;
import cpp.entity.EntityCls;

public class MethodCreateQueryUpdate extends Method {
	EntityCls entity;
	
	public MethodCreateQueryUpdate(EntityCls cls) {
		//super(Public, new ClsBeanQuery(cls).toUniquePointer(), "createQuery"+cls.getName());
		super(Public, Types.beanQueryUpdate(cls),getMethodName(cls)
				);
		setStatic(true);
		this.entity=cls;
	}

	@Override
	public void addImplementation() {
		_return(new CreateObjectExpression(returnType,QString.fromStringConstant(entity.getTbl().getEscapedName()) ));
	}

	public static String getMethodName(EntityCls cls) {
		// TODO Auto-generated method stub
		return "update"+cls.getName();
	}

}
