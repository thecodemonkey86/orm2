package cpp.entity.method;

import util.StringUtil;
import cpp.Types;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.Struct;
import cpp.core.expression.Var;
import cpp.entity.Entities;
import cpp.entity.EntityCls;
import cpp.orm.OrmUtil;
import database.column.Column;
import database.relation.ManyRelation;

public class MethodRemoveManyToManyRelatedEntity extends Method {

	protected ManyRelation rel;
	
	public MethodRemoveManyToManyRelatedEntity(ManyRelation r, Param p) {
		super(Public, Types.Void, "remove"+StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrNameSingular(r)));
		addParam(p);
		rel=r;
	}

	@Override
	public void addImplementation() {
		Attr a=parent.getAttrByName(OrmUtil.getManyRelationDestAttrName(rel));
		addInstr(a.callMethod("removeOne",getParam("entity")).asInstruction());
		EntityCls relationBean = Entities.get( rel.getDestTable());
		
		if (relationBean.getTbl().getPrimaryKey().isMultiColumn()) {
			Struct pkType=relationBean.getStructPk();
			Var idRemoved = _declare(pkType, "idRemoved");
			for(Column col:relationBean.getTbl().getPrimaryKey().getColumns()) {
				_assign(idRemoved.accessAttr(col
						.getCamelCaseName()), getParam("entity")
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
									getParam("entity")
									.callAttrGetter(
											relationBean.getTbl().getPrimaryKey().getFirstColumn()
											.getCamelCaseName()
									)
								).asInstruction());	
		}
	}

}
