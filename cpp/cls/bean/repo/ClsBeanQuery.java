package cpp.cls.bean.repo;

import cpp.Types;
import cpp.cls.Attr;
import cpp.cls.Cls;
import cpp.cls.bean.BeanCls;
import cpp.cls.bean.repo.method.ConstructorBeanQuery;
import cpp.cls.bean.repo.method.MethodBeanQueryFetch;
import cpp.cls.bean.repo.method.MethodBeanQueryFetchOne;
import cpp.lib.ClsQVector;
import cpp.lib.ClsTemplateAbstractBeanQuery;

public class ClsBeanQuery extends Cls {

	
	public ClsBeanQuery(BeanCls cls) {
		super(cls.getName()+ "BeanQuery");
		addSuperclass(Types.beanQuery(cls));
		addConstructor(new ConstructorBeanQuery());
		addMethod(new MethodBeanQueryFetch(cls));
		addMethod(new MethodBeanQueryFetchOne(cls));
		addIncludeLib(ClsQVector.CLSNAME);
		addIncludeHeader(BeanCls.getModelPath() + "beans/"+cls.getIncludeHeader());
		addIncludeHeader(ClsTemplateAbstractBeanQuery.CLSNAME.toLowerCase());
		addIncludeHeader("../"+ ClsBeanRepository.CLSNAME.toLowerCase());
		addAttr(new Attr(Types.BeanRepository.toSharedPtr(), "repository"));
		
		addForwardDeclaredClass(Types.BeanRepository.getName());
	}
	
	@Override
	public String toHeaderString() {
		// TODO Auto-generated method stub
		return super.toHeaderString();
	}
	
	@Override
	protected void addBeforeSourceCode(StringBuilder sb){
		super.addBeforeSourceCode(sb);
	}


}
