package sunjava.cls.bean.method;

import sunjava.Types;
import sunjava.cls.Attr;
import sunjava.cls.JavaCls;
import sunjava.cls.Method;
import sunjava.cls.Param;
import sunjava.cls.expression.NewOperator;
import sunjava.lib.ClsArrayList;
import sunjava.lib.ClsLinkedHashSet;
import sunjava.orm.OrmUtil;
import model.OneToManyRelation;

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
