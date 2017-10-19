package php.cls.bean.method;

import php.Types;
import php.cls.Attr;
import php.cls.PhpCls;
import php.cls.Method;
import php.cls.Param;
import php.cls.expression.NewOperator;

import php.orm.OrmUtil;
import util.StringUtil;
import model.ManyRelation;

public class MethodAddManyToManyRelatedBeanInternal extends Method {

	protected ManyRelation rel;
	
	public MethodAddManyToManyRelatedBeanInternal(ManyRelation r, Param p) {
		super(Public, Types.Void, 
				"add"+StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrNameSingular(r))+ "Internal");
		addParam(p);
		rel=r;
	}

	@Override
	public void addImplementation() {
		PhpCls parent = (PhpCls) this.parent;
		Attr a=parent.getAttrByName(OrmUtil.getManyRelationDestAttrName(rel));
		_if(a.isNull()).addIfInstr(a.assign(new NewOperator(a.getType())));
		addInstr(a.arrayPush(getParam("bean")));
		
	}
	
	

}
