package cpp.cls.bean.method;

import util.StringUtil;
import model.Column;
import model.ManyRelation;
import cpp.Struct;
import cpp.Types;
import cpp.cls.Attr;
import cpp.cls.Method;
import cpp.cls.Param;
import cpp.cls.bean.BeanCls;
import cpp.cls.bean.Beans;
import cpp.cls.expression.Var;
import cpp.orm.OrmUtil;

public class MethodRemoveManyToManyRelatedBean extends Method {

	protected ManyRelation rel;
	
	public MethodRemoveManyToManyRelatedBean(ManyRelation r, Param p) {
		super(Public, Types.Void, "remove"+StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrNameSingular(r)));
		addParam(p);
		rel=r;
	}

	@Override
	public void addImplementation() {
		Attr a=parent.getAttrByName(OrmUtil.getManyRelationDestAttrName(rel));
		addInstr(a.callMethod("removeOne",getParam("bean")).asInstruction());
		BeanCls relationBean = Beans.get( rel.getDestTable());
		
		if (relationBean.getTbl().getPrimaryKey().isMultiColumn()) {
			Struct pkType=relationBean.getStructPk();
			Var idRemoved = _declare(pkType, "idRemoved");
			for(Column col:relationBean.getTbl().getPrimaryKey().getColumns()) {
				_assign(idRemoved.accessAttr(col
						.getCamelCaseName()), getParam("bean")
						.callAttrGetter(
								col
								.getCamelCaseName()
						));
			}
			addInstr(
					parent.getAttrByName(
							a.getName()+"Removed")
							.callMethod("append",
									idRemoved
								).asInstruction());	
				
		} else {
			addInstr(
					parent.getAttrByName(
							a.getName()+"Removed")
							.callMethod("append",
									getParam("bean")
									.callAttrGetter(
											relationBean.getTbl().getPrimaryKey().getFirstColumn()
											.getCamelCaseName()
									)
								).asInstruction());	
		}
	}

}
