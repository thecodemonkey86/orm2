package cpp.entityrepository;

import java.util.Collection;

import cpp.Types;
import cpp.core.Cls;
import cpp.core.Param;
import cpp.core.SharedPtr;
import cpp.entity.EntityCls;
import cpp.entityrepository.method.MethodEntityLoad;
import cpp.entityrepository.method.MethodEntityRemove;
import cpp.entityrepository.method.MethodEntitySharedPtrRemove;
import cpp.entityrepository.method.MethodCreateQueryDelete;
import cpp.entityrepository.method.MethodCreateQuerySelect;
import cpp.entityrepository.method.MethodCreateQueryUpdate;
import cpp.entityrepository.method.MethodFetchList;
import cpp.entityrepository.method.MethodFetchOne;
import cpp.entityrepository.method.MethodGetById;
import cpp.entityrepository.method.MethodGetByIdOrCreateNew;
import cpp.entityrepository.method.MethodGetFromRecord;
import cpp.entityrepository.method.MethodLoadCollection;
import cpp.entityrepository.method.MethodRemoveAllRelated;
import cpp.entityrepository.method.MethodRepoCreateNew;
import cpp.entityrepository.method.MethodRepoCreateNewNonNullableOnly;
import cpp.lib.ClsBaseRepository;
import cpp.util.ClsDbPool;
import database.column.Column;
import database.relation.IManyRelation;
import database.table.Table;

public class ClsEntityRepository extends Cls{
//	protected ArrayList<ClsBeanQuery> beanQueryClasses;
	public static final String CLSNAME = "EntityRepository";
	
	public ClsEntityRepository() {
		super(CLSNAME,false);
		addSuperclass(new ClsBaseRepository(ClsDbPool.instance));
//		beanQueryClasses = new ArrayList<>(); 
	}
	

	public void addDeclarations(Collection<EntityCls> entities) {
		addIncludeLibInSource(Types.QSqlRecord);
		addIncludeHeader(getSuperclass().getHeaderInclude());
		addIncludeInSourceDefaultHeaderFileName(EntityCls.getDatabaseMapper().getSqlQueryType());
		addInclude(ClsDbPool.instance.getHeaderInclude());
		
		if(EntityCls.getDatabase().supportsInsertOrIgnore()) {
			addMethodTemplate(EntityCls.getDatabaseMapper().getInsertOrIgnoreMethod(true));
			addMethodTemplate(EntityCls.getDatabaseMapper().getInsertOrIgnoreMethod(false));
		}
		
		for(EntityCls entity:entities) {
			addIncludeHeaderInSource(entity.getHeaderInclude());
			addIncludeHeader("query/"+entity.getName().toLowerCase()+"entityqueryselect");
			
			if(entity.getTbl().hasQueryType(Table.QueryType.Delete))
				addIncludeHeader("query/"+entity.getName().toLowerCase()+"entityquerydelete");
			
			if(entity.getTbl().hasQueryType(Table.QueryType.Update))
				addIncludeHeader("query/"+entity.getName().toLowerCase()+"entityqueryupdate");
//			addAttr(new Attr(new ClsQHash(entity.getPkType(), entity.toRawPointer()), "loadedBeans"+entity.getName()));
			addMethod(new MethodGetById(entity));
			addMethod(new MethodGetById(entity,true));
			addMethod(new MethodGetByIdOrCreateNew(entity));
			addMethod(new MethodGetByIdOrCreateNew(entity,true));			
//			addMethod(new MethodGetByRecord(entity.getTbl().getColumns(true), entity));
			addMethod(new MethodFetchList(entity, entity.getTbl().getPrimaryKey(),false));
			addMethod(new MethodFetchOne(entity.getOneRelations(),entity.getOneToManyRelations(), entity, null, false));
			if(entity.hasRelations()) {
				addMethod(new MethodFetchList(entity, entity.getTbl().getPrimaryKey(),true));
				addMethod(new MethodFetchOne(entity.getOneRelations(),entity.getOneToManyRelations(), entity, null, true));
			}
			
//			addMethod(new MethodFetchListStatic(entity));
			
			
//			addMethod(new MethodFetchOneStatic(entity));
//			beanQueryClasses.add(new ClsBeanQuery(entity));
			addForwardDeclaredClass(entity);
//			addForwardDeclaredClass(Types.beanQuerySelect(entity));
			
			if(entity.getTbl().hasQueryType(Table.QueryType.Update))
				addForwardDeclaredClass(Types.beanQueryUpdate(entity));
			
			if(entity.getTbl().hasQueryType(Table.QueryType.Delete))
				addForwardDeclaredClass(Types.beanQueryDelete(entity));
			
			if(EntityCls.getCfg().isEnableMethodLoadCollection())
				addMethod(new MethodLoadCollection(new Param(Types.qlist(entity.toSharedPtr()).toConstRef(),  "collection"), entity));
			addMethod(new MethodCreateQuerySelect(entity));
			
			if(entity.getTbl().hasQueryType(Table.QueryType.Delete))
				addMethod(new MethodCreateQueryDelete(entity));
			
			if(entity.getTbl().hasQueryType(Table.QueryType.Update))
				addMethod(new MethodCreateQueryUpdate(entity));
			
			if(entity.hasRelations())
				addMethod(new MethodEntityLoad(entity));
//			if(EntityCls.getDatabase().supportsInsertOrIgnore()) {
//				addMethod(new MethodPrepareUpsertPg(entity));
//				addMethod(new MethodInsertOrIgnorePg(entity));
//				
//			}
//			addMethod(new MethodEntitySave(entity,false));
//			addMethod(new MethodEntitySaveBulk(entity,false));
//			addMethod(new MethodEntitySaveBulk(entity,true));
			addMethod(new MethodGetFromRecord(entity,false));
			addMethod(new MethodRepoCreateNew(entity));
			int countNullable = 0;
			
			for(Column c : entity.getTbl().getFieldColumns()) {
				if(c.isNullable()) {
					countNullable++;
				}
			}
			
			int countInitializeFields = entity.getTbl().getFieldColumns().size();
			if(!entity.getTbl().getPrimaryKey().isAutoIncrement()) {
				countInitializeFields += entity.getTbl().getPrimaryKey().getColumnCount();
				
				for(Column c : entity.getTbl().getPrimaryKey()) {
					if(c.isNullable()) {
						countNullable++;
					}
				}
			}
			if(countInitializeFields > 0) {
				addMethod(new MethodRepoCreateNew(entity,true,false));
				
				if(countNullable > 0) {
					addMethod(new MethodRepoCreateNew(entity,true,true));
					MethodRepoCreateNewNonNullableOnly methodRepoCreateNewNonNullableOnly = new MethodRepoCreateNewNonNullableOnly(entity);
					if(!methodRepoCreateNewNonNullableOnly.getParams().isEmpty())
						addMethod(methodRepoCreateNewNonNullableOnly);
				}
			}
			addMethod(new MethodEntityRemove(entity,false));
			addMethod(new MethodEntitySharedPtrRemove(entity,false));
			
			for(IManyRelation r:entity.getAllManyRelations())
				addMethod(new MethodRemoveAllRelated(entity, r));
		}
		
	}
	
	
	@Override
	public SharedPtr toSharedPtr() {
		throw new RuntimeException("deleted constructor");
	}

}
