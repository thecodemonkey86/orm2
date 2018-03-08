package cpp.bean.method;

import util.StringUtil;
import cpp.Types;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.core.Param;
import cpp.orm.OrmUtil;
import database.relation.OneToManyRelation;

public class MethodAddRelatedBean extends Method {

	protected OneToManyRelation rel;
	
	public MethodAddRelatedBean(OneToManyRelation r, Param p) {
		super(Public, Types.Void, getMethodName(r));
		addParam(p);
		rel=r;
	}

	@Override
	public void addImplementation() {
		Attr a=parent.getAttrByName(OrmUtil.getOneToManyRelationDestAttrName(rel));
		addInstr(a.callMethod("append",getParam("bean")).asInstruction());
//		addInstr(parent.getAttrByName("_added"+StringUtil.ucfirst(a.getName())).callMethod("append",getParam("bean")).asInstruction());
	}

	public static String getMethodName(OneToManyRelation r) {
		return "add"+StringUtil.ucfirst(OrmUtil.getOneToManyRelationDestAttrNameSingular(r));
	}
}
