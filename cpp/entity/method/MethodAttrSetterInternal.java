package cpp.entity.method;

import util.StringUtil;
import util.pg.PgCppUtil;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.RawPtr;
import cpp.entity.EntityCls;
import database.relation.OneRelation;

public class MethodAttrSetterInternal extends Method{

	Attr a;
	
	public MethodAttrSetterInternal(EntityCls cls,  Attr a) {
		super(Public, cls.toRawPointer(), "set"+StringUtil.ucfirst(a.getName())+"Internal");
		this.a=a;
		addParam(new Param(a.getType().isPrimitiveType() || a.getType()instanceof RawPtr ? a.getType() : a.getType().toConstRef(), a.getName()));
	}

	@Override
	public void addImplementation() {
		_assign(_accessThis(a), getParam(a.getName()));
		_return(_this());
		
	}

	public static String getMethodName(OneRelation r) {
		return "set"+StringUtil.ucfirst(PgCppUtil.getOneRelationDestAttrName(r))+"Internal";
	}

	

}
