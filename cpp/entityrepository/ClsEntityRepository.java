package cpp.entityrepository;

import java.util.Collection;

import cpp.Types;
import cpp.core.Cls;
import cpp.core.Param;
import cpp.entity.EntityCls;
import cpp.entityrepository.method.ConstructorEntityRepository;
import cpp.entityrepository.method.MethodEntityLoad;
import cpp.entityrepository.method.MethodEntityRemove;
import cpp.entityrepository.method.MethodEntitySave;
import cpp.entityrepository.method.MethodEntitySaveBulk;
import cpp.entityrepository.method.MethodCreateQueryDelete;
import cpp.entityrepository.method.MethodCreateQuerySelect;
import cpp.entityrepository.method.MethodCreateQueryUpdate;
import cpp.entityrepository.method.MethodFetchList;
import cpp.entityrepository.method.MethodFetchOne;
import cpp.entityrepository.method.MethodGetById;
import cpp.entityrepository.method.MethodGetFromRecord;
import cpp.entityrepository.method.MethodLoadCollection;
import cpp.entityrepository.method.MethodRepoCreateNew;
import cpp.entityrepository.method.MethodRepoCreateNewNonNullableOnly;
import cpp.lib.ClsBaseRepository;
import cpp.lib.EnableSharedFromThis;
import database.column.Column;

public class ClsEntityRepository extends Cls{
//	protected ArrayList<ClsBeanQuery> beanQueryClasses;
	public static final String CLSNAME = "EntityRepository";
	public static final String sqlCon = "sqlCon";
	
	public ClsEntityRepository() {
		super(CLSNAME);
		addSuperclass(new ClsBaseRepository());
		addSuperclass(new EnableSharedFromThis(this));
//		beanQueryClasses = new ArrayList<>(); 
	}
	

	public void addDeclarations(Collection<EntityCls> beans) {
		addConstructor(new ConstructorEntityRepository());
		addIncludeLib("QHash");
		addIncludeLib(Types.QSqlRecord);
		addIncludeHeader(getSuperclass().getHeaderInclude());
		addIncludeHeader(EntityCls.getDatabaseMapper().getSqlQueryType().getIncludeHeader());
		if(beans.size()>0)
			addIncludeHeader(Types.orderedSet(null).getHeaderInclude()
					);
		for(EntityCls bean:beans) {
			addIncludeHeader(EntityCls.getModelPath() + "entities/"+bean.getIncludeHeader());
			addIncludeHeader("query/"+bean.getName().toLowerCase()+"entityqueryselect");
			addIncludeHeader("query/"+bean.getName().toLowerCase()+"entityquerydelete");
			addIncludeHeader("query/"+bean.getName().toLowerCase()+"entityqueryupdate");
//			addAttr(new Attr(new ClsQHash(bean.getPkType(), bean.toRawPointer()), "loadedBeans"+bean.getName()));
			addMethod(new MethodGetById(bean));
			addMethod(new MethodGetById(bean,true));
//			addMethod(new MethodGetByRecord(bean.getTbl().getColumns(true), bean));
			addMethod(new MethodFetchList(bean, bean.getTbl().getPrimaryKey(),false));
			addMethod(new MethodFetchList(bean, bean.getTbl().getPrimaryKey(),true));
//			addMethod(new MethodFetchListStatic(bean));
			addMethod(new MethodFetchOne(bean.getOneRelations(),bean.getOneToManyRelations(), bean, null));
//			addMethod(new MethodFetchOneStatic(bean));
//			beanQueryClasses.add(new ClsBeanQuery(bean));
			addForwardDeclaredClass(bean);
			addForwardDeclaredClass(Types.beanQuerySelect(bean));
			addForwardDeclaredClass(Types.beanQueryUpdate(bean));
			addForwardDeclaredClass(Types.beanQueryDelete(bean));
//			addMethod(new MethodLoadCollection(new Param(Types.qset(bean.toSharedPtr()).toRawPointer(), "collection")));
			addMethod(new MethodLoadCollection(new Param(Types.orderedSet(bean.toSharedPtr()).toRawPointer(),  "collection"), bean));
			addMethod(new MethodCreateQuerySelect(bean));
			addMethod(new MethodCreateQueryDelete(bean));
			addMethod(new MethodCreateQueryUpdate(bean));
			addMethod(new MethodEntityLoad(bean));
			addMethod(new MethodEntitySave(bean));
			addMethod(new MethodEntitySaveBulk(bean,false));
			addMethod(new MethodEntitySaveBulk(bean,true));
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