package cpp.jsonentity.method;

import cpp.core.Method;
import cpp.core.Type;
import cpp.lib.ClsQList;
import cpp.orm.OrmUtil;
import database.relation.IManyRelation;
import util.StringUtil;

public class MethodGetLastItem extends Method{

	IManyRelation relation;
	
	public MethodGetLastItem(Type relatedBeanCls, IManyRelation relation) {
		super(Public, relatedBeanCls.toConstRef(), "getLast" + StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrNameSingular(relation)));
		this.relation = relation;
	}

	@Override
	public void addImplementation() {
		_return(_this().accessAttr(OrmUtil.getManyRelationDestAttrName(relation)).callMethod(ClsQList.last));		
	}

}
