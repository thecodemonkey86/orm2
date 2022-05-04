package php.entity;

import database.relation.OneRelation;
import php.core.Attr;
import php.core.Type;
import php.core.expression.Expressions;
import php.orm.OrmUtil;

public class OneAttr extends Attr {
	protected OneRelation relation;
	
	public OneAttr(OneRelation relation) {
		super(Attr.Protected, Entities.get(relation.getDestTable().getUc1stCamelCaseName()).toNullable(),
				OrmUtil.getOneRelationDestAttrName(relation)
				, Expressions.Null, false);
		
		this.relation = relation;
	}
	
	public Type getElementType() {
		return type;
	}

	public OneRelation getRelation() {
		return relation;
	}
}
