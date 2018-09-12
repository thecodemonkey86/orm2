package php.beanrepository.query;

import database.column.Column;
import php.bean.BeanCls;
import php.beanrepository.query.method.ConstructorBeanQuery;
import php.beanrepository.query.method.MethodAddRelatedTableJoins;
import php.beanrepository.query.method.MethodBeanQueryFetch;
import php.beanrepository.query.method.MethodBeanQueryFetchOne;
import php.beanrepository.query.method.MethodBeanQueryWhereEquals;
import php.beanrepository.query.method.MethodGetAllSelectFields;
import php.beanrepository.query.method.MethodGetTableName;
import php.beanrepository.query.method.MethodLimitAndOffset;
import php.core.PhpCls;
import php.lib.ClsBaseBeanQuery;

public class ClsBeanQuery extends PhpCls {

	protected static String beanQueryNamespace;
	
	public static void setBeanQueryNamespace(String beanQueryNamespace) {
		ClsBeanQuery.beanQueryNamespace = beanQueryNamespace;
	}
	
	public ClsBeanQuery(BeanCls cls) {
		super(cls.getName()+ "BeanQuery",beanQueryNamespace);
		
		setSuperclass(new ClsBaseBeanQuery(cls));
		setConstructor(new ConstructorBeanQuery());
		addMethod(new MethodBeanQueryFetch(cls));
		addMethod(new MethodBeanQueryFetchOne(cls));
		addMethod(new MethodAddRelatedTableJoins(cls));
		addMethod(new MethodGetAllSelectFields(cls));
		addMethod(new MethodGetTableName(cls));
		addMethod(new MethodLimitAndOffset(cls,this));
		
		for(Column c : cls.getTbl().getAllColumns()) {
			addMethod(new MethodBeanQueryWhereEquals(this, cls, c));
		}
	}


}
