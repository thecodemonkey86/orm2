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

public class MethodAddManyToManyRelatedBean extends Method {

	protected ManyRelation rel;
	
	public MethodAddManyToManyRelatedBean(ManyRelation r, Param p) {
		super(Public, Types.Void, "add"+StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrNameSingular(r)));
		addParam(p);
		rel=r;
	}

	@Override
	public void addImplementation() {
		Attr a=parent.getAttrByName(OrmUtil.getManyRelationDestAttrName(rel));
		addInstr(a.callMethod("append",getParam("bean")).asInstruction());
		BeanCls relationBean = Beans.get( rel.getDestTable());
		
		if (relationBean.getTbl().getPrimaryKey().isMultiColumn()) {
			Struct pkType=relationBean.getStructPk();
			Var idAdded = _declare(pkType, "idAdded");
			for(Column col:relationBean.getTbl().getPrimaryKey().getColumns()) {
				_assign(idAdded.accessAttr(col
						.getCamelCaseName()), getParam("bean")
						.callAttrGetter(
								col
								.getCamelCaseName()
						));
			}
			addInstr(
					parent.getAttrByName(
							a.getName()+"Added")
							.callMethod("append",
									idAdded
								).asInstruction());	
				
		} else {
			addInstr(
					parent.getAttrByName(
							a.getName()+"Added")
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
