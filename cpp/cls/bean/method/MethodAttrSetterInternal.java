package cpp.cls.bean.method;

import pg.PgCppUtil;
import model.OneRelation;
import util.StringUtil;
import cpp.cls.Attr;
import cpp.cls.Method;
import cpp.cls.Param;
import cpp.cls.RawPtr;
import cpp.cls.bean.BeanCls;

public class MethodAttrSetterInternal extends Method{

	Attr a;
	
	public MethodAttrSetterInternal(BeanCls cls,  Attr a) {
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
