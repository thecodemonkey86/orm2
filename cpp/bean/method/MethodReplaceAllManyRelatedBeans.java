package cpp.bean.method;

import util.StringUtil;
import cpp.Types;
import cpp.bean.Beans;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.expression.Var;
import cpp.core.instruction.ForeachLoop;
import cpp.orm.OrmUtil;
import database.relation.IManyRelation;
import database.relation.ManyRelation;
import database.relation.OneToManyRelation;

public class MethodReplaceAllManyRelatedBeans extends Method {

	protected IManyRelation rel;
	Param beans;
	public MethodReplaceAllManyRelatedBeans(IManyRelation r) {
		super(Public, Types.Void, "replaceAll"+StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrName(r)));
		beans = addParam(new Param(Types.qvector(Beans.get(r.getDestTable()).toSharedPtr()).toConstRef(), OrmUtil.getManyRelationDestAttrName(r)));
		rel=r;
	}

	@Override
	public void addImplementation() {
		addInstr( _this().callMethodInstruction(MethodRemoveAllManyRelatedBeans.getMethodName(rel)));
		ForeachLoop foreach = _foreach(new Var(Beans.get(rel.getDestTable()).toSharedPtr().toConstRef(), "_"+  rel.getDestTable().getCamelCaseName()), beans);
		
		if(rel instanceof OneToManyRelation)
			foreach._callMethodInstr(_this(), MethodAddRelatedBean.getMethodName((OneToManyRelation) rel), foreach.getVar());
		else if(rel instanceof ManyRelation)
			foreach._callMethodInstr(_this(), MethodAddManyToManyRelatedBean.getMethodName((ManyRelation) rel), foreach.getVar());
	}

}
