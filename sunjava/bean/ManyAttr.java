package sunjava.bean;

import database.relation.ManyRelation;
import database.relation.OneToManyRelation;
import sunjava.core.Attr;
import sunjava.core.JavaGenericClass;
import sunjava.core.Type;
import sunjava.core.Types;
import sunjava.core.expression.Expressions;
import sunjava.orm.OrmUtil;

public class ManyAttr extends Attr{
	public ManyAttr(OneToManyRelation relation) {
		super(Attr.Protected, Types.linkedHashSet(Beans.get(relation.getDestTable().getUc1stCamelCaseName())), 
				OrmUtil.getOneToManyRelationDestAttrName(relation)
				, Expressions.Null, false);
	}
	
	public ManyAttr(ManyRelation relation) {
		super(Attr.Protected, Types.linkedHashSet(Beans.get(relation.getDestTable().getUc1stCamelCaseName())), 
				OrmUtil.getManyRelationDestAttrName(relation)
				, Expressions.Null, false);
	}
	
	public Type getElementType() {
		return ((JavaGenericClass )type).getElementType();
	}
}
