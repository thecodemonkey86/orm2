package cpp;

import java.util.HashMap;

import cpp.core.Optional;
import cpp.core.Type;
import cpp.entity.Entities;
import cpp.entity.EntityCls;
import cpp.entityquery.ClsEntityQueryDelete;
import cpp.entityquery.ClsEntityQuerySelect;
import cpp.entityquery.ClsEntityQueryUpdate;
import cpp.entityrepository.ClsEntityRepository;
import cpp.jsonentity.JsonEntities;
import cpp.jsonentity.JsonEntity;
import cpp.lib.ClsBaseEntity;
import cpp.lib.ClsFirebirdSqlQuery;
import cpp.lib.ClsMySqlQuery;
import cpp.lib.ClsSql;
import cpp.lib.ClsSqlQuery;
import cpp.lib.ClsSqlUtil;
import cpp.lib.ClsSqliteSqlQuery;
import cpp.lib.ClsPgSqlQuery;
import cpp.lib.EnumSqlQueryOrderDirection;
import database.relation.AbstractRelation;


public class Types extends CoreTypes{
	
	
	
	public static final ClsSqlQuery SqlQuery = 	new ClsSqlQuery();
	public static final ClsPgSqlQuery PgSqlQuery = 	new ClsPgSqlQuery();
	public static final ClsMySqlQuery MySqlQuery = 	new ClsMySqlQuery();
	public static final ClsFirebirdSqlQuery FirebirdSqlQuery = 	new ClsFirebirdSqlQuery();
	public static final ClsSqliteSqlQuery SqliteSqlQuery = 	new ClsSqliteSqlQuery();
	public static final ClsSql Sql = 	new ClsSql();
	public static final ClsSqlUtil SqlUtil = 	new ClsSqlUtil();
	public static final ClsEntityRepository EntityRepository = new ClsEntityRepository();
	public static final ClsBaseEntity BaseEntity = new ClsBaseEntity();
	public static final EnumSqlQueryOrderDirection OrderDirection = EnumSqlQueryOrderDirection.INSTANCE;
	
	private static HashMap<String, ClsEntityQuerySelect> selectQueryClasses = new HashMap<>();
	private static HashMap<String, ClsEntityQueryDelete> deleteQueryClasses = new HashMap<>();
	private static HashMap<String, ClsEntityQueryUpdate> updateQueryClasses = new HashMap<>();
	
	public static ClsEntityQuerySelect entityQuerySelect(EntityCls cls) {
		
		
		if(selectQueryClasses.containsKey(cls.getName())) {
			return selectQueryClasses.get(cls.getName());
		}
		ClsEntityQuerySelect s= new ClsEntityQuerySelect(cls); //
		selectQueryClasses.put(cls.getName(), s);
		return s;
	}

	
	
	public static ClsEntityQueryDelete entityQueryDelete(EntityCls cls) {
		
		
		if(deleteQueryClasses.containsKey(cls.getName())) {
			return deleteQueryClasses.get(cls.getName());
		}
		ClsEntityQueryDelete d= new ClsEntityQueryDelete(cls); //
		deleteQueryClasses.put(cls.getName(), d);
		return d;
	}
	
	public static ClsEntityQueryUpdate entityQueryUpdate(EntityCls cls) {
		
		
		if(updateQueryClasses.containsKey(cls.getName())) {
			return updateQueryClasses.get(cls.getName());
		}
		ClsEntityQueryUpdate u= new ClsEntityQueryUpdate(cls); //
		updateQueryClasses.put(cls.getName(), u);
		return u;
	}
	
	
	public static Type getRelationForeignPrimaryKeyType(AbstractRelation r) {
		Type entityPk = null;
		if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
			entityPk = Entities.get(r.getDestTable().getUc1stCamelCaseName()).getStructPk();
			
		} else {
			entityPk =EntityCls.getDatabaseMapper().columnToType(r.getDestTable().getPrimaryKey().getColumns().get(0));
		}
		return entityPk;
		
	}
	
	public static Type getRelationForeignPrimaryKeyTypeJsonEntities(AbstractRelation r) {
		Type entityPk = null;
		if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
			entityPk = JsonEntities.get(r.getDestTable().getUc1stCamelCaseName()).getStructPk();
			
		} else {
			entityPk =JsonEntity.getDatabaseMapper().columnToType(r.getDestTable().getPrimaryKey().getColumns().get(0));
		}
		return entityPk;
		
	}



	public static Optional optional(Type t) {
		return new Optional(t);
	}
	
	
	
}
