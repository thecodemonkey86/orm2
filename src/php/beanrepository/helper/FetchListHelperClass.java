package php.beanrepository.helper;

import java.util.ArrayList;
import java.util.List;

import database.relation.AbstractRelation;
import php.core.Attr;
import php.core.PhpCls;
import php.core.Type;
import php.core.Types;
import php.core.method.MethodAttributeGetter;
import php.core.method.MethodAttributeSetter;
import php.orm.OrmUtil;
import php.bean.EntityCls;
import php.bean.method.FetchListHelperConstructor;
import php.beanrepository.helper.method.MethodAddRelationPk;
import php.beanrepository.helper.method.MethodContainsRelationPk;

public class FetchListHelperClass extends PhpCls{

	public FetchListHelperClass(EntityCls bean, String repositoryNamespace) {
		super(bean.getName()+"FetchListHelper",repositoryNamespace+"\\Helper");
		Attr e1 = new Attr(bean, "e1");
		addAttr(e1);

		addMethod(new MethodAttributeSetter(e1));
		addMethod(new MethodAttributeGetter(e1));
		List<AbstractRelation> manyRelations = new ArrayList<>();
		manyRelations.addAll(bean.getOneToManyRelations());
		manyRelations.addAll(bean.getManyToManyRelations());
		setConstructor(new FetchListHelperConstructor(bean));
		for(AbstractRelation r:manyRelations) {
			Type beanPk=OrmUtil.getRelationForeignPrimaryKeyType(r);
			Attr attrSet = new Attr(Types.array(Types.String, beanPk), r.getAlias()+"Set");
		//	addMethod(new MethodAttributeGetter(attrSet));
			addMethod(new MethodAddRelationPk(r));
			addMethod(new MethodContainsRelationPk(r));
			addAttr(attrSet);
		}
		
	//	addMethod(new );
	}
	


	
	
}
