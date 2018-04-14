package cpp.beanquery;

import cpp.Types;
import cpp.bean.BeanCls;
import cpp.beanquery.method.ConstructorBeanQuery;
import cpp.beanquery.method.MethodBeanQueryFetch;
import cpp.beanquery.method.MethodBeanQueryFetchOne;
import cpp.beanquery.method.MethodOrderByPrimaryKey;
import cpp.beanrepository.ClsBeanRepository;
import cpp.core.Attr;
import cpp.core.Cls;
import cpp.lib.ClsQVector;
import cpp.lib.ClsTemplateAbstractBeanQuery;

public class ClsBeanQuery extends Cls {

	
	public ClsBeanQuery(BeanCls cls) {
		super(cls.getName()+ "BeanQuery");
		addSuperclass(Types.baseBeanQuery(cls));
		addConstructor(new ConstructorBeanQuery());
		addMethod(new MethodBeanQueryFetch(cls));
		addMethod(new MethodBeanQueryFetchOne(cls));
		addMethod(new MethodOrderByPrimaryKey(cls));
		
		// FIXME TODO Parent Klasse BeanQuery (und BaseRepository) auflösen
		/*for(Column c : cls.getTbl().getAllColumns()) {
			addMethod(new MethodBeanQueryWhereEquals(this,cls, c));
		}*/
		
		addIncludeLib(ClsQVector.CLSNAME);
		addIncludeHeader(BeanCls.getModelPath() + "beans/"+cls.getIncludeHeader());
		addIncludeHeader(ClsTemplateAbstractBeanQuery.CLSNAME.toLowerCase());
		addIncludeHeader("../"+ ClsBeanRepository.CLSNAME.toLowerCase());
		addAttr(new Attr(Types.BeanRepository.toSharedPtr(), "repository"));
		
		addForwardDeclaredClass(Types.BeanRepository);
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
