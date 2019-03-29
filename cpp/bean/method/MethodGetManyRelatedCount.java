package cpp.bean.method;

import cpp.Types;
import cpp.bean.BeanCls;
import cpp.bean.ManyAttr;
import cpp.beanrepository.method.MethodBeanLoad;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.Expressions;
import cpp.core.instruction.IfBlock;
import cpp.lib.ClsQVector;
import cpp.orm.OrmUtil;
import database.relation.IManyRelation;
import util.StringUtil;

public class MethodGetManyRelatedCount extends Method{

	Attr a;
	
	public MethodGetManyRelatedCount(ManyAttr a, IManyRelation r) {
		super(Public, Types.Int ,"get"+ StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrNameSingular(r)+"Count" ));
		this.a = a;
		//setConstQualifier(true);
	}

	@Override
	public void addImplementation() {
		IfBlock ifNotLoaded = _if(Expressions.not(parent.getAttrByName("loaded")));
		
		ifNotLoaded.thenBlock()._callMethodInstr(_this().accessAttr(BeanCls.repository), MethodBeanLoad.getMethodName(), _this());
		ifNotLoaded.thenBlock()._assign(parent.getAttrByName("loaded"), BoolExpression.TRUE);
		_return(a.callMethod(ClsQVector.size));
		
	}

}
