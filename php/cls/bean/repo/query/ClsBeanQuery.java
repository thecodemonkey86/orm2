package php.cls.bean.repo.query;

import php.cls.PhpCls;
import php.cls.bean.BeanCls;
import php.cls.bean.repo.query.method.ConstructorBeanQuery;
import php.cls.bean.repo.query.method.MethodAddRelatedTableJoins;
import php.cls.bean.repo.query.method.MethodBeanQueryFetch;
import php.cls.bean.repo.query.method.MethodBeanQueryFetchOne;
import php.cls.bean.repo.query.method.MethodGetAllSelectFields;
import php.cls.bean.repo.query.method.MethodGetTableName;
import php.lib.ClsBaseBeanQuery;

public class ClsBeanQuery extends PhpCls {

	protected static String beanQueryPackage;
	
	public static void setBeanQueryPackage(String beanQueryPackage) {
		ClsBeanQuery.beanQueryPackage = beanQueryPackage;
	}
	
	public ClsBeanQuery(BeanCls cls) {
		super(cls.getName()+ "BeanQuery",beanQueryPackage);
		setSuperclass(new ClsBaseBeanQuery(cls));
		setConstructor(new ConstructorBeanQuery());
		addMethod(new MethodBeanQueryFetch(cls));
		addMethod(new MethodBeanQueryFetchOne(cls));
		addMethod(new MethodAddRelatedTableJoins(cls));
		addMethod(new MethodGetAllSelectFields(cls));
		addMethod(new MethodGetTableName(cls));
	}


}
