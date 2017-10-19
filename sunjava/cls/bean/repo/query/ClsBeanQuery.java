package sunjava.cls.bean.repo.query;

import sunjava.cls.JavaCls;
import sunjava.cls.bean.BeanCls;
import sunjava.cls.bean.repo.query.method.ConstructorBeanQuery;
import sunjava.cls.bean.repo.query.method.MethodAddRelatedTableJoins;
import sunjava.cls.bean.repo.query.method.MethodBeanQueryFetch;
import sunjava.cls.bean.repo.query.method.MethodBeanQueryFetchOne;
import sunjava.cls.bean.repo.query.method.MethodGetAllSelectFields;
import sunjava.cls.bean.repo.query.method.MethodGetTableName;
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
