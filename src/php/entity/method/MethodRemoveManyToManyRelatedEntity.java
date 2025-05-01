package php.entity.method;

import database.column.Column;
import database.relation.ManyRelation;
import php.core.Attr;
import php.core.Param;
import php.core.PhpCls;
import php.core.Type;
import php.core.Types;
import php.core.expression.ArrayInitExpression;
import php.core.expression.Expression;
import php.core.expression.Var;
import php.core.method.Method;
import php.entity.Entities;
import php.entity.EntityCls;
import php.orm.OrmUtil;
import util.StringUtil;

public class MethodRemoveManyToManyRelatedEntity extends Method {

	protected ManyRelation rel;

	public MethodRemoveManyToManyRelatedEntity(ManyRelation r, Param p) {
		super(Public, Types.Void, "remove" + StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrNameSingular(r)));
		addParam(p);
		rel = r;
	}

	@Override
	public void addImplementation() {
		PhpCls parent = (PhpCls) this.parent;
		Param pEntity = getParam("entity");
		Attr a = parent.getAttrByName(OrmUtil.getManyRelationDestAttrName(rel));
		EntityCls relationEntity = Entities.get(rel.getDestTable());
		
		if (relationEntity.getTbl().getPrimaryKey().isMultiColumn()) {
			throw new RuntimeException("not impl");
		} else {
			addInstr(a.arrayUnset(pEntity.callAttrGetter(relationEntity.getTbl().getPrimaryKey().getFirstColumn().getCamelCaseName())));
		}
		
		
		Attr aRemoved = parent.getAttrByName(a.getName() + "Removed");
		_if(aRemoved.isNull()).addIfInstr(aRemoved.assign(new ArrayInitExpression()));

		if (relationEntity.getTbl().getPrimaryKey().isMultiColumn()) {
			Type pkType = relationEntity.getPkType();
			Expression[] idAddedArgs = new Expression[relationEntity.getTbl().getPrimaryKey().getColumnCount()];

			for (int i = 0; i < idAddedArgs.length; i++) {
				idAddedArgs[i] = pEntity
						.callAttrGetter(relationEntity.getTbl().getPrimaryKey().getColumn(i).getCamelCaseName());
			}
			Var idRemoved = _declareNew(pkType, "idRemoved", idAddedArgs);
			for (Column col : relationEntity.getTbl().getPrimaryKey().getColumns()) {
				addInstr(idRemoved.callAttrSetterMethodInstr(col.getCamelCaseName(),
						pEntity.callAttrGetter(col.getCamelCaseName())));
			}
			addInstr(aRemoved.arrayPush(idRemoved));

		} else {
			addInstr(aRemoved.arrayPush(
					pEntity.callAttrGetter(relationEntity.getTbl().getPrimaryKey().getFirstColumn().getCamelCaseName())));
		}
	}

}
