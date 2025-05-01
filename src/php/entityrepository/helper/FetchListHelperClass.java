package php.entityrepository.helper;

import java.util.ArrayList;
import java.util.List;

import database.relation.AbstractRelation;
import php.core.Attr;
import php.core.PhpCls;
import php.core.Type;
import php.core.Types;
import php.core.method.MethodAttributeGetter;
import php.core.method.MethodAttributeSetter;
import php.entity.EntityCls;
import php.entity.method.FetchListHelperConstructor;
import php.entityrepository.helper.method.MethodAddRelationPk;
import php.entityrepository.helper.method.MethodContainsRelationPk;
import php.orm.OrmUtil;

public class FetchListHelperClass extends PhpCls{

	public FetchListHelperClass(EntityCls entity, String repositoryNamespace) {
		super(entity.getName()+"FetchListHelper",repositoryNamespace+"\\Helper");
		Attr e1 = new Attr(entity, "e1");
		addAttr(e1);

		addMethod(new MethodAttributeSetter(e1));
		addMethod(new MethodAttributeGetter(e1));
		List<AbstractRelation> manyRelations = new ArrayList<>();
		manyRelations.addAll(entity.getOneToManyRelations());
		manyRelations.addAll(entity.getManyToManyRelations());
		setConstructor(new FetchListHelperConstructor(entity));
		for(AbstractRelation r:manyRelations) {
			Type entityPk=OrmUtil.getRelationForeignPrimaryKeyType(r);
			Attr attrSet = new Attr(Types.array(Types.String, entityPk), r.getAlias()+"Set");
		//	addMethod(new MethodAttributeGetter(attrSet));
			addMethod(new MethodAddRelationPk(r));
			addMethod(new MethodContainsRelationPk(r));
			addAttr(attrSet);
		}
		
	//	addMethod(new );
	}
	


	
	
}
