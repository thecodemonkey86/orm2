package cpp.cls.bean;

import pg.PgCppUtil;
import model.OneRelation;
import cpp.cls.Attr;
import cpp.cls.SharedPtr;
import cpp.cls.Type;
import cpp.cls.expression.Expressions;

public class OneAttr extends Attr {
	protected OneRelation relation;
	
	public OneAttr(OneRelation relation) {
		super(Attr.Protected, Beans.get(relation.getDestTable().getUc1stCamelCaseName()).toSharedPtr(),
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
