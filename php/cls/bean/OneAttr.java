package php.cls.bean;

import pg.PgCppUtil;
import php.cls.Attr;
import php.cls.Type;
import php.cls.expression.Expressions;
import model.OneRelation;

public class OneAttr extends Attr {
	protected OneRelation relation;
	
	public OneAttr(OneRelation relation) {
		super(Attr.Protected, Beans.get(relation.getDestTable().getUc1stCamelCaseName()),
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
