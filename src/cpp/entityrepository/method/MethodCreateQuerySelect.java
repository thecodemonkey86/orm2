package cpp.entityrepository.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.CreateObjectExpression;
import cpp.entity.EntityCls;

public class MethodCreateQuerySelect extends Method {
	EntityCls entity;
	Param pLazy;
	
	public MethodCreateQuerySelect(EntityCls cls) {
		//super(Public, new ClsBeanQuery(cls).toUniquePointer(), "createQuery"+cls.getName());
		super(Public, Types.beanQuerySelect(cls),getMethodName(cls)
				);
		
		if(cls.hasRelations()) {
			pLazy = addParam(Types.Bool, "lazyLoading",BoolExpression.FALSE);
		}
		
		setStatic(true);
		this.entity=cls;
	}

	@Override
	public void addImplementation() {
		//_return(new StdMoveExpression(new CreateObjectExpression(returnType, new NewOperator(new ClsBeanQuery(entity), parent.getAttrByName("sqlCon")) )));
		//_return(new MakeSharedExpression((SharedPtr)returnType, parent.getStaticAttribute("sqlCon").callMethod("buildQuery")));
		if(entity.hasRelations()) {
			_return(new CreateObjectExpression(returnType, pLazy ));
		} else {
			_return(new CreateObjectExpression(returnType));
		}
	}

	public static String getMethodName(EntityCls cls) {
		// TODO Auto-generated method stub
		return "select"+cls.getName();
	}

}
