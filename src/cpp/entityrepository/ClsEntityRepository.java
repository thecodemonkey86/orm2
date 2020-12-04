package cpp.entityrepository;

import java.util.Collection;

import cpp.Types;
import cpp.core.Cls;
import cpp.core.Param;
import cpp.core.SharedPtr;
import cpp.entity.EntityCls;
import cpp.entityrepository.method.MethodEntityLoad;
import cpp.entityrepository.method.MethodEntityRemove;
import cpp.entityrepository.method.MethodCreateQueryDelete;
import cpp.entityrepository.method.MethodCreateQuerySelect;
import cpp.entityrepository.method.MethodCreateQueryUpdate;
import cpp.entityrepository.method.MethodFetchList;
import cpp.entityrepository.method.MethodFetchOne;
import cpp.entityrepository.method.MethodGetById;
import cpp.entityrepository.method.MethodGetByIdOrCreateNew;
import cpp.entityrepository.method.MethodGetFromRecord;
import cpp.entityrepository.method.MethodInsertOrIgnore;
import cpp.entityrepository.method.MethodLoadCollection;
import cpp.entityrepository.method.MethodPrepareUpsert;
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
	

	public void addDeclarations(Collection<EntityCls> beans) {
		addIncludeLibInSource(Types.QSqlRecord);
		addIncludeHeader(getSuperclass().getHeaderInclude());
		addIncludeInSourceDefaultHeaderFileName(EntityCls.getDatabaseMapper().getSqlQueryType());
		addInclude(ClsDbPool.instance.getHeaderInclude());
		if(beans.size()>0)
			addIncludeHeaderInSource(Types.orderedSet(null).getHeaderInclude()
					);
		
		
		
		for(EntityCls bean:beans) {
			addIncludeHeader(bean.getHeaderInclude());
			addIncludeHeader("query/"+bean.getName().toLowerCase()+"entityqueryselect");
			
			if(bean.getTbl().hasQueryType(Table.QueryType.Delete))
				addIncludeHeader("query/"+bean.getName().toLowerCase()+"entityquerydelete");
			
			if(bean.getTbl().hasQueryType(Table.QueryType.Update))
				addIncludeHeader("query/"+bean.getName().toLowerCase()+"entityqueryupdate");
//			addAttr(new Attr(new ClsQHash(bean.getPkType(), bean.toRawPointer()), "loadedBeans"+bean.getName()));
			addMethod(new MethodGetById(bean));
			addMethod(new MethodGetById(bean,true));
			addMethod(new MethodGetByIdOrCreateNew(bean));
			addMethod(new MethodGetByIdOrCreateNew(bean,true));			
//			addMethod(new MethodGetByRecord(bean.getTbl().getColumns(true), bean));
			addMethod(new MethodFetchList(bean, bean.getTbl().getPrimaryKey(),false));
			addMethod(new MethodFetchOne(bean.getOneRelations(),bean.getOneToManyRelations(), bean, null, false));
			if(bean.hasRelations()) {
				addMethod(new MethodFetchList(bean, bean.getTbl().getPrimaryKey(),true));
				addMethod(new MethodFetchOne(bean.getOneRelations(),bean.getOneToManyRelations(), bean, null, true));
			}
			
//			addMethod(new MethodFetchListStatic(bean));
			
			
//			addMethod(new MethodFetchOneStatic(bean));
//			beanQueryClasses.add(new ClsBeanQuery(bean));
			addForwardDeclaredClass(bean);
			addForwardDeclaredClass(Types.beanQuerySelect(bean));
			
			if(bean.getTbl().hasQueryType(Table.QueryType.Update))
				addForwardDeclaredClass(Types.beanQueryUpdate(bean));
			
			if(bean.getTbl().hasQueryType(Table.QueryType.Delete))
				addForwardDeclaredClass(Types.beanQueryDelete(bean));
			
			if(EntityCls.getCfg().isEnableMethodLoadCollection())
				addMethod(new MethodLoadCollection(new Param(Types.orderedSet(bean.toSharedPtr()).toRawPointer(),  "collection"), bean));
			addMethod(new MethodCreateQuerySelect(bean));
			
			if(bean.getTbl().hasQueryType(Table.QueryType.Delete))
				addMethod(new MethodCreateQueryDelete(bean));
			
			if(bean.getTbl().hasQueryType(Table.QueryType.Update))
				addMethod(new MethodCreateQueryUpdate(bean));
			
			addMethod(new MethodEntityLoad(bean));
			if(EntityCls.getDatabase().supportsInsertOrIgnore()) {
				addMethod(new MethodPrepareUpsert(bean));
				addMethod(new MethodInsertOrIgnore(bean));
				
			}
//			addMethod(new MethodEntitySave(bean,false));
//			addMethod(new MethodEntitySaveBulk(bean,false));
//			addMethod(new MethodEntitySaveBulk(bean,true));
			addMethod(new MethodGetFromRecord(bean,false));
			addMethod(new MethodRepoCreateNew(bean));
			int countNullable = 0;
			
			for(Column c : bean.getTbl().getFieldColumns()) {
				if(c.isNullable()) {
					countNullable++;
				}
			}
			
			int countInitializeFields = bean.getTbl().getFieldColumns().size();
			if(!bean.getTbl().getPrimaryKey().isAutoIncrement()) {
				countInitializeFields += bean.getTbl().getPrimaryKey().getColumnCount();
				
				for(Column c : bean.getTbl().getPrimaryKey()) {
					if(c.isNullable()) {
						countNullable++;
					}
				}
			}
			if(countInitializeFields > 0) {
				addMethod(new MethodRepoCreateNew(bean,true,false));
				
				if(countNullable > 0) {
					addMethod(new MethodRepoCreateNew(bean,true,true));
					MethodRepoCreateNewNonNullableOnly methodRepoCreateNewNonNullableOnly = new MethodRepoCreateNewNonNullableOnly(bean);
					if(!methodRepoCreateNewNonNullableOnly.getParams().isEmpty())
						addMethod(methodRepoCreateNewNonNullableOnly);
				}
			}
			addMethod(new MethodEntityRemove(bean,false));
			
			for(IManyRelation r:bean.getAllManyRelations())
				addMethod(new MethodRemoveAllRelated(bean, r));
		}
		
		
//		addUsingMethodInstruction(new UsingMethodInstruction(getMethod(ClsBaseRepository.beginTransaction)));
//		addUsingMethodInstruction(new UsingMethodInstruction(getMethod(ClsBaseRepository.commitTransaction)));
//		addUsingMethodInstruction(new UsingMethodInstruction(getMethod(ClsBaseRepository.rollbackTransaction)));
//		addUsingMethodInstruction(new UsingMethodInstruction(getMethod(ClsBaseRepository.getSqlCon)));
	}
	
	
	@Override
	public SharedPtr toSharedPtr() {
		throw new RuntimeException("deleted constructor");
	}

}
