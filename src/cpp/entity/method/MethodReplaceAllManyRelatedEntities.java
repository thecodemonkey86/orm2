package cpp.entity.method;

import util.StringUtil;
import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.entity.Entities;
import cpp.orm.OrmUtil;
import database.relation.IManyRelation;

public class MethodReplaceAllManyRelatedEntities extends Method {

	protected IManyRelation rel;
	Param beans;
	public MethodReplaceAllManyRelatedEntities(IManyRelation r) {
		super(Public, Types.Void, "replaceAll"+StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrName(r)));
		beans = addParam(new Param(Types.qlist(Entities.get(r.getDestTable()).toSharedPtr()).toConstRef(), OrmUtil.getManyRelationDestAttrName(r)));
		rel=r;
	}

	@Override
	public void addImplementation() {
//		addInstr( _this().callMethodInstruction(MethodRemoveAllManyRelatedEntities.getMethodName(rel)));
//		ForeachLoop foreach = _foreach(new Var(Entities.get(rel.getDestTable()).toSharedPtr().toConstRef(), "_"+  rel.getDestTable().getCamelCaseName()), beans);
//		
//		if(rel instanceof OneToManyRelation)
//			foreach._callMethodInstr(_this(), MethodAddRelatedEntity.getMethodName((OneToManyRelation) rel), foreach.getVar());
//		else if(rel instanceof ManyRelation)
//			foreach._callMethodInstr(_this(), MethodAddManyToManyRelatedEntity.getMethodName((ManyRelation) rel), foreach.getVar());
	}

}
