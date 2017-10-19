package php.cls.bean.repo.helper.method;

import model.AbstractRelation;
import php.Types;
import php.cls.Method;
import php.cls.Param;
import php.cls.bean.repo.helper.FetchListHelperClass;
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
