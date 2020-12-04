package cpp.entity.method;

import util.StringUtil;
import cpp.Types;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.TplCls;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.Expressions;
import cpp.core.instruction.IfBlock;
import cpp.entityrepository.method.MethodEntityLoad;
import cpp.lib.ClsQVector;
import cpp.util.ClsDbPool;

public class MethodManyAttrGetter extends Method{
	protected Attr a;
	protected Param pSqlCon;
	
	public MethodManyAttrGetter(Attr a) {
		super(Public,null, "get"+StringUtil.ucfirst(a.getName()));
//		
		setReturnType(new ClsQVector(((TplCls)a.getType()).getElementType()));
		this.a = a;
		pSqlCon = addParam(Types.QSqlDatabase.toConstRef(),"sqlCon",ClsDbPool.instance.callStaticMethod(ClsDbPool.getDatabase));
	//	setConstQualifier(true);
	}

	@Override
	public void addImplementation() {
		
//		ClsOrderedSet orderedSet = Types.orderedSet(((TplCls)a.getType()).getElementType());
//		_return(a.callMethod( orderedSet.getMethod("toList")));
		IfBlock ifNotLoaded = _if(Expressions.not(parent.getAttrByName("loaded")));
		
		ifNotLoaded.thenBlock().addInstr(Types.EntityRepository.callStaticMethod(MethodEntityLoad.getMethodName(), _this().dereference(),pSqlCon).asInstruction());
		ifNotLoaded.thenBlock()._assign(parent.getAttrByName("loaded"), BoolExpression.TRUE);
		_return(a); 
	}

}

