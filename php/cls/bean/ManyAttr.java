package php.cls.bean;

import php.Types;
import php.cls.Attr;
import php.cls.PhpPseudoGenericClass;
import php.cls.Type;
import php.cls.expression.Expressions;
import php.orm.OrmUtil;
import model.ManyRelation;
import model.OneToManyRelation;

public class ManyAttr extends Attr{
	public ManyAttr(OneToManyRelation relation) {
		super(Attr.Protected, Types.array(Beans.get(relation.getDestTable().getUc1stCamelCaseName())), 
				OrmUtil.getOneToManyRelationDestAttrName(relation)
				, Expressions.Null, false);
	}
	
	public ManyAttr(ManyRelation relation) {
		super(Attr.Protected, Types.array(Beans.get(relation.getDestTable().getUc1stCamelCaseName())), 
				OrmUtil.getManyRelationDestAttrName(relation)
				, Expressions.Null, false);
	}
	
	public Type getElementType() {
		return ((PhpPseudoGenericClass )type).getElementType();
	}
}
