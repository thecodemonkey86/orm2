package cpp.entity.method;

import util.StringUtil;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.core.TplCls;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.Expressions;
import cpp.core.instruction.IfBlock;
import cpp.entity.EntityCls;
import cpp.entityrepository.method.MethodEntityLoad;
import cpp.lib.ClsQList;

public class MethodManyAttrGetter extends Method{
	protected Attr a;
	
	public MethodManyAttrGetter(Attr a) {
		super(Public,null, "get"+StringUtil.ucfirst(a.getName()));
//		
		setReturnType(new ClsQList(((TplCls)a.getType()).getElementType()).toConstRef());
		this.a = a;
//		setConstQualifier(true);
	}

	@Override
	public void addImplementation() {
		
//		ClsOrderedSet orderedSet = Types.orderedSet(((TplCls)a.getType()).getElementType());
//		_return(a.callMethod( orderedSet.getMethod("toList")));
		IfBlock ifNotLoaded = _if(Expressions.not(parent.getAttrByName("loaded")));
		
		ifNotLoaded.thenBlock()._callMethodInstr(_this().accessAttr(EntityCls.repository), MethodEntityLoad.getMethodName(), _this());
		ifNotLoaded.thenBlock()._assign(parent.getAttrByName("loaded"), BoolExpression.TRUE);
		_return(a); 
	}

}

