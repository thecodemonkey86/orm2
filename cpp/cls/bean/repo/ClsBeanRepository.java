package cpp.cls.bean.repo;

import java.util.Collection;

import cpp.Types;
import cpp.cls.Cls;
import cpp.cls.Param;
import cpp.cls.bean.BeanCls;
import cpp.cls.bean.repo.method.ConstructorBeanRepository;
import cpp.cls.bean.repo.method.MethodBeanLoad;
import cpp.cls.bean.repo.method.MethodBeanSave;
import cpp.cls.bean.repo.method.MethodCreateQuery;
import cpp.cls.bean.repo.method.MethodFetchList;
import cpp.cls.bean.repo.method.MethodFetchOne;
import cpp.cls.bean.repo.method.MethodGetById;
import cpp.cls.bean.repo.method.MethodGetFromRecord;
import cpp.cls.bean.repo.method.MethodLoadCollection;
import cpp.cls.bean.repo.method.MethodRepoCreateNew;
import cpp.lib.ClsBaseRepository;
import cpp.lib.EnableSharedFromThis;

public class ClsBeanRepository extends Cls{
//	protected ArrayList<ClsBeanQuery> beanQueryClasses;
	public static final String CLSNAME = "BeanRepository";
	public static final String sqlCon = "sqlCon";
	
	public ClsBeanRepository() {
		super(CLSNAME);
		addSuperclass(new ClsBaseRepository());
		addSuperclass(new EnableSharedFromThis(this));
//		beanQueryClasses = new ArrayList<>(); 
	}
	

	public void addDeclarations(Collection<BeanCls> beans) {
		addConstructor(new ConstructorBeanRepository());
		addIncludeLib("QHash");
		addIncludeHeader(getSuperclass().getHeaderInclude());
		
		if(beans.size()>0)
			addIncludeHeader(Types.orderedSet(null).getHeaderInclude()
					);
		for(BeanCls bean:beans) {
			addIncludeHeader(BeanCls.getModelPath() + "beans/"+bean.getIncludeHeader());
			addIncludeHeader("query/"+bean.getName().toLowerCase()+"beanquery");
//			addAttr(new Attr(new ClsQHash(bean.getPkType(), bean.toRawPointer()), "loadedBeans"+bean.getName()));
			addMethod(new MethodGetById(bean));
//			addMethod(new MethodGetByRecord(bean.getTbl().getColumns(true), bean));
			addMethod(new MethodFetchList(bean.getOneRelations(), bean.getOneToManyRelations(), bean, bean.getTbl().getPrimaryKey()));
//			addMethod(new MethodFetchListStatic(bean));
			addMethod(new MethodFetchOne(bean.getOneRelations(),bean.getOneToManyRelations(), bean, null));
//			addMethod(new MethodFetchOneStatic(bean));
//			beanQueryClasses.add(new ClsBeanQuery(bean));
			addForwardDeclaredClass(bean.getName());
			addForwardDeclaredClass(bean.getName()+"BeanQuery");
//			addMethod(new MethodLoadCollection(new Param(Types.qset(bean.toSharedPtr()).toRawPointer(), "collection")));
			addMethod(new MethodLoadCollection(new Param(Types.orderedSet(bean.toSharedPtr()).toRawPointer(),  "collection"), bean));
			addMethod(new MethodCreateQuery(bean));
			addMethod(new MethodBeanLoad(bean));
			addMethod(new MethodBeanSave(bean));
			addMethod(new MethodGetFromRecord(bean,false));
			addMethod(new MethodRepoCreateNew(bean));
		}
		
	}
	
	
//	@Override
//	public String toHeaderString() {
//		StringBuilder sb=new StringBuilder(super.toHeaderString());
//		sb.append("\n\n");
//		for(ClsBeanQuery beanQuery:beanQueryClasses) {
//			sb.append(beanQuery.toHeaderString()).append("\n\n");
//		}
//		return sb.toString();
//	}
	
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
