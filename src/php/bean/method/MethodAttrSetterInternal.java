package php.bean.method;

import database.relation.OneRelation;
import php.bean.EntityCls;
import php.core.Attr;
import php.core.Param;
import php.core.method.Method;
import php.core.Types;
import util.StringUtil;
import util.pg.PgCppUtil;

public class MethodAttrSetterInternal extends Method{

	Attr a;
	
	public MethodAttrSetterInternal(EntityCls cls,  Attr a) {
		super(Public, Types.Void, "set"+StringUtil.ucfirst(a.getName())+"Internal");
		this.a=a;
		addParam(new Param(a.getType() , a.getName()));
	}

	@Override
	public void addImplementation() {
		_assign(_accessThis(a), getParam(a.getName()));
		//_return(_this());
		
	}

	public static String getMethodName(OneRelation r) {
		return "set"+StringUtil.ucfirst(PgCppUtil.getOneRelationDestAttrName(r))+"Internal";
	}

	

}
