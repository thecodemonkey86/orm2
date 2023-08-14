package cpp.entityrepository;

import java.util.Collection;

import cpp.Types;
import cpp.core.Cls;
import cpp.core.Param;
import cpp.entity.EntityCls;
import cpp.entityrepository.method.ConstructorEntityRepository;
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
import cpp.entityrepository.method.MethodInsertOrIgnore;
import cpp.entityrepository.method.MethodLoadCollection;
import cpp.entityrepository.method.MethodPrepareInsertOrIgnore;
import cpp.entityrepository.method.MethodRemoveAllRelated;
import cpp.entityrepository.method.MethodRepoCreateNew;
import cpp.entityrepository.method.MethodRepoCreateNewNonNullableOnly;
import cpp.lib.ClsBaseRepository;
import database.column.Column;
import database.relation.IManyRelation;
import database.table.Table;

public class ClsEntityRepository extends Cls{
//	protected ArrayList<ClsBeanQuery> beanQueryClasses;
	public static final String CLSNAME = "EntityRepository";
	public static final String sqlCon = "sqlCon";
	
	public ClsEntityRepository() {
		super(CLSNAME);
		addSuperclass(new ClsBaseRepository());
		//addSuperclass(new EnableSharedFromThis(this));
//		beanQueryClasses = new ArrayList<>(); 
	}
	

	public void addDeclarations(Collection<EntityCls> beans) {
		addConstructor(new ConstructorEntityRepository());
		addIncludeLibInSource("QHash");
		addIncludeLibInSource(Types.QSqlRecord);
		addIncludeHeader(getSuperclass().getHeaderInclude());
		addIncludeHeaderInSource(EntityCls.getDatabaseMapper().getSqlQueryType().getIncludeHeader());
		for(EntityCls entity:beans) {
			addIncludeHeaderInSource(EntityCls.getModelPath() + "entities/"+entity.getIncludeHeader());
			addIncludeHeader("query/"+entity.getName().toLowerCase()+"entityqueryselect");
			
			if(entity.getTbl().hasQueryType(Table.QueryType.Delete))
				addIncludeHeader("query/"+entity.getName().toLowerCase()+"entityquerydelete");
			
			if(entity.getTbl().hasQueryType(Table.QueryType.Update))
				addIncludeHeader("query/"+entity.getName().toLowerCase()+"entityqueryupdate");
//			addAttr(new Attr(new ClsQHash(bean.getPkType(), bean.toRawPointer()), "loadedBeans"+bean.getName()));
			addMethod(new MethodGetById(entity));
			addMethod(new MethodGetById(entity,true));
			addMethod(new MethodGetByIdOrCreateNew(entity));
			addMethod(new MethodGetByIdOrCreateNew(entity,true));			
//			addMethod(new MethodGetByRecord(bean.getTbl().getColumns(true), bean));
			addMethod(new MethodFetchList(entity, entity.getTbl().getPrimaryKey(),false));
			addMethod(new MethodFetchOne(entity.getOneRelations(),entity.getOneToManyRelations(), entity, null, false));
			if(entity.hasRelations()) {
				addMethod(new MethodFetchList(entity, entity.getTbl().getPrimaryKey(),true));
				addMethod(new MethodFetchOne(entity.getOneRelations(),entity.getOneToManyRelations(), entity, null, true));
			}
			
//			addMethod(new MethodFetchListStatic(bean));
			
			
//			addMethod(new MethodFetchOneStatic(bean));
//			beanQueryClasses.add(new ClsBeanQuery(bean));
			addForwardDeclaredClass(entity);
		
//			addMethod(new MethodLoadCollection(new Param(Types.qset(bean.toSharedPtr()).toRawPointer(), "collection")));
			
			if(entity.getTbl().isEnableLoadCollection())
				addMethod(new MethodLoadCollection(new Param(Types.qlist(entity.toSharedPtr()).toRef(),  "collection"), entity));
			addMethod(new MethodCreateQuerySelect(entity));
			
			if(entity.getTbl().hasQueryType(Table.QueryType.Delete))
				addMethod(new MethodCreateQueryDelete(entity));
			
			if(entity.getTbl().hasQueryType(Table.QueryType.Update))
				addMethod(new MethodCreateQueryUpdate(entity));
			
			if(entity.hasRelations())
				addMethod(new MethodEntityLoad(entity));
			
			if(EntityCls.getDatabase().supportsInsertOrIgnore()) {
//				addMethod(new MethodEntitySave(bean,true));
				addMethod(new MethodPrepareInsertOrIgnore(entity));
				addMethod(new MethodInsertOrIgnore(entity));
				
			}
//			addMethod(new MethodEntitySave(bean,false));
//			addMethod(new MethodEntitySaveBulk(bean,false));
//			addMethod(new MethodEntitySaveBulk(bean,true));
			addMethod(new MethodGetFromRecord(entity));
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
	protected void addHeaderCodeBeforeClassDeclaration(StringBuilder sb) {
		// TODO Auto-generated method stub
		super.addHeaderCodeBeforeClassDeclaration(sb);
		sb.append("using namespace QtCommon2;\n");
	}

	@Override
		public String toSourceString() {
			// TODO Auto-generated method stub
			return super.toSourceString();
		}

}
