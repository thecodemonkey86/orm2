package php.cls.bean.method;

import php.Types;
import php.cls.Attr;
import php.cls.PhpCls;
import php.cls.Method;
import php.cls.Param;
import php.cls.expression.ArrayInitExpression;
import php.cls.expression.NewOperator;

import php.orm.OrmUtil;
import util.StringUtil;
import model.ManyRelation;
import model.OneRelation;
import model.OneToManyRelation;

public class MethodAddRelatedBeanInternal extends Method {

	protected OneToManyRelation rel;
	
	public MethodAddRelatedBeanInternal(OneToManyRelation r, Param p) {
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
		PhpCls parent = (PhpCls) this.parent;
		Attr a=parent.getAttrByName(OrmUtil.getOneToManyRelationDestAttrName(rel));
		_if(a.isNull()).addIfInstr(a.assign(new ArrayInitExpression()));
		addInstr(a.arrayPush(getParam("bean")));
//		addInstr(parent.getAttrByName("_added"+StringUtil.ucfirst(a.getName())).callMethod("append",getParam("bean")).asInstruction());
	}
	
	public static MethodAddRelatedBeanInternal prototype() {
		return new MethodAddRelatedBeanInternal(null, null);
	}

}
