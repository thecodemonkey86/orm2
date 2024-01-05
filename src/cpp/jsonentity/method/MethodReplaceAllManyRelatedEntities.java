package cpp.jsonentity.method;

import util.StringUtil;
import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.expression.Var;
import cpp.core.instruction.ForeachLoop;
import cpp.jsonentity.JsonEntities;
import cpp.orm.OrmUtil;
import database.relation.IManyRelation;
import database.relation.ManyRelation;
import database.relation.OneToManyRelation;

public class MethodReplaceAllManyRelatedEntities extends Method {

	protected IManyRelation rel;
	Param entities;
	public MethodReplaceAllManyRelatedEntities(IManyRelation r) {
		super(Public, Types.Void, "replaceAll"+StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrName(r)));
		entities = addParam(new Param(Types.qlist(JsonEntities.get(r.getDestTable()).toSharedPtr()).toConstRef(), OrmUtil.getManyRelationDestAttrName(r)));
		rel=r;
	}

	@Override
	public void addImplementation() {
		addInstr( _this().callMethodInstruction(MethodRemoveAllManyRelatedEntities.getMethodName(rel)));
		ForeachLoop foreach = _foreach(new Var(JsonEntities.get(rel.getDestTable()).toSharedPtr().toConstRef(),  OrmUtil.getManyRelationDestAttrNameSingular(rel)), entities);
		
		if(rel instanceof OneToManyRelation)
			foreach._callMethodInstr(_this(), MethodAddRelatedBean.getMethodName((OneToManyRelation) rel), foreach.getVar());
		else if(rel instanceof ManyRelation)
			foreach._callMethodInstr(_this(), MethodAddManyToManyRelatedEntity.getMethodName((ManyRelation) rel), foreach.getVar());
	}

}
