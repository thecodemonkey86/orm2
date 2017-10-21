package cpp.bean.method;

import util.StringUtil;
import cpp.bean.BeanCls;
import cpp.beanrepository.method.MethodBeanLoad;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.core.TplCls;
import cpp.core.expression.Expressions;
import cpp.lib.ClsQVector;

public class MethodManyAttrGetter extends Method{
	protected Attr a;
	
	public MethodManyAttrGetter(Attr a) {
		super(Public,null, "get"+StringUtil.ucfirst(a.getName()));
//		
		setReturnType(new ClsQVector(((TplCls)a.getType()).getElementType()));
		this.a = a;
//		setConstQualifier(true);
	}

	@Override
	public void addImplementation() {
		
//		ClsOrderedSet orderedSet = Types.orderedSet(((TplCls)a.getType()).getElementType());
//		_return(a.callMethod( orderedSet.getMethod("toList")));
		_if(Expressions.not(parent.getAttrByName("loaded"))).thenBlock()._callMethodInstr(_this().accessAttr(BeanCls.repository), MethodBeanLoad.getMethodName(), _this());
		_return(a); 
	}

}

