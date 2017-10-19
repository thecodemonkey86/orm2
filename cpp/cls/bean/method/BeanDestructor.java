package cpp.cls.bean.method;

import cpp.cls.Destructor;
import cpp.cls.bean.BeanCls;
import cpp.cls.expression.Expressions;
import cpp.orm.OrmUtil;
import model.OneRelation;

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
