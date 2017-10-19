package sunjava.cls.bean;

import sunjava.Types;
import sunjava.cls.Attr;
import sunjava.cls.JavaGenericClass;
import sunjava.cls.Type;
import sunjava.cls.expression.Expressions;
import sunjava.orm.OrmUtil;
import model.ManyRelation;
import model.OneToManyRelation;

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
