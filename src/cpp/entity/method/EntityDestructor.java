package cpp.entity.method;

import cpp.core.Destructor;
import cpp.core.expression.Expressions;
import cpp.entity.EntityCls;
import cpp.orm.OrmUtil;
import database.relation.OneRelation;

public class EntityDestructor extends Destructor {

	EntityCls entity;
	
	public EntityDestructor(EntityCls entity) {
		this.entity = entity;
		//setVirtualQualifier(true);
	}

	@Override
	public void addImplementation() {

		for(OneRelation r:entity.getOneRelations()) {
			addInstr(_assignInstruction(_this().accessAttr(OrmUtil.getOneRelationDestAttrName(r)), Expressions.Nullptr));
		}
	}
}
