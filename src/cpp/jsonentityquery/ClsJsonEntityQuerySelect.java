package cpp.jsonentityquery;

import cpp.JsonTypes;
import cpp.NetworkTypes;
import cpp.Types;
import cpp.core.Cls;
import cpp.core.Constructor;
import cpp.core.Type;
import cpp.jsonentity.JsonEntity;
import cpp.jsonentityquery.method.MethodJsonQuery;
import cpp.jsonentityquery.method.MethodJsonQueryJoin;
import cpp.jsonentityquery.method.MethodJsonQueryOne;
import cpp.jsonentityquery.method.MethodJsonQueryOrderBy;
import cpp.jsonentityquery.method.MethodJsonQueryWhere;
import cpp.jsonentityquery.method.MethodJsonQueryWhereEquals;
import cpp.jsonentityquery.method.MethodJsonQueryWhereIsNotNull;
import cpp.jsonentityquery.method.MethodJsonQueryWhereIsNull;
import cpp.jsonentityquery.method.MethodJsonQueryWhereNotEquals;
import database.column.Column;

public class ClsJsonEntityQuerySelect extends Cls{

	public ClsJsonEntityQuerySelect(JsonEntity entity) {
		super(entity.getName() +"JsonEntityQuerySelect");
		addConstructor(new Constructor() {
			
			@Override
			public void addImplementation() {
			}
		});
		addSuperclass(JsonTypes.BaseJsonEntitySelectQuery);
		addIncludeDefaultHeaderFileName(getSuperclass());
		if(entity.getTbl().hasNullableColumn()) {
			addIncludeDefaultHeaderFileName(Types.optional(null)) ;
		}
		
		for(Column c : entity.getTbl().getAllColumns()) {
			addMethod(new MethodJsonQueryWhereEquals(this, c,false,true));
			addMethod(new MethodJsonQueryWhereNotEquals(this, c,false,true));
			if(c.isNullable()) {
				addMethod(new MethodJsonQueryWhereEquals(this, c,true,true));
				addMethod(new MethodJsonQueryWhereNotEquals(this, c,true,true));
				addMethod(new MethodJsonQueryWhereIsNull(this, c));
				addMethod(new MethodJsonQueryWhereIsNotNull(this, c));
			}
			Type colType = JsonEntity.getDatabaseMapper().columnToType(c);
			colType.collectIncludes(this,false);
		}
		addIncludeLibInSource("memory");
		addForwardDeclaredClass(entity);
		addIncludeLibInSource(NetworkTypes.QUrl);
		addIncludeLibInSource(NetworkTypes.QUrlQuery);
		addIncludeHeaderInSource(JsonTypes.JsonEntityRepository.getHeaderInclude());
		addMethod(new MethodJsonQueryWhere(this));
		addMethod(new MethodJsonQueryJoin(this));
		addMethod(new MethodJsonQueryOrderBy(this));
		addMethod(new MethodJsonQueryOne(entity));
		addMethod(new MethodJsonQuery(entity));
		headerInclude = "repository/query/"+getName().toLowerCase()+".h";
	}

}
