package sunjava.entity.method;

import database.relation.OneRelation;
import sunjava.core.Attr;
import sunjava.core.Method;
import sunjava.core.Param;
import sunjava.entity.EntityCls;
import util.StringUtil;
import util.pg.PgCppUtil;

public class MethodAttrSetterInternal extends Method{

	Attr a;
	
	public MethodAttrSetterInternal(EntityCls cls,  Attr a) {
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
