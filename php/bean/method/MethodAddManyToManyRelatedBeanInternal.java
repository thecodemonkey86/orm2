package php.bean.method;

import database.relation.ManyRelation;
import php.core.Attr;
import php.core.Param;
import php.core.PhpCls;
import php.core.Types;
import php.core.expression.NewOperator;
import php.core.method.Method;
import php.orm.OrmUtil;
import util.StringUtil;

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
