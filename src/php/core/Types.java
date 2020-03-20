package php.core;

import php.bean.EntityCls;
import php.beanrepository.ClsBeanRepository;
import php.lib.ClsBaseEntity;
import php.lib.ClsBaseEntityQuery;
import php.lib.ClsException;
import php.lib.ClsFirebirdSqlQuery;
import php.lib.ClsMySqlQuery;
import php.lib.ClsMysqli;
import php.lib.ClsMysqliResult;
import php.lib.ClsPgSqlQuery;
import php.lib.ClsSql;
import php.lib.ClsSqlParam;
import php.lib.ClsSqlQuery;
import php.lib.ClsSqlUtil;


public class Types extends CoreTypes{
	public static final Resource Resource = new Resource();
	
	public static final ClsMysqli mysqli = new ClsMysqli();
	public static final ClsMysqliResult mysqli_result = new ClsMysqliResult();
	public static final ClsSql Sql = new ClsSql();
	public static final ClsBaseEntity BaseEntity=new ClsBaseEntity();
	public static final ClsSqlQuery SqlQuery = new ClsSqlQuery();
	public static final ClsPgSqlQuery PgSqlQuery = new ClsPgSqlQuery();
	public static final ClsMySqlQuery MysqlSqlQuery = new ClsMySqlQuery();
	public static final ClsFirebirdSqlQuery FirebirdSqlQuery = new ClsFirebirdSqlQuery();
	
	public static final ClsBeanRepository BeanRepository = new ClsBeanRepository();
	
	public static final ClsSqlParam SqlParam = new ClsSqlParam();
	public static final ClsSqlUtil SqlUtil = new ClsSqlUtil();
	public static final ClsException Exception = new ClsException();
	
	
	public static PhpArray array(Type valueType) {
		return new PhpArray(valueType) ;
	}
	public static PhpArray array(Type keyType, Type valueType) {
		return new PhpArray(keyType, valueType) ;
	}
	public static ClsBaseEntityQuery beanQuery(EntityCls bean) {
		return new ClsBaseEntityQuery(bean);
	}


	
	
}
