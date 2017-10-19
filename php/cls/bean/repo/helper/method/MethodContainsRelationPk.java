package php.cls.bean.repo.helper.method;

import model.AbstractRelation;
import php.PhpFunctions;
import php.Types;
import php.cls.Method;
import php.cls.Param;
import php.cls.bean.repo.helper.FetchListHelperClass;
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
