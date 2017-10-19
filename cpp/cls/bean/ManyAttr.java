package cpp.cls.bean;

import cpp.cls.Attr;
import cpp.cls.TplCls;
import cpp.cls.Type;
import cpp.cls.expression.Expressions;
import cpp.lib.ClsQVector;
import cpp.orm.OrmUtil;
import model.ManyRelation;
import model.OneToManyRelation;

public class ManyAttr extends Attr{
	public ManyAttr(OneToManyRelation relation) {
		super(Attr.Protected, new ClsQVector(Beans.get(relation.getDestTable().getUc1stCamelCaseName()).toSharedPtr()), 
				OrmUtil.getOneToManyRelationDestAttrName(relation)
				, Expressions.Nullptr, false);
		setMutableModifier();
	}
	
	public ManyAttr(ManyRelation relation) {
		super(Attr.Protected, new ClsQVector(Beans.get(relation.getDestTable().getUc1stCamelCaseName()).toSharedPtr()), 
				OrmUtil.getManyRelationDestAttrName(relation)
				, Expressions.Nullptr, false);
		setMutableModifier();
	}
	
	public Type getElementType() {
		return ((TplCls )type).getElementType();
	}
}
