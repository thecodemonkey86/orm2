package cpp.jsonentityquery;

import cpp.JsonTypes;
import cpp.NetworkTypes;
import cpp.Types;
import cpp.core.Cls;
import cpp.core.Constructor;
import cpp.jsonentity.JsonEntity;
import cpp.jsonentityquery.method.MethodJsonQueryExecute;
import cpp.jsonentityquery.method.MethodJsonQueryWhere;
import cpp.jsonentityquery.method.MethodJsonQueryWhereEquals;
import cpp.jsonentityquery.method.MethodJsonQueryWhereIsNull;
import database.column.Column;

public class ClsJsonEntityQueryDelete extends Cls{
	
	public ClsJsonEntityQueryDelete(JsonEntity entity) {
		super(entity.getName() +"JsonEntityQueryDelete");
		addConstructor(new Constructor() {
			
			@Override
			public void addImplementation() {
			}
		});
		addSuperclass(JsonTypes.BaseJsonEntityDeleteQuery);
		addIncludeDefaultHeaderFileName(getSuperclass());
		if(entity.getTbl().hasNullableColumn()) {
			addIncludeDefaultHeaderFileName(Types.optional(null)) ;
		}
		
		for(Column c : entity.getTbl().getAllColumns()) {
			addMethod(new MethodJsonQueryWhereEquals(this, c,false,false));
			if(c.isNullable()) {
				addMethod(new MethodJsonQueryWhereEquals(this, c,true,false));
				addMethod(new MethodJsonQueryWhereIsNull(this, c));
			}
		}
		addIncludeLib("memory");
		addForwardDeclaredClass(entity);
		addIncludeLibInSource(NetworkTypes.QUrl);
		addIncludeLibInSource(NetworkTypes.QUrlQuery);
		addIncludeHeaderInSource(JsonTypes.JsonEntityRepository.getHeaderInclude());	
		addMethod(new MethodJsonQueryWhere(this));
		addMethod(new MethodJsonQueryExecute(entity));
		headerInclude = "repository/query/"+getName().toLowerCase()+".h";
	}

}
