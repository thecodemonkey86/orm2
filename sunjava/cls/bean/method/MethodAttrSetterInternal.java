package sunjava.cls.bean.method;

import pg.PgCppUtil;
import model.OneRelation;
import sunjava.cls.Attr;
import sunjava.cls.Method;
import sunjava.cls.Param;
import sunjava.cls.bean.BeanCls;
import util.StringUtil;

public class MethodAttrSetterInternal extends Method{

	Attr a;
	
	public MethodAttrSetterInternal(BeanCls cls,  Attr a) {
		super(Public, cls, "set"+StringUtil.ucfirst(a.getName())+"Internal");
		this.a=a;
		addParam(new Param(a.getType() , a.getName()));
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
