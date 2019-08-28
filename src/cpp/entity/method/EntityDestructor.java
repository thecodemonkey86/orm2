package cpp.entity.method;

import cpp.core.Destructor;
import cpp.core.expression.Expressions;
import cpp.entity.EntityCls;
import cpp.orm.OrmUtil;
import database.relation.OneRelation;

public class EntityDestructor extends Destructor {

	EntityCls bean;
	
	public EntityDestructor(EntityCls bean) {
		this.bean = bean;
		setVirtualQualifier(true);
	}

	@Override
	public void addImplementation() {

		for(OneRelation r:bean.getOneRelations()) {
			addInstr(_assignInstruction(_this().accessAttr(OrmUtil.getOneRelationDestAttrName(r)), Expressions.Nullptr));
		}
	}
}
