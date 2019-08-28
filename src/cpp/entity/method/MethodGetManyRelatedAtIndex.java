package cpp.entity.method;

import cpp.Types;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.core.Param;
import cpp.entity.ManyAttr;
import cpp.lib.ClsQVector;
import cpp.orm.OrmUtil;
import database.relation.IManyRelation;
import util.StringUtil;

public class MethodGetManyRelatedAtIndex extends Method{

	Param pIndex;
	Attr a;
	
	public MethodGetManyRelatedAtIndex(ManyAttr a, IManyRelation r) {
		super(Public, ((ClsQVector) a.getClassType()).getElementType().toConstRef() ,"get"+ StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrNameSingular(r) ));
		pIndex = addParam(new Param(Types.Int, "index"));
		this.a = a;
		setConstQualifier(true);
	}

	@Override
	public void addImplementation() {
		_return(a.arrayIndex(pIndex));
		
	}

}
