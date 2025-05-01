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
	public static final String CLSNAME = "EntityRepository";
	protected static String entityRepositoryNamespace;
	
	public static void setEntityRepositoryNamespace(String entityRepositoryNamespace) {
		ClsEntityRepository.entityRepositoryNamespace = entityRepositoryNamespace;
	}
	
	public static String getEntityRepositoryNamespace() {
		return entityRepositoryNamespace;
	}
	
	public ClsEntityRepository() {
		super(CLSNAME,entityRepositoryNamespace);
		Attr sqlCon = new Attr(EntityCls.getTypeMapper().getDatabaseLinkType(), "sqlCon");
		sqlCon.setStatic(true);
		addAttr(sqlCon);
		addMethod(new MethodAttributeGetter(sqlCon));
	}

	public void addDeclarations(Collection<EntityCls> entities) {
		setConstructor(new ConstructorEntityRepository());
		addMethod(new MethodSetSqlCon());
		addMethod(EntityCls.getTypeMapper().getEntityRepositoryBeginTransactionMethod());
		addMethod(EntityCls.getTypeMapper().getEntityRepositoryCommitTransactionMethod());
		addMethod(EntityCls.getTypeMapper().getEntityRepositoryRollbackTransactionMethod());
		for(EntityCls entity:entities) {
			addMethod(new MethodGetById(entity));
			addMethod(new MethodLoadCollection(new Param(Types.array(entity),  "collection"), entity));
			addMethod(new MethodCreateQuery(entity));
			addMethod(new MethodEntityLoad(entity));
			addMethod(new MethodEntitySave(entity));
			addMethod(new MethodGetSelectFields(entity));
			addMethod(new MethodGetAllSelectFields(entity));
			addMethod(new MethodGetTableName(entity));
			addMethod(new MethodGetFromQueryAssocArray(entity));
		}
		
//		addImports();
	}

	public static String getMethodNameGetTableName(EntityCls entity) {
		return "getTableName"+entity.getName();
	}

	public static String getMethodNameGetSelectFields(EntityCls entity) {
		return "getSelectFields"+entity.getName();
	}
	
	public static String getMethodNameGetAllSelectFields(EntityCls entity) {
		return "getAllSelectFields"+entity.getName();
	}

}
