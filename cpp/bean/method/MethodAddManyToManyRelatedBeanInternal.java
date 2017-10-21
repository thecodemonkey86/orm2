package cpp.bean.method;

import util.StringUtil;
import cpp.Types;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.core.Param;
import cpp.orm.OrmUtil;
import database.relation.ManyRelation;

public class MethodAddManyToManyRelatedBeanInternal extends Method {

	protected ManyRelation rel;
	
	public MethodAddManyToManyRelatedBeanInternal(ManyRelation r, Param p) {
		super(Public, Types.Void, 
				"add"+StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrNameSingular(r))+ "Internal");
		addParam(p);
		rel=r;
	}

	@Override
	public void addImplementation() {
		Attr a=parent.getAttrByName(OrmUtil.getManyRelationDestAttrName(rel));
		addInstr(a.callMethod("append",getParam("bean")).asInstruction());
//		BeanCls bean=(BeanCls) parent;
//		BeanCls relationBean = Beans.get( rel.getDestTable());
//		if (bean.getTbl().getPrimaryKey().isMultiColumn()) {
//			throw new RuntimeException("no impl");
//		} else {
//				addInstr(parent.getAttrByName(a.getName()+"Added").callMethod("append",getParam("bean").callAttrGetter(relationBean.getTbl().getPrimaryKey().getFirstColumn().getCamelCaseName())).asInstruction());	
//		}
//		
		
	}
	
	

}
