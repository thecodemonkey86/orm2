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
	
	public void addDeclarations(Collection<EntityCls> beans) {
		addConstructor(new ConstructorEntityRepository());
//		addMethod(new MethodInit());
		for(EntityCls bean:beans) {
			addImport(bean.getPackage()+"."+bean.getName());
//			addAttr(new Attr(new ClsQHash(bean.getPkType(), bean), "loadedBeans"+bean.getName()));
			addMethod(new MethodGetById(bean));
			addMethod(new MethodGetFromResultSet(bean));
//			addMethod(new MethodFetchList(bean.getOneRelations(), bean.getOneToManyRelations(), bean, bean.getTbl().getPrimaryKey()));
//			addMethod(new MethodFetchListStatic(bean));
//			addMethod(new MethodFetchOne(bean.getOneRelations(),bean.getOneToManyRelations(), bean, null));
//			addMethod(new MethodFetchOneStatic(bean));
//			beanQueryClasses.add(new ClsBeanQuery(bean));
//			addMethod(new MethodLoadCollection(new Param(Types.qset(bean), "collection")));
			addMethod(new MethodLoadCollection(new Param(Types.linkedHashSet(bean),  "collection"), bean));
			addMethod(new MethodCreateQuery(bean));
			addMethod(new MethodEntityLoad(bean));
			addMethod(new MethodEntitySave(bean));
			addMethod(new MethodGetSelectFields(bean));
			addMethod(new MethodGetAllSelectFields(bean));
			addMethod(new MethodGetTableNameAlias(bean));
			addMethod(new MethodGetTableName(bean));
		}
		
//		addImports();
	}

	public static String getMethodNameGetTableName(EntityCls bean) {
		// TODO Auto-generated method stub
		return "getTableName"+bean.getName();
	}

	public static String getMethodNameGetSelectFields(EntityCls bean) {
		// TODO Auto-generated method stub
		return "getSelectFields"+bean.getName();
	}
	
	public static String getMethodNameGetAllSelectFields(EntityCls bean) {
		// TODO Auto-generated method stub
		return "getAllSelectFields"+bean.getName();
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
