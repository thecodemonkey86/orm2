package sunjava.entity.method;

import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import sunjava.core.Attr;
import sunjava.core.JavaCls;
import sunjava.core.Method;
import sunjava.core.Param;
import sunjava.core.Types;
import sunjava.core.expression.NewOperator;
import sunjava.lib.ClsArrayList;
import sunjava.orm.OrmUtil;
import util.StringUtil;

public class MethodAddRelatedEntityInternal extends Method {

	protected OneToManyRelation rel;
	
	public MethodAddRelatedEntityInternal(OneToManyRelation r, Param p) {
		super(Public, Types.Void, getMethodName(r) );
		addParam(p);
		rel=r;
	}
	
	public static String getMethodName(OneRelation r) {
		return "add"+StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrNameSingular(r))+"Internal";
	}
	
	public static String getMethodName(OneToManyRelation r) {
		return "add"+StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrNameSingular(r))+"Internal";
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
		JavaCls parent = (JavaCls) this.parent;
		Attr a=parent.getAttrByName(OrmUtil.getOneToManyRelationDestAttrName(rel));
		_if(a.isNull()).addIfInstr(a.assign(new NewOperator(a.getType())));
		addInstr(a.callMethod(ClsArrayList.add,getParam("entity")).asInstruction());
//		addInstr(parent.getAttrByName("_added"+StringUtil.ucfirst(a.getName())).callMethod("append",getParam("entity")).asInstruction());
	}
	
	public static MethodAddRelatedEntityInternal prototype() {
		return new MethodAddRelatedEntityInternal(null, null);
	}

}
