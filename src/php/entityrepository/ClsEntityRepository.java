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
	
	public void addDeclarations(Collection<EntityCls> entities) {
		setConstructor(new ConstructorEntityRepository());
		addMethod(new MethodSetSqlCon());
		addMethod(EntityCls.getTypeMapper().getBeanRepositoryBeginTransactionMethod());
		addMethod(EntityCls.getTypeMapper().getBeanRepositoryCommitTransactionMethod());
		addMethod(EntityCls.getTypeMapper().getBeanRepositoryRollbackTransactionMethod());
		for(EntityCls entity:entities) {
//			addAttr(new Attr(new ClsQHash(entity.getPkType(), entity), "loadedBeans"+entity.getName()));
			addMethod(new MethodGetById(entity));
//			addMethod(new MethodFetchList(entity.getOneRelations(), entity.getOneToManyRelations(), entity, entity.getTbl().getPrimaryKey()));
//			addMethod(new MethodFetchListStatic(entity));
//			addMethod(new MethodFetchOne(entity.getOneRelations(),entity.getOneToManyRelations(), entity, null));
//			addMethod(new MethodFetchOneStatic(entity));
//			beanQueryClasses.add(new ClsBeanQuery(entity));
//			addMethod(new MethodLoadCollection(new Param(Types.qset(entity), "collection")));
			addMethod(new MethodLoadCollection(new Param(Types.array(entity),  "collection"), entity));
			addMethod(new MethodCreateQuery(entity));
			addMethod(new MethodEntityLoad(entity));
			addMethod(new MethodEntitySave(entity));
			addMethod(new MethodGetSelectFields(entity));
			addMethod(new MethodGetAllSelectFields(entity));
			addMethod(new MethodGetTableName(entity));
			addMethod(new MethodGetFromQueryAssocArray(entity));
//			addMethod(new MethodGetTableName(entity));
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
