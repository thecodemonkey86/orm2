package php;

import php.cls.PrimitiveType;
import php.cls.Type;
import php.cls.bean.repo.ClsBeanRepository;
import php.lib.ClsBaseBean;
import php.lib.ClsDateTime;
import php.lib.ClsMySqlQuery;
import php.lib.ClsMysqli;
import php.lib.ClsMysqliResult;
import php.lib.ClsPgSqlQuery;
import php.lib.ClsSql;
import php.lib.ClsSqlParam;
import php.lib.ClsSqlQuery;
import php.lib.ClsSqlUtil;
import php.lib.PhpStringType;


public class Types {
	
	public static final PrimitiveType Int=new PrimitiveType("int") {
		@Override
		public String getSprintfType() {
			return "%d";
		}
	};
	public static final PrimitiveType Short=new PrimitiveType("short");
	//public static final PrimitiveType Char= new PrimitiveType("char");
	public static final PrimitiveType Bool = new PrimitiveType("bool");
	public static final PrimitiveType Float = new PrimitiveType("float") {
		@Override
		public String getSprintfType() {
			return "%f";
		}
	};
	public static final PrimitiveType Void = new PrimitiveType("void");
	public static final PrimitiveType Uint = new PrimitiveType("uint");
	public static final PhpStringType String = new PhpStringType();
	public static final Type Mixed = new Type("mixed");
	public static final ClsDateTime DateTime = new ClsDateTime();
	public static final ClsMysqli mysqli = new ClsMysqli();
	public static final ClsMysqliResult mysqli_result = new ClsMysqliResult();
	public static final ClsSql Sql = new ClsSql();
	public static final ClsBaseBean BaseBean=new ClsBaseBean();
	public static final ClsSqlQuery SqlQuery = new ClsSqlQuery();
	public static final ClsPgSqlQuery PgSqlQuery = new ClsPgSqlQuery();
	public static final ClsMySqlQuery MysqlSqlQuery = new ClsMySqlQuery();
	
	public static final ClsBeanRepository BeanRepository = new ClsBeanRepository();
	
	public static final ClsSqlParam SqlParam = new ClsSqlParam();
	public static final ClsSqlUtil SqlUtil = new ClsSqlUtil();
	
	public static PhpArray array(Type valueType) {
		return new PhpArray(valueType) ;
	}
	public static PhpArray array(Type keyType, Type valueType) {
		return new PhpArray(keyType, valueType) ;
	}
	public static Type nullable(Type i) {
		// TODO Auto-generated method stub
		return null;
	}
	


	
	
}
