package cpp.entity.method;

import util.StringUtil;
import cpp.Types;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.TplCls;
import cpp.lib.ClsQList;
import cpp.util.ClsDbPool;
import database.relation.AbstractRelation;
import database.relation.IManyRelation;

public class MethodManyAttrGetter extends Method{
	protected Attr a;
	protected Param pSqlCon;
	protected IManyRelation relation;
	
	public MethodManyAttrGetter(Attr a,IManyRelation relation) {
		super(Public,null, "get"+StringUtil.ucfirst(a.getName()));
//		
		setReturnType(new ClsQList(((TplCls)a.getType()).getElementType()).toConstRef());
		this.a = a;
		pSqlCon = addParam(Types.QSqlDatabase.toConstRef(),"sqlCon",ClsDbPool.instance.callStaticMethod(ClsDbPool.getDatabase));
		this.relation=relation;
	//	setConstQualifier(true);
	}

	@Override
	public void addImplementation() {
		
//		ClsOrderedSet orderedSet = Types.orderedSet(((TplCls)a.getType()).getElementType());
//		_return(a.callMethod( orderedSet.getMethod("toList")));
		_callMethodInstr(_this(), parent.getMethod(MethodEnsureLoaded.getMethodName((AbstractRelation) relation)),pSqlCon);
		_return(a); 
	}

}

