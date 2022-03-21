package cpp.jsonentity.method;

import cpp.Types;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.jsonentity.ManyAttr;
import cpp.lib.ClsQList;
import cpp.orm.OrmUtil;
import database.relation.IManyRelation;
import util.StringUtil;

public class MethodGetManyRelatedCount extends Method{

	Attr a;
	
	public MethodGetManyRelatedCount(ManyAttr a, IManyRelation r) {
		super(Public, Types.Int ,"get"+ StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrName(r)+"Count" ));
		this.a = a;
		setConstQualifier(true);
	}

	@Override
	public void addImplementation() {
		_return(a.callMethod(ClsQList.size));
		
	}

}
