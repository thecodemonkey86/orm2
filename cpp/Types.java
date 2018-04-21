package cpp;

import cpp.bean.BeanCls;
import cpp.bean.Beans;
import cpp.bean.Nullable;
import cpp.beanquery.ClsBeanQuery;
import cpp.beanrepository.ClsBeanRepository;
import cpp.core.Cls;
import cpp.core.Type;
import cpp.lib.ClsBaseBean;
import cpp.lib.ClsOrderedSet;
import cpp.lib.ClsSql;
import cpp.lib.ClsSqlQuery;
import cpp.lib.EnumSqlQueryOrderDirection;
import database.relation.AbstractRelation;


public class Types extends CoreTypes{
	
	
	
	public static final ClsSqlQuery SqlQuery = 	new ClsSqlQuery();
	public static final ClsSql Sql = 	new ClsSql();
	public static final ClsBeanRepository BeanRepository = new ClsBeanRepository();
	public static final ClsBaseBean BaseBean = new ClsBaseBean();
	public static final EnumSqlQueryOrderDirection OrderDirection = EnumSqlQueryOrderDirection.INSTANCE;
	
	public static Cls beanQuery(BeanCls cls) {
		return new ClsBeanQuery(cls); //
	}

	public static Type nullable(Type element) {
		return new Nullable( element);
	}
	
	public static Type getRelationForeignPrimaryKeyType(AbstractRelation r) {
		Type beanPk = null;
		if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
			beanPk = Beans.get(r.getDestTable().getUc1stCamelCaseName()).getStructPk();
			
		} else {
			beanPk =BeanCls.getDatabaseMapper().columnToType(r.getDestTable().getPrimaryKey().getColumns().get(0));
		}
		return beanPk;
		
	}
	
	
	public static ClsOrderedSet orderedSet(Type elementType) {
		return new ClsOrderedSet(elementType);
	}
	
	
}
