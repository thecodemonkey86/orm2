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
	public static final String CLSNAME = "EntityRepository";
	protected static String entityRepositoryPackage;
	
	public static void setEntityRepositoryPackage(String entityRepositoryPackage) {
		ClsEntityRepository.entityRepositoryPackage = entityRepositoryPackage;
	}
	
	public ClsEntityRepository() {
		super(CLSNAME,entityRepositoryPackage);
	}

	
	public void addDeclarations(Collection<EntityCls> entities) {
		addConstructor(new ConstructorEntityRepository());
		for(EntityCls entity:entities) {
			addImport(entity.getPackage()+"."+entity.getName());
			addMethod(new MethodGetById(entity));
			addMethod(new MethodGetFromResultSet(entity));
			addMethod(new MethodLoadCollection(new Param(Types.linkedHashSet(entity),  "collection"), entity));
			addMethod(new MethodCreateQuery(entity));
			addMethod(new MethodEntityLoad(entity));
			addMethod(new MethodEntitySave(entity));
			addMethod(new MethodGetSelectFields(entity));
			addMethod(new MethodGetAllSelectFields(entity));
			addMethod(new MethodGetTableNameAlias(entity));
			addMethod(new MethodGetTableName(entity));
		}
		
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
