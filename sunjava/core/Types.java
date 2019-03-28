package sunjava.core;

import database.relation.AbstractRelation;
import sunjava.bean.BeanCls;
import sunjava.bean.Beans;
import sunjava.beanrepository.ClsBeanRepository;
import sunjava.lib.ClsArrayList;
import sunjava.lib.ClsBaseBean;
import sunjava.lib.ClsJavaString;
import sunjava.lib.ClsLinkedHashSet;
import sunjava.lib.ClsHashSet;
import sunjava.lib.ClsLocalDate;
import sunjava.lib.ClsLocalDateTime;
import sunjava.lib.ClsPgSqlQuery;
import sunjava.lib.ClsConnection;
import sunjava.lib.ClsResultSet;
import sunjava.lib.ClsSqlDate;
import sunjava.lib.ClsSqlException;
import sunjava.lib.ClsSqlParam;
import sunjava.lib.ClsSqlQuery;
import sunjava.lib.ClsSqlUtil;
import sunjava.lib.ClsTimestamp;
import sunjava.lib.ClsZonedDateTime;
import sunjava.lib.StaticLibMethod;


public class Types {
	
	
	private static final String HASH_CODE = "hashCode";
	public static final PrimitiveType Int=new PrimitiveType("int") {
		@Override
		public JavaCls getAutoBoxingClass() {
			return IntAutoBoxing;
		}
	};
	public static final PrimitiveType Byte=new PrimitiveType("byte") {
		@Override
		public JavaCls getAutoBoxingClass() {
			return ByteAutoBoxing;
		}
		
	};
	public static final PrimitiveType Long=new PrimitiveType("long") {

		@Override
		public JavaCls getAutoBoxingClass() {
			return LongAutoBoxing;
		}
		
	};
	public static final PrimitiveType Short=new PrimitiveType("short") {

		@Override
		public JavaCls getAutoBoxingClass() {
			return ShortAutoBoxing;
		}
		
	};
	public static final PrimitiveType Char= new PrimitiveType("char") {

		@Override
		public JavaCls getAutoBoxingClass() {
			return CharAutoBoxing;
		}
		
	};
	public static final PrimitiveType Bool = new PrimitiveType("boolean") {

		@Override
		public JavaCls getAutoBoxingClass() {
			return BoolAutoBoxing;
		}
		
	};
	public static final PrimitiveType Double = new PrimitiveType("double") {

		@Override
		public JavaCls getAutoBoxingClass() {
			return DoubleAutoBoxing;
		}
		
	};
	public static final PrimitiveType Void = new PrimitiveType("void") {

		@Override
		public JavaCls getAutoBoxingClass() {
			return VoidAutoBoxing;
		}
		
	};
	public static final Type NullType = new Type("null");
	
	public static final JavaCls IntAutoBoxing=new JavaCls("Integer","java.lang");
	
	
	public static final JavaCls ByteAutoBoxing=new JavaCls("Byte","java.lang");
	
	
	public static final JavaCls LongAutoBoxing=new JavaCls("Long","java.lang");
	
	
	public static final JavaCls ShortAutoBoxing=new JavaCls("Short","java.lang");
	
	public static final ClsJavaString String= new ClsJavaString();
	
	public static final JavaCls CharAutoBoxing= new JavaCls("Character","java.lang");
	
	
	public static final JavaCls BoolAutoBoxing = new JavaCls("Boolean","java.lang");
	
	
	public static final JavaCls DoubleAutoBoxing = new JavaCls("Double","java.lang");
	
	
	public static final ClsZonedDateTime ZonedDateTime = new ClsZonedDateTime(); 
	public static final ClsLocalDate LocalDate = new ClsLocalDate(); 
	public static final ClsLocalDateTime LocalDateTime = new ClsLocalDateTime(); 
	
	public static final JavaArray ByteArray = new JavaArray(Types.Byte);
	
	public static final JavaCls VoidAutoBoxing = new JavaCls("Void", "java.lang");
	public static final JavaCls Object = new JavaCls("Object", "java.lang");
	public static final JavaCls Instant = new JavaCls("Instant", "java.time");
	public static final ClsTimestamp Timestamp = new ClsTimestamp();
	public static final ClsSqlDate SqlDate = new ClsSqlDate();
	public static final ClsResultSet ResultSet = new ClsResultSet();
	public static final ClsConnection Connection = 	new ClsConnection();
	public static final ClsSqlQuery SqlQuery = 	new ClsSqlQuery();
	public static final ClsPgSqlQuery PgSqlQuery = 	new ClsPgSqlQuery();
	
	public static final ClsBaseBean BaseBean = new ClsBaseBean();
	public static final ClsBeanRepository BeanRepository = new ClsBeanRepository();
	public static final ClsSqlParam SqlParam = new ClsSqlParam();
	public static final ClsSqlUtil SqlUtil = new ClsSqlUtil();
	public static final ClsSqlException SqlException = new ClsSqlException();
	
	

	
	public static ClsArrayList arraylist(Type element) {
		return new ClsArrayList(element);
	}
	public static JavaArray array(Type element) {
		return new JavaArray(element);
	}
	public static JavaGenericClass hashset(Type element) {
		return new ClsHashSet(element);
	}
	public static Type nullable(Type element) {
		if (element instanceof PrimitiveType) {
			return ((PrimitiveType)element).getAutoBoxingClass();
		}
		return element;
	}
	
	public static Type getRelationForeignPrimaryKeyType(AbstractRelation r) {
		Type beanPk = null;
		if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
			beanPk = Beans.get(r.getDestTable().getUc1stCamelCaseName()).getPkType();
			
		} else {
			beanPk = BeanCls.getTypeMapper().columnToType( r.getDestTable().getPrimaryKey().getColumns().get(0));
		}
		return beanPk;
	}
	
	
	public static ClsLinkedHashSet linkedHashSet(Type elementType) {
		return new ClsLinkedHashSet(elementType);
	}

	
	static {
		Types.String.addDeclarations();
		Types.LongAutoBoxing.addMethod(new StaticLibMethod(Int, HASH_CODE));
		Types.IntAutoBoxing.addMethod(new StaticLibMethod(Int, HASH_CODE));
		Types.ShortAutoBoxing.addMethod(new StaticLibMethod(Int, HASH_CODE));
		Types.DoubleAutoBoxing.addMethod(new StaticLibMethod(Int, HASH_CODE));
		Types.CharAutoBoxing.addMethod(new StaticLibMethod(Int, HASH_CODE));
		Types.ByteAutoBoxing.addMethod(new StaticLibMethod(Int, HASH_CODE));
	}
	
//	public static <T> JavaCls fromReflectionClass(Class<T> cls) {
//		JavaCls result = new JavaCls(cls.getSimpleName(), cls.getPackage().getName());
//		for(java.lang.reflect.Method m : cls.getMethods()) {
//			result.addMethod(new LibMethod(returnType, name));
//		}
//		return result;
//	}
	
}
