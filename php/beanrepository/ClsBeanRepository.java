package php.beanrepository;

import java.util.Collection;

import php.bean.BeanCls;
import php.beanrepository.method.ConstructorBeanRepository;
import php.beanrepository.method.MethodBeanLoad;
import php.beanrepository.method.MethodBeanSave;
import php.beanrepository.method.MethodCreateQuery;
import php.beanrepository.method.MethodGetAllSelectFields;
import php.beanrepository.method.MethodGetById;
import php.beanrepository.method.MethodGetFromQueryAssocArray;
import php.beanrepository.method.MethodGetSelectFields;
import php.beanrepository.method.MethodGetTableName;
import php.beanrepository.method.MethodLoadCollection;
import php.beanrepository.method.MethodSetSqlCon;
import php.core.Attr;
import php.core.Param;
import php.core.PhpCls;
import php.core.Types;
import php.core.method.MethodAttributeGetter;

public class ClsBeanRepository extends PhpCls{
//	protected ArrayList<ClsBeanQuery> beanQueryClasses;
	public static final String CLSNAME = "BeanRepository";
	protected static String beanRepositoryNamespace;
	
	public static void setBeanRepositoryNamespace(String beanRepositoryNamespace) {
		ClsBeanRepository.beanRepositoryNamespace = beanRepositoryNamespace;
	}
	
	public static String getBeanRepositoryNamespace() {
		return beanRepositoryNamespace;
	}
	
	public ClsBeanRepository() {
		super(CLSNAME,beanRepositoryNamespace);
		Attr sqlCon = new Attr(BeanCls.getTypeMapper().getDatabaseLinkType(), "sqlCon");
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
		addMethod(BeanCls.getTypeMapper().getBeanRepositoryBeginTransactionMethod());
		addMethod(BeanCls.getTypeMapper().getBeanRepositoryCommitTransactionMethod());
		addMethod(BeanCls.getTypeMapper().getBeanRepositoryRollbackTransactionMethod());
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
