package cpp.bean.method;

import cpp.bean.BeanCls;
import cpp.core.Destructor;
import cpp.core.expression.Expressions;
import cpp.orm.OrmUtil;
import database.relation.OneRelation;

public class BeanDestructor extends Destructor {

	BeanCls bean;
	
	public BeanDestructor(BeanCls bean) {
		this.bean = bean;
	}

	@Override
	public void addImplementation() {

		for(OneRelation r:bean.getOneRelations()) {
			addInstr(_assignInstruction(_this().accessAttr(OrmUtil.getOneRelationDestAttrName(r)), Expressions.Nullptr));
		}
	}
}
