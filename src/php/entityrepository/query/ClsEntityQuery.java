package php.entityrepository.query;

import database.column.Column;
import php.core.PhpCls;
import php.entity.EntityCls;
import php.entityrepository.query.method.ConstructorEntityQuery;
import php.entityrepository.query.method.MethodAddRelatedTableJoins;
import php.entityrepository.query.method.MethodEntityQueryFetch;
import php.entityrepository.query.method.MethodEntityQueryFetchOne;
import php.entityrepository.query.method.MethodEntityQueryWhereEquals;
import php.entityrepository.query.method.MethodEntityQueryWhereIn;
import php.entityrepository.query.method.MethodEntityQueryWhereIsNull;
import php.entityrepository.query.method.MethodEntityQueryWhereNotEquals;
import php.entityrepository.query.method.MethodEntityQueryWhereNotIn;
import php.entityrepository.query.method.MethodGetAllSelectFields;
import php.entityrepository.query.method.MethodGetTableName;
import php.entityrepository.query.method.MethodLimitAndOffset;
import php.lib.ClsBaseEntityQuery;

public class ClsEntityQuery extends PhpCls {

	protected static String beanQueryNamespace;
	
	public static void setBeanQueryNamespace(String beanQueryNamespace) {
		ClsEntityQuery.beanQueryNamespace = beanQueryNamespace;
	}
	
	public ClsEntityQuery(EntityCls cls) {
		super(cls.getName()+ "EntityQuery",beanQueryNamespace);
		
		setSuperclass(new ClsBaseEntityQuery(cls));
		setConstructor(new ConstructorEntityQuery());
		addMethod(new MethodEntityQueryFetch(cls));
		addMethod(new MethodEntityQueryFetchOne(cls));
		addMethod(new MethodAddRelatedTableJoins(cls));
		addMethod(new MethodGetAllSelectFields(cls));
		addMethod(new MethodGetTableName(cls));
		addMethod(new MethodLimitAndOffset(cls,this));
		getMethod(ClsBaseEntityQuery.select).setReturnType(this);
		getMethod(ClsBaseEntityQuery.where).setReturnType(this);
		for(Column c : cls.getTbl().getAllColumns()) {
			addMethod(new MethodEntityQueryWhereEquals(this, cls, c));
			addMethod(new MethodEntityQueryWhereIn(this, cls, c));
			addMethod(new MethodEntityQueryWhereNotEquals(this, cls, c));
			addMethod(new MethodEntityQueryWhereNotIn(this, cls, c));
			
			if(c.isNullable()) {
				addMethod(new MethodEntityQueryWhereIsNull(this, cls, c));
			}
		}
	}


}
