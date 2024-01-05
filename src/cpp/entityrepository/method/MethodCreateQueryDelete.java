package cpp.entityrepository.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.QString;
import cpp.core.expression.CreateObjectExpression;
import cpp.entity.EntityCls;

public class MethodCreateQueryDelete extends Method {
	EntityCls entity;
	
	public MethodCreateQueryDelete(EntityCls cls) {
		//super(Public, new ClsBeanQuery(cls).toUniquePointer(), "createQuery"+cls.getName());
		super(Public, Types.beanQueryDelete(cls),getMethodName(cls)
				);
		setStatic(true);
		this.entity=cls;
	}

	@Override
	public void addImplementation() {
		//_return(new StdMoveExpression(new CreateObjectExpression(returnType, new NewOperator(new ClsBeanQuery(entity), parent.getAttrByName("sqlCon")) )));
		//_return(new MakeSharedExpression((SharedPtr)returnType, parent.getStaticAttribute("sqlCon").callMethod("buildQuery")));
		_return(new CreateObjectExpression(returnType,
				EntityCls.getDatabase().supportsDeleteTableAlias() 
				? QString.fromStringConstant(entity.getTbl().getEscapedName()+" e1") 
				: QString.fromStringConstant(entity.getTbl().getEscapedName()) 
				
				
				));
	}

	public static String getMethodName(EntityCls cls) {
		// TODO Auto-generated method stub
		return "delete"+cls.getName();
	}

}
