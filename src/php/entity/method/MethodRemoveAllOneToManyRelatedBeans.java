package php.entity.method;

import database.column.Column;
import database.relation.OneToManyRelation;
import php.core.Attr;
import php.core.PhpCls;
import php.core.Type;
import php.core.Types;
import php.core.expression.ArrayInitExpression;
import php.core.expression.Expression;
import php.core.expression.Var;
import php.core.instruction.ForeachLoop;
import php.core.method.Method;
import php.entity.Entities;
import php.entity.EntityCls;
import php.orm.OrmUtil;
import util.StringUtil;

public class MethodRemoveAllOneToManyRelatedBeans extends Method {

	protected OneToManyRelation rel;

	public MethodRemoveAllOneToManyRelatedBeans(OneToManyRelation r) {
		super(Public, Types.Void, "removeAll" + StringUtil.ucfirst(OrmUtil.getOneToManyRelationDestAttrName(r)));
		rel = r;
	}

	@Override
	public void addImplementation() {
		PhpCls parent = (PhpCls) this.parent;
		
		Attr a = parent.getAttrByName(OrmUtil.getOneToManyRelationDestAttrName(rel));
		Var vRelatedBean = new Var(Entities.get(rel.getDestTable()), "relatedBean");
		 ForeachLoop foreach = _foreach(vRelatedBean, _this().accessAttr(a));
		 EntityCls relationBean = Entities.get(rel.getDestTable());
				
		Attr aRemoved = parent.getAttrByName(a.getName() + "Removed");
		foreach._if(aRemoved.isNull()).addIfInstr(aRemoved.assign(new ArrayInitExpression()));

		if (relationBean.getTbl().getPrimaryKey().isMultiColumn()) {
			Type pkType = relationBean.getPkType();
			Expression[] idAddedArgs = new Expression[relationBean.getTbl().getPrimaryKey().getColumnCount()];

			for (int i = 0; i < idAddedArgs.length; i++) {
				idAddedArgs[i] = vRelatedBean
						.callAttrGetter(relationBean.getTbl().getPrimaryKey().getColumn(i).getCamelCaseName());
			}
			Var idRemoved = foreach._declareNew(pkType, "idRemoved", idAddedArgs);
			for (Column col : relationBean.getTbl().getPrimaryKey().getColumns()) {
				foreach.addInstr(idRemoved.callAttrSetterMethodInstr(col.getCamelCaseName(),
						vRelatedBean.callAttrGetter(col.getCamelCaseName())));
			}
			foreach.addInstr(aRemoved.arrayPush(idRemoved));

		} else {
			foreach.addInstr(aRemoved.arrayPush(
					vRelatedBean.callAttrGetter(relationBean.getTbl().getPrimaryKey().getFirstColumn().getCamelCaseName())));
		}
		_assign(a,new ArrayInitExpression());
	}

}
