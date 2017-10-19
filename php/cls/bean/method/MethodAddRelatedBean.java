package php.cls.bean.method;

import php.Types;
import php.cls.Attr;
import php.cls.PhpCls;
import php.cls.Method;
import php.cls.Param;
import php.cls.expression.ArrayInitExpression;
import php.cls.expression.NewOperator;
import php.orm.OrmUtil;
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
		PhpCls parent = (PhpCls) this.parent;
		Attr a=parent.getAttrByName(OrmUtil.getOneToManyRelationDestAttrName(rel));
		_if(a.isNull()).addIfInstr(a.assign(new ArrayInitExpression()));
		addInstr(a.arrayPush(getParam("bean")));
//		addInstr(parent.getAttrByName("_added"+StringUtil.ucfirst(a.getName())).callMethod("append",getParam("bean")).asInstruction());
	}

}
