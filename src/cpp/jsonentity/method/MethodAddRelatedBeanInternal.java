package cpp.jsonentity.method;

import util.StringUtil;
import cpp.Types;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.core.Param;
import cpp.lib.ClsQVector;
import cpp.orm.OrmUtil;
import database.relation.ManyRelation;
import database.relation.OneToManyRelation;

public class MethodAddRelatedBeanInternal extends Method {

	protected OneToManyRelation rel;
	Param pBean; 
	public MethodAddRelatedBeanInternal(OneToManyRelation r, Param p) {
		super(Public, Types.Void, getMethodName(r) );
		pBean = addParam(p);
		rel=r;
	}
	
		
	public static String getMethodName(OneToManyRelation r) {
		return "add"+StringUtil.ucfirst(OrmUtil.getOneToManyRelationDestAttrNameSingular(r))+"Internal";
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
		Attr a=parent.getAttrByName(OrmUtil.getOneToManyRelationDestAttrName(rel));
		addInstr(a.callMethod(ClsQVector.append,pBean).asInstruction());
//		addInstr(parent.getAttrByName("_added"+StringUtil.ucfirst(a.getName())).callMethod("append",getParam("bean")).asInstruction());
	}
	
	public static MethodAddRelatedBeanInternal prototype() {
		return new MethodAddRelatedBeanInternal(null, null);
	}

}
