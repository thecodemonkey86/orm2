package php.entityrepository.helper.method;

import database.relation.AbstractRelation;
import php.core.Param;
import php.core.Types;
import php.core.method.Method;
import php.entityrepository.helper.FetchListHelperClass;
import php.orm.OrmUtil;
import util.StringUtil;

public class MethodAddRelationPk extends Method {
	AbstractRelation relation;
	
	public MethodAddRelationPk(AbstractRelation relation) {
		super(Public, Types.Void, "addPk" + StringUtil.ucfirst(relation.getAlias()));
		this.relation = relation;
		addParam(new Param(OrmUtil.getRelationForeignPrimaryKeyType(relation), "pk"));
	}

	@Override
	public void addImplementation() {
		FetchListHelperClass parent = (FetchListHelperClass) this.parent;
		addInstr( parent.getAttrByName(relation.getAlias()+"Set").arrayPush(getParam("pk")));
	}

}
