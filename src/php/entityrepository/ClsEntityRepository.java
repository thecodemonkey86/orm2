package php.entityrepository;

import java.util.Collection;

import php.core.Attr;
import php.core.Param;
import php.core.PhpCls;
import php.core.Types;
import php.core.method.MethodAttributeGetter;
import php.entity.EntityCls;
import php.entityrepository.method.ConstructorEntityRepository;
import php.entityrepository.method.MethodEntityLoad;
import php.entityrepository.method.MethodEntitySave;
import php.entityrepository.method.MethodCreateQuery;
import php.entityrepository.method.MethodGetAllSelectFields;
import php.entityrepository.method.MethodGetById;
import php.entityrepository.method.MethodGetFromQueryAssocArray;
import php.entityrepository.method.MethodGetSelectFields;
import php.entityrepository.method.MethodGetTableName;
import php.entityrepository.method.MethodLoadCollection;
import php.entityrepository.method.MethodSetSqlCon;

public class ClsEntityRepository extends PhpCls{
//	protected ArrayList<ClsBeanQuery> beanQueryClasses;
	public static final String CLSNAME = "EntityRepository";
	protected static String beanRepositoryNamespace;
	
	public static void setBeanRepositoryNamespace(String beanRepositoryNamespace) {
		ClsEntityRepository.beanRepositoryNamespace = beanRepositoryNamespace;
	}
	
	public static String getBeanRepositoryNamespace() {
		return beanRepositoryNamespace;
	}
	
	public ClsEntityRepository() {
		super(CLSNAME,beanRepositoryNamespace);
		Attr sqlCon = new Attr(EntityCls.getTypeMapper().getDatabaseLinkType(), "sqlCon");
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
	
	public void addDeclarations(Collection<EntityCls> beans) {
		setConstructor(new ConstructorEntityRepository());
		addMethod(new MethodSetSqlCon());
		addMethod(EntityCls.getTypeMapper().getBeanRepositoryBeginTransactionMethod());
		addMethod(EntityCls.getTypeMapper().getBeanRepositoryCommitTransactionMethod());
		addMethod(EntityCls.getTypeMapper().getBeanRepositoryRollbackTransactionMethod());
		for(EntityCls bean:beans) {
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
			addMethod(new MethodEntityLoad(bean));
			addMethod(new MethodEntitySave(bean));
			addMethod(new MethodGetSelectFields(bean));
			addMethod(new MethodGetAllSelectFields(bean));
			addMethod(new MethodGetTableName(bean));
			addMethod(new MethodGetFromQueryAssocArray(bean));
//			addMethod(new MethodGetTableName(bean));
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
