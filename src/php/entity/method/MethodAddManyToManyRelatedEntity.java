package php.entity.method;

import database.relation.ManyRelation;
import php.core.Attr;
import php.core.Param;
import php.core.PhpCls;
import php.core.Types;
import php.core.expression.ArrayInitExpression;
import php.core.method.Method;
import php.orm.OrmUtil;

public class MethodAddManyToManyRelatedEntity extends Method {

	protected ManyRelation rel;
	
	public MethodAddManyToManyRelatedEntity(ManyRelation r, Param p) {
		super(Public, Types.Void, OrmUtil.getAddRelatedEntityMethodName(r));
		addParam(p);
		rel=r;
	}

	@Override
	public void addImplementation() {
		PhpCls parent = (PhpCls) this.parent;
		Attr a=parent.getAttrByName(OrmUtil.getManyRelationDestAttrName(rel));
		_if(a.isNull()).addIfInstr(a.assign(new ArrayInitExpression()));
		Param pEntity = getParam("entity");
		if(rel.getDestTable().getPrimaryKey().isMultiColumn()) {
			throw new RuntimeException("unimplemented");
		} else {
			addInstr(a.arrayIndexSet(pEntity.callAttrGetter(rel.getDestTable().getPrimaryKey().getFirstColumn().getCamelCaseName()),pEntity));
		}
		Attr aAdded = parent.getAttrByName(a.getName()+"Added"
				);
		_if(aAdded.isNull()).addIfInstr(aAdded.assign(new ArrayInitExpression()));
		addInstr(
				aAdded.arrayPush(pEntity));
	}

}
