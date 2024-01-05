package sunjava.entity;

import java.util.ArrayList;
import java.util.List;

import database.relation.AbstractRelation;
import sunjava.core.Attr;
import sunjava.core.JavaCls;
import sunjava.core.Type;
import sunjava.core.Types;
import sunjava.core.method.MethodAttributeGetter;
import sunjava.core.method.MethodAttributeSetter;
import sunjava.entity.method.FetchListHelperConstructor;

public class FetchListHelperClass extends JavaCls{

	public FetchListHelperClass(EntityCls entity) {
		super(entity.getName()+"FetchListHelper",entity.getPackage()+".helper");
		Attr e1 = new Attr(entity, "e1");
		addAttr(e1);

		addMethod(new MethodAttributeSetter(e1));
		addMethod(new MethodAttributeGetter(e1));
		List<AbstractRelation> manyRelations = new ArrayList<>();
		manyRelations.addAll(entity.getOneToManyRelations());
		manyRelations.addAll(entity.getManyToManyRelations());
		addConstructor(new FetchListHelperConstructor(entity));
		for(AbstractRelation r:manyRelations) {
			Type beanPk=Types.getRelationForeignPrimaryKeyType(r);
			Attr attrSet = new Attr(Types.hashset(beanPk), r.getAlias()+"Set");
			addMethod(new MethodAttributeGetter(attrSet));
			addAttr(attrSet);
		}
	}
	


	
	
}
