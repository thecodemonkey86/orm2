package sunjava.bean.method;

import database.relation.OneToManyRelation;
import sunjava.core.Attr;
import sunjava.core.JavaCls;
import sunjava.core.Method;
import sunjava.core.Param;
import sunjava.core.Types;
import sunjava.core.expression.NewOperator;
import sunjava.lib.ClsArrayList;
import sunjava.lib.ClsLinkedHashSet;
import sunjava.orm.OrmUtil;

public class MethodAddRelatedBean extends Method {

	protected OneToManyRelation rel;
	
	public MethodAddRelatedBean(OneToManyRelation r, Param p) {
		super(Public, Types.Void, OrmUtil.getAddRelatedBeanMethodName(r));
		addParam(p);
		rel=r;
	}

	@Override
	public void addImplementation() {
		JavaCls parent = (JavaCls) this.parent;
		Attr a=parent.getAttrByName(OrmUtil.getOneToManyRelationDestAttrName(rel));
		_if(a.isNull()).addIfInstr(a.assign(new NewOperator(a.getType())));
		addInstr(a.callMethod(methodCollectionAdd(a),getParam("bean")).asInstruction());
//		addInstr(parent.getAttrByName("_added"+StringUtil.ucfirst(a.getName())).callMethod("append",getParam("bean")).asInstruction());
	}

	protected String methodCollectionAdd(Attr a) {
		if(a.getType() instanceof ClsArrayList) {
			return ClsArrayList.add;
		} else if (a.getType() instanceof ClsLinkedHashSet) {
			return ClsLinkedHashSet.add;
		}
		throw new RuntimeException();
	}
}
