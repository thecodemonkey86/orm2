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
import php.core.Attr;
import php.core.PhpCls;
import php.core.Types;
import php.lib.ClsBaseBeanQuery;

public class ClsBeanQuery extends PhpCls {

	protected static String beanQueryNamespace;
	
	public static void setBeanQueryNamespace(String beanQueryNamespace) {
		ClsBeanQuery.beanQueryNamespace = beanQueryNamespace;
	}
	
	public ClsBeanQuery(BeanCls cls) {
		super(cls.getName()+ "BeanQuery",beanQueryNamespace);
		addAttr(new Attr(Types.SqlQuery, "sqlQuery"));
		addAttr(new Attr(Types.SqlQuery, "beanQueryLimit"));
		addAttr(new Attr(Types.SqlQuery, "beanQueryOffset"));
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
