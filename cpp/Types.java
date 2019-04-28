package cpp;

import java.util.HashMap;

import cpp.bean.BeanCls;
import cpp.bean.Beans;
import cpp.bean.Nullable;
import cpp.beanquery.ClsBeanQueryDelete;
import cpp.beanquery.ClsBeanQuerySelect;
import cpp.beanquery.ClsBeanQueryUpdate;
import cpp.beanrepository.ClsBeanRepository;
import cpp.core.Type;
import cpp.jsonentity.JsonEntities;
import cpp.jsonentity.JsonEntity;
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
	
	private static HashMap<String, ClsBeanQuerySelect> selectQueryClasses = new HashMap<>();
	private static HashMap<String, ClsBeanQueryDelete> deleteQueryClasses = new HashMap<>();
	private static HashMap<String, ClsBeanQueryUpdate> updateQueryClasses = new HashMap<>();
	
	public static ClsBeanQuerySelect beanQuerySelect(BeanCls cls) {
		
		
		if(selectQueryClasses.containsKey(cls.getName())) {
			return selectQueryClasses.get(cls.getName());
		}
		ClsBeanQuerySelect s= new ClsBeanQuerySelect(cls); //
		selectQueryClasses.put(cls.getName(), s);
		return s;
	}

	
	
	public static ClsBeanQueryDelete beanQueryDelete(BeanCls cls) {
		
		
		if(deleteQueryClasses.containsKey(cls.getName())) {
			return deleteQueryClasses.get(cls.getName());
		}
		ClsBeanQueryDelete d= new ClsBeanQueryDelete(cls); //
		deleteQueryClasses.put(cls.getName(), d);
		return d;
	}
	
	public static ClsBeanQueryUpdate beanQueryUpdate(BeanCls cls) {
		
		
		if(updateQueryClasses.containsKey(cls.getName())) {
			return updateQueryClasses.get(cls.getName());
		}
		ClsBeanQueryUpdate u= new ClsBeanQueryUpdate(cls); //
		updateQueryClasses.put(cls.getName(), u);
		return u;
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
	
	public static Type getRelationForeignPrimaryKeyTypeJsonEntities(AbstractRelation r) {
		Type beanPk = null;
		if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
			beanPk = JsonEntities.get(r.getDestTable().getUc1stCamelCaseName()).getStructPk();
			
		} else {
			beanPk =JsonEntity.getDatabaseMapper().columnToType(r.getDestTable().getPrimaryKey().getColumns().get(0));
		}
		return beanPk;
		
	}
	
	public static ClsOrderedSet orderedSet(Type elementType) {
		return new ClsOrderedSet(elementType);
	}
	
	
}
