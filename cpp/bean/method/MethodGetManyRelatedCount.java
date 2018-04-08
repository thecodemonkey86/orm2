package cpp.bean.method;

import cpp.Types;
import cpp.bean.ManyAttr;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.lib.ClsQVector;
import cpp.orm.OrmUtil;
import database.relation.IManyRelation;
import util.StringUtil;

public class MethodGetManyRelatedCount extends Method{

	Attr a;
	
	public MethodGetManyRelatedCount(ManyAttr a, IManyRelation r) {
		super(Public, Types.Int ,"get"+ StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrNameSingular(r)+"Count" ));
		this.a = a;
		setConstQualifier(true);
	}

	@Override
	public void addImplementation() {
		_return(a.callMethod(ClsQVector.size));
		
	}

}
