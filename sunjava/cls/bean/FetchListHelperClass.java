package sunjava.cls.bean;

import java.util.ArrayList;
import java.util.List;

import sunjava.cls.method.MethodAttributeGetter;
import sunjava.cls.method.MethodAttributeSetter;
import model.AbstractRelation;
import sunjava.Types;
import sunjava.cls.Attr;
import sunjava.cls.JavaCls;
import sunjava.cls.Type;
import sunjava.cls.bean.method.FetchListHelperConstructor;

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
