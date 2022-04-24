package cpp.jsonentity.method;

import util.StringUtil;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.core.TplCls;
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
		/*IfBlock ifNotLoaded = _if(Expressions.not(parent.getAttrByName("loaded")));
		
		ifNotLoaded.thenBlock().addInstr(JsonTypes.JsonEntityRepository.callStaticMethod("getInstance").callMethod( MethodEntityLoad.getMethodName(), _this()).asInstruction());
		ifNotLoaded.thenBlock()._assign(parent.getAttrByName("loaded"), BoolExpression.TRUE);*/
		_return(a); 
	}

}

