package cpp.cls.bean.method;

import util.StringUtil;
import model.OneToManyRelation;
import cpp.Types;
import cpp.cls.Attr;
import cpp.cls.Method;
import cpp.cls.Param;
import cpp.orm.OrmUtil;

public class MethodAddRelatedBean extends Method {

	protected OneToManyRelation rel;
	
	public MethodAddRelatedBean(OneToManyRelation r, Param p) {
		super(Public, Types.Void, "add"+StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrNameSingular(r)));
		addParam(p);
		rel=r;
	}

	@Override
	public void addImplementation() {
		Attr a=parent.getAttrByName(OrmUtil.getOneToManyRelationDestAttrName(rel));
		addInstr(a.callMethod("append",getParam("bean")).asInstruction());
//		addInstr(parent.getAttrByName("_added"+StringUtil.ucfirst(a.getName())).callMethod("append",getParam("bean")).asInstruction());
	}

}
