package php.cls.bean.repo;

import java.util.Collection;

import php.Types;
import php.cls.Attr;
import php.cls.PhpCls;
import php.cls.Param;
import php.cls.bean.BeanCls;
import php.cls.bean.repo.method.ConstructorBeanRepository;
import php.cls.bean.repo.method.MethodBeanLoad;
import php.cls.bean.repo.method.MethodBeanSave;
import php.cls.bean.repo.method.MethodCreateQuery;
import php.cls.bean.repo.method.MethodGetAllSelectFields;
import php.cls.bean.repo.method.MethodGetById;
import php.cls.bean.repo.method.MethodGetFromQueryAssocArray;
import php.cls.bean.repo.method.MethodGetSelectFields;
import php.cls.bean.repo.method.MethodGetTableName;
import php.cls.bean.repo.method.MethodGetTableName;
import php.cls.bean.repo.method.MethodLoadCollection;
import php.cls.bean.repo.method.MethodSetSqlCon;
import php.cls.method.MethodAttributeGetter;

public class ClsBeanRepository extends PhpCls{
//	protected ArrayList<ClsBeanQuery> beanQueryClasses;
	public static final String CLSNAME = "BeanRepository";
	protected static String beanRepositoryNamespace;
	
	public static void setBeanRepositoryPackage(String beanRepositoryPackage) {
		ClsBeanRepository.beanRepositoryNamespace = beanRepositoryPackage;
	}
	
	public ClsBeanRepository() {
		super(CLSNAME,beanRepositoryNamespace);
		Attr sqlCon = new Attr(Types.mysqli, "sqlCon");
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
		setConstructor(new ConstructorBeanRepository());
		addMethod(new MethodSetSqlCon());
		for(BeanCls bean:beans) {
//			addAttr(new Attr(new ClsQHash(bean.getPkType(), bean), "loadedBeans"+bean.getName()));
			addMethod(new MethodGetById(bean));
//			addMethod(new MethodFetchList(bean.getOneRelations(), bean.getOneToManyRelations(), bean, bean.getTbl().getPrimaryKey()));
//			addMethod(new MethodFetchListStatic(bean));
//			addMethod(new MethodFetchOne(bean.getOneRelations(),bean.getOneToManyRelations(), bean, null));
//			addMethod(new MethodFetchOneStatic(bean));
//			beanQueryClasses.add(new ClsBeanQuery(bean));
//			addMethod(new MethodLoadCollection(new Param(Types.qset(bean), "collection")));
			addMethod(new MethodLoadCollection(new Param(Types.array(bean),  "collection"), bean));
			addMethod(new MethodCreateQuery(bean));
			addMethod(new MethodBeanLoad(bean));
			addMethod(new MethodBeanSave(bean));
			addMethod(new MethodGetSelectFields(bean));
			addMethod(new MethodGetAllSelectFields(bean));
			addMethod(new MethodGetTableName(bean));
			addMethod(new MethodGetFromQueryAssocArray(bean));
//			addMethod(new MethodGetTableName(bean));
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
