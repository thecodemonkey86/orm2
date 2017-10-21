package sunjava.beanrepository.query;

import sunjava.bean.BeanCls;
import sunjava.beanrepository.query.method.ConstructorBeanQuery;
import sunjava.beanrepository.query.method.MethodAddRelatedTableJoins;
import sunjava.beanrepository.query.method.MethodBeanQueryFetch;
import sunjava.beanrepository.query.method.MethodBeanQueryFetchOne;
import sunjava.beanrepository.query.method.MethodGetAllSelectFields;
import sunjava.beanrepository.query.method.MethodGetTableName;
import sunjava.core.JavaCls;
import sunjava.lib.ClsBaseBeanQuery;

public class ClsBeanQuery extends JavaCls {

	protected static String beanQueryPackage;
	
	public static void setBeanQueryPackage(String beanQueryPackage) {
		ClsBeanQuery.beanQueryPackage = beanQueryPackage;
	}
	
	public ClsBeanQuery(BeanCls cls) {
		super(cls.getName()+ "BeanQuery",beanQueryPackage);
		addImport((JavaCls) BeanCls.getTypeMapper().getSqlQueryClass());
		setSuperclass(new ClsBaseBeanQuery(cls));
		addConstructor(new ConstructorBeanQuery());
		addMethod(new MethodBeanQueryFetch(cls));
		addMethod(new MethodBeanQueryFetchOne(cls));
		addMethod(new MethodAddRelatedTableJoins(cls));
		addMethod(new MethodGetAllSelectFields(cls));
		addMethod(new MethodGetTableName(cls));
	}


}
