package sunjava.entityrepository;

import java.util.Collection;

import sunjava.core.JavaCls;
import sunjava.core.Param;
import sunjava.core.Types;
import sunjava.entity.EntityCls;
import sunjava.entityrepository.method.ConstructorEntityRepository;
import sunjava.entityrepository.method.MethodEntityLoad;
import sunjava.entityrepository.method.MethodEntitySave;
import sunjava.entityrepository.method.MethodCreateQuery;
import sunjava.entityrepository.method.MethodGetAllSelectFields;
import sunjava.entityrepository.method.MethodGetById;
import sunjava.entityrepository.method.MethodGetFromResultSet;
import sunjava.entityrepository.method.MethodGetSelectFields;
import sunjava.entityrepository.method.MethodGetTableName;
import sunjava.entityrepository.method.MethodGetTableNameAlias;
import sunjava.entityrepository.method.MethodLoadCollection;

public class ClsEntityRepository extends JavaCls{
//	protected ArrayList<ClsBeanQuery> beanQueryClasses;
	public static final String CLSNAME = "EntityRepository";
	protected static String entityRepositoryPackage;
	
	public static void setEntityRepositoryPackage(String entityRepositoryPackage) {
		ClsEntityRepository.entityRepositoryPackage = entityRepositoryPackage;
	}
	
	public ClsEntityRepository() {
		super(CLSNAME,entityRepositoryPackage);
		//Attr sqlCon = new Attr(Attr.Protected,new ClsThreadLocal(Types.Connection) , "sqlConnection",null, true);
		//addAttr(sqlCon);
		//addMethod(new MethodGetSqlCon());
//		beanQueryClasses = new ArrayList<>(); 
	}

	/*private void addImports() {
		addImport(Types.SqlQuery.getImport());
		addImport(Types.PgSqlQuery.getImport());
		addImport(Types.SqlParam.getImport());
		addImport(Types.ResultSet.getImport());
		addImport("sql.orm.BeanQuery");
	}*/
	
	public void addDeclarations(Collection<EntityCls> entities) {
		addConstructor(new ConstructorEntityRepository());
//		addMethod(new MethodInit());
		for(EntityCls entity:entities) {
			addImport(entity.getPackage()+"."+entity.getName());
//			addAttr(new Attr(new ClsQHash(entity.getPkType(), entity), "loadedBeans"+entity.getName()));
			addMethod(new MethodGetById(entity));
			addMethod(new MethodGetFromResultSet(entity));
//			addMethod(new MethodFetchList(entity.getOneRelations(), entity.getOneToManyRelations(), entity, entity.getTbl().getPrimaryKey()));
//			addMethod(new MethodFetchListStatic(entity));
//			addMethod(new MethodFetchOne(entity.getOneRelations(),entity.getOneToManyRelations(), entity, null));
//			addMethod(new MethodFetchOneStatic(entity));
//			beanQueryClasses.add(new ClsBeanQuery(entity));
//			addMethod(new MethodLoadCollection(new Param(Types.qset(entity), "collection")));
			addMethod(new MethodLoadCollection(new Param(Types.linkedHashSet(entity),  "collection"), entity));
			addMethod(new MethodCreateQuery(entity));
			addMethod(new MethodEntityLoad(entity));
			addMethod(new MethodEntitySave(entity));
			addMethod(new MethodGetSelectFields(entity));
			addMethod(new MethodGetAllSelectFields(entity));
			addMethod(new MethodGetTableNameAlias(entity));
			addMethod(new MethodGetTableName(entity));
		}
		
//		addImports();
	}

	public static String getMethodNameGetTableName(EntityCls entity) {
		// TODO Auto-generated method stub
		return "getTableName"+entity.getName();
	}

	public static String getMethodNameGetSelectFields(EntityCls entity) {
		// TODO Auto-generated method stub
		return "getSelectFields"+entity.getName();
	}
	
	public static String getMethodNameGetAllSelectFields(EntityCls entity) {
		// TODO Auto-generated method stub
		return "getAllSelectFields"+entity.getName();
	}
	
//	@Override
//	public String toSourceString() {
//		StringBuilder sb=new StringBuilder(super.toSourceString());
//		sb.append("\n\n");
//		for(ClsBeanQuery beanQuery:beanQueryClasses) {
//			sb.append(beanQuery.toSourceString()).append("\n\n");
//		}
//		return sb.toString();
//	}
//	
//	@Override
//	public void addMethodImplementations() {
//		super.addMethodImplementations();
//		for(ClsBeanQuery beanQuery:beanQueryClasses) {
//			beanQuery.addMethodImplementations();
//		}
//	}

}
