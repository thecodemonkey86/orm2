package sunjava.beanrepository;

import java.util.Collection;

import sunjava.bean.BeanCls;
import sunjava.beanrepository.method.ConstructorBeanRepository;
import sunjava.beanrepository.method.MethodBeanLoad;
import sunjava.beanrepository.method.MethodBeanSave;
import sunjava.beanrepository.method.MethodCreateQuery;
import sunjava.beanrepository.method.MethodGetAllSelectFields;
import sunjava.beanrepository.method.MethodGetById;
import sunjava.beanrepository.method.MethodGetFromResultSet;
import sunjava.beanrepository.method.MethodGetSelectFields;
import sunjava.beanrepository.method.MethodGetTableName;
import sunjava.beanrepository.method.MethodGetTableNameAlias;
import sunjava.beanrepository.method.MethodLoadCollection;
import sunjava.beanrepository.method.MethodSetSqlCon;
import sunjava.core.Attr;
import sunjava.core.JavaCls;
import sunjava.core.Param;
import sunjava.core.Types;
import sunjava.core.method.MethodAttributeGetter;

public class ClsBeanRepository extends JavaCls{
//	protected ArrayList<ClsBeanQuery> beanQueryClasses;
	public static final String CLSNAME = "BeanRepository";
	protected static String beanRepositoryPackage;
	
	public static void setBeanRepositoryPackage(String beanRepositoryPackage) {
		ClsBeanRepository.beanRepositoryPackage = beanRepositoryPackage;
	}
	
	public ClsBeanRepository() {
		super(CLSNAME,beanRepositoryPackage);
		Attr sqlCon = new Attr(Types.Connection, "sqlCon");
		sqlCon.setStatic(true);
		addAttr(sqlCon);
		addMethod(new MethodAttributeGetter(sqlCon));
//		beanQueryClasses = new ArrayList<>(); 
	}

	/*private void addImports() {
		addImport(Types.SqlQuery.getImport());
		addImport(Types.PgSqlQuery.getImport());
		addImport(Types.SqlParam.getImport());
		addImport(Types.ResultSet.getImport());
		addImport("sql.orm.BeanQuery");
	}*/
	
	public void addDeclarations(Collection<BeanCls> beans) {
		addConstructor(new ConstructorBeanRepository());
		addMethod(new MethodSetSqlCon());
		for(BeanCls bean:beans) {
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
			addMethod(new MethodBeanLoad(bean));
			addMethod(new MethodBeanSave(bean));
			addMethod(new MethodGetSelectFields(bean));
			addMethod(new MethodGetAllSelectFields(bean));
			addMethod(new MethodGetTableNameAlias(bean));
			addMethod(new MethodGetTableName(bean));
		}
		
//		addImports();
	}

	public static String getMethodNameGetTableName(BeanCls bean) {
		// TODO Auto-generated method stub
		return "getTableName"+bean.getName();
	}

	public static String getMethodNameGetSelectFields(BeanCls bean) {
		// TODO Auto-generated method stub
		return "getSelectFields"+bean.getName();
	}
	
	public static String getMethodNameGetAllSelectFields(BeanCls bean) {
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
