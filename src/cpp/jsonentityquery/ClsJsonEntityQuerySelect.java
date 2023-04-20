package cpp.jsonentityquery;

import cpp.JsonTypes;
import cpp.NetworkTypes;
import cpp.Types;
import cpp.core.Cls;
import cpp.core.Constructor;
import cpp.jsonentity.JsonEntity;
import cpp.jsonentityquery.method.MethodJsonQuery;
import cpp.jsonentityquery.method.MethodJsonQueryJoin;
import cpp.jsonentityquery.method.MethodJsonQueryOne;
import cpp.jsonentityquery.method.MethodJsonQueryWhere;
import cpp.jsonentityquery.method.MethodJsonQueryWhereEquals;
import cpp.jsonentityquery.method.MethodJsonQueryWhereIsNull;
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
			addIncludeDefaultHeaderFileName(Types.nullable(null)) ;
		}
		
		for(Column c : entity.getTbl().getAllColumns()) {
			addMethod(new MethodJsonQueryWhereEquals(this, c,false));
			if(c.isNullable()) {
				addMethod(new MethodJsonQueryWhereEquals(this, c,true));
				addMethod(new MethodJsonQueryWhereIsNull(this, c));
			}
		}
		addIncludeLib("memory");
		addForwardDeclaredClass(entity);
		addIncludeLibInSource(NetworkTypes.QUrl);
		addIncludeLibInSource(NetworkTypes.QUrlQuery);
		addIncludeHeaderInSource(JsonTypes.JsonEntityRepository.getHeaderInclude());
		addMethod(new MethodJsonQueryWhere(this));
		addMethod(new MethodJsonQueryJoin(this));
		addMethod(new MethodJsonQueryOne(entity));
		addMethod(new MethodJsonQuery(entity));
		headerInclude = "repository/query/"+getName().toLowerCase()+".h";
	}

}
