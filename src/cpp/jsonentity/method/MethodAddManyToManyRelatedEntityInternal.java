package cpp.jsonentity.method;

import util.StringUtil;
import cpp.Types;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.core.Param;
import cpp.lib.ClsQList;
import cpp.orm.OrmUtil;
import database.relation.ManyRelation;

public class MethodAddManyToManyRelatedEntityInternal extends Method {

	protected ManyRelation rel;
	Param pEntity; 
	public MethodAddManyToManyRelatedEntityInternal(ManyRelation r, Param p) {
		super(Public, Types.Void, getMethodName(r) );
		pEntity = addParam(p);
		rel=r;
	}
	
	
	public static String getMethodName(ManyRelation r) {
		return "add"+StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrNameSingular(r))+"Internal";
	}
	
//	public static String getMethodName(AbstractRelation r) {
//		if (r instanceof OneRelation) {
//			return "add"+StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrNameSingular((OneRelation) r))+"Internal";	
//		} else if (r instanceof OneToManyRelation) {
//				return "add"+StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrNameSingular((OneToManyRelation) r))+"Internal";
//		} else {
//			return "add"+StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrNameSingular((ManyRelation) r))+"Internal";
//		}
//		
//		
//	}

	@Override
	public void addImplementation() {
		Attr a=parent.getAttrByName(OrmUtil.getManyRelationDestAttrName(rel));
		addInstr(a.callMethod(ClsQList.append,pEntity).asInstruction());
//		addInstr(parent.getAttrByName("_added"+StringUtil.ucfirst(a.getName())).callMethod("append",getParam("entity")).asInstruction());
	}
	
	public static MethodAddManyToManyRelatedEntityInternal prototype() {
		return new MethodAddManyToManyRelatedEntityInternal(null, null);
	}

}
