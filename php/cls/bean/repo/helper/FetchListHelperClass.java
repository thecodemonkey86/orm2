package php.cls.bean.repo.helper;

import java.util.ArrayList;
import java.util.List;

import php.cls.method.MethodAttributeGetter;
import php.cls.method.MethodAttributeSetter;
import php.orm.OrmUtil;
import model.AbstractRelation;
import php.Types;
import php.cls.Attr;
import php.cls.PhpCls;
import php.cls.Type;
import php.cls.bean.BeanCls;
import php.cls.bean.method.FetchListHelperConstructor;
import php.cls.bean.repo.helper.method.MethodAddRelationPk;
import php.cls.bean.repo.helper.method.MethodContainsRelationPk;

public class FetchListHelperClass extends PhpCls{

	public FetchListHelperClass(BeanCls bean, String repositoryNamespace) {
		super(bean.getName()+"FetchListHelper",repositoryNamespace+"\\Helper");
		Attr b1 = new Attr(bean, "b1");
		addAttr(b1);

		addMethod(new MethodAttributeSetter(b1));
		addMethod(new MethodAttributeGetter(b1));
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
