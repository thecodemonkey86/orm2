package sunjava.bean;

import java.util.ArrayList;
import java.util.List;

import database.relation.AbstractRelation;
import sunjava.bean.method.FetchListHelperConstructor;
import sunjava.core.Attr;
import sunjava.core.JavaCls;
import sunjava.core.Type;
import sunjava.core.Types;
import sunjava.core.method.MethodAttributeGetter;
import sunjava.core.method.MethodAttributeSetter;

public class FetchListHelperClass extends JavaCls{

	public FetchListHelperClass(BeanCls bean) {
		super(bean.getName()+"FetchListHelper",bean.getPackage()+".helper");
		Attr b1 = new Attr(bean, "b1");
		addAttr(b1);

		addMethod(new MethodAttributeSetter(b1));
		addMethod(new MethodAttributeGetter(b1));
		List<AbstractRelation> manyRelations = new ArrayList<>();
		manyRelations.addAll(bean.getOneToManyRelations());
		manyRelations.addAll(bean.getManyToManyRelations());
		addConstructor(new FetchListHelperConstructor(bean));
		for(AbstractRelation r:manyRelations) {
			Type beanPk=Types.getRelationForeignPrimaryKeyType(r);
			Attr attrSet = new Attr(Types.hashset(beanPk), r.getAlias()+"Set");
			addMethod(new MethodAttributeGetter(attrSet));
			addAttr(attrSet);
		}
	}
	


	
	
}
