package cpp.entity;

import util.pg.PgCppUtil;
import cpp.core.Attr;
import cpp.core.SharedPtr;
import cpp.core.Type;
import cpp.core.expression.Expressions;
import database.relation.OneRelation;

public class OneAttr extends Attr {
	protected OneRelation relation;
	
	public OneAttr(OneRelation relation) {
		super(Attr.Protected, Entities.get(relation.getDestTable().getUc1stCamelCaseName()).toSharedPtr(),
				PgCppUtil.getOneRelationDestAttrName(relation)
				, Expressions.Nullptr, false);
		
		setMutableModifier();
		this.relation = relation;
	}
	
	public Type getElementType() {
		if (type instanceof SharedPtr)
			return ((SharedPtr )type).getElementType();
		return type;
	}

	public OneRelation getRelation() {
		return relation;
	}
}
