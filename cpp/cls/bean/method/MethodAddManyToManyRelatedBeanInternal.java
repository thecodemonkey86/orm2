package cpp.cls.bean.method;

import util.StringUtil;
import model.ManyRelation;
import cpp.Types;
import cpp.cls.Attr;
import cpp.cls.Method;
import cpp.cls.Param;
import cpp.orm.OrmUtil;

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
