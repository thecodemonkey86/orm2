package cpp.entity;

import cpp.core.Attr;
import cpp.core.TplCls;
import cpp.core.Type;
import cpp.core.expression.Expressions;
import cpp.lib.ClsQVector;
import cpp.orm.OrmUtil;
import database.relation.ManyRelation;
import database.relation.OneToManyRelation;

public class ManyAttr extends Attr{
	public ManyAttr(OneToManyRelation relation) {
		super(Attr.Protected, new ClsQVector(Entities.get(relation.getDestTable().getUc1stCamelCaseName()).toSharedPtr()), 
				OrmUtil.getOneToManyRelationDestAttrName(relation)
				, Expressions.Nullptr, false);
		setMutableModifier();
	}
	
	public ManyAttr(ManyRelation relation) {
		super(Attr.Protected, new ClsQVector(Entities.get(relation.getDestTable().getUc1stCamelCaseName()).toSharedPtr()), 
				OrmUtil.getManyRelationDestAttrName(relation)
				, Expressions.Nullptr, false);
		setMutableModifier();
	}
	
	public Type getElementType() {
		return ((TplCls )type).getElementType();
	}
}
