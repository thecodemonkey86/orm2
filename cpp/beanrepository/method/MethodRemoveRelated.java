package cpp.beanrepository.method;

import cpp.Types;
import cpp.bean.BeanCls;
import cpp.core.Method;
import database.relation.OneToManyRelation;
import util.StringUtil;

public class MethodRemoveRelated extends Method{

	public MethodRemoveRelated(BeanCls bean, OneToManyRelation r) {
		super(Public, Types.Void, "removeRelated"+r.getDestTable()+StringUtil.ucfirst(r.getAlias()));
		
	}

	@Override
	public void addImplementation() {
		//_callMethodInstr(_this().accessAttr("sqlCon"), "execute", QStringLiteral.fromStringConstant(sql), varParams);		
	}
	
}
