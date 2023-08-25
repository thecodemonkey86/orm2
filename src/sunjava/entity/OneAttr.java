package sunjava.entity;

import database.relation.OneRelation;
import sunjava.core.Attr;
import sunjava.core.Type;
import sunjava.core.expression.Expressions;
import util.pg.PgCppUtil;

public class OneAttr extends Attr {
	protected OneRelation relation;
	
	public OneAttr(OneRelation relation) {
		super(Attr.Protected, Entities.get(relation.getDestTable().getUc1stCamelCaseName()),
				PgCppUtil.getOneRelationDestAttrName(relation)
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
