package cpp.entity.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.expression.CreateObjectExpression;
import cpp.core.expression.NewOperator;
import cpp.core.expression.StdMoveExpression;
import cpp.entity.EntityCls;

public class MethodCreateQuery extends Method {

	public MethodCreateQuery(EntityCls cls) {
		super(Public, Types.entityQuerySelect(cls).toUniquePointer(), "createQuery");
		setStatic(true);
	}

	@Override
	public void addImplementation() {
		_return(new StdMoveExpression(new CreateObjectExpression(returnType, new NewOperator(Types.entityQuerySelect((EntityCls) parent), parent.getAttrByName("sqlCon").callMethod("buildQuery")) )));
		//_return(new MakeSharedExpression((SharedPtr)returnType, parent.getStaticAttribute("sqlCon").callMethod("buildQuery")));
	}

}
