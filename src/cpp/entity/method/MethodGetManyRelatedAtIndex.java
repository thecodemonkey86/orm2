package cpp.entity.method;

import cpp.Types;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.core.Param;
import cpp.entity.ManyAttr;
import cpp.lib.ClsQList;
import cpp.orm.OrmUtil;
import database.relation.IManyRelation;
import util.StringUtil;

public class MethodGetManyRelatedAtIndex extends Method{

	Param pIndex;
	Attr a;
	
	public MethodGetManyRelatedAtIndex(ManyAttr a, IManyRelation r) {
		super(Public, ((ClsQList) a.getClassType()).getElementType().toConstRef() ,"get"+ StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrNameSingular(r) ));
		pIndex = addParam(new Param(Types.SizeT, "index"));
		this.a = a;
		setConstQualifier(true);
	}

	@Override
	public void addImplementation() {
		_return(a.arrayIndex(pIndex));
		
	}

}
