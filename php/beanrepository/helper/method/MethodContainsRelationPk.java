package php.beanrepository.helper.method;

import database.relation.AbstractRelation;
import php.beanrepository.helper.FetchListHelperClass;
import php.core.Param;
import php.core.PhpFunctions;
import php.core.Types;
import php.core.method.Method;
import php.orm.OrmUtil;
import util.StringUtil;

public class MethodContainsRelationPk extends Method {
	AbstractRelation relation;
	
	public MethodContainsRelationPk(AbstractRelation relation) {
		super(Public, Types.Bool, "containsPk" + StringUtil.ucfirst(relation.getAlias()));
		this.relation = relation;
		addParam(new Param(OrmUtil.getRelationForeignPrimaryKeyType(relation), "pk"));
	}

	@Override
	public void addImplementation() {
		FetchListHelperClass parent = (FetchListHelperClass) this.parent;
		Param paramPk = getParam("pk");
		_return(parent.getAttrByName(relation.getAlias()+"Set").arrayIndexIsset(relation.getDestTable().getPrimaryKey().getColumnCount() == 1 ? paramPk : PhpFunctions.spl_object_hash.call(paramPk)));
		
	}

}
