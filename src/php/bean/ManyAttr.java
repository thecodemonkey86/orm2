package php.bean;

import database.relation.ManyRelation;
import database.relation.OneToManyRelation;
import php.core.Attr;
import php.core.PhpPseudoGenericClass;
import php.core.Type;
import php.core.Types;
import php.core.expression.Expressions;
import php.orm.OrmUtil;

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
