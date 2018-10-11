package cpp.jsonentityrepository.method;

import cpp.JsonTypes;
import cpp.Types;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.SharedPtr;
import cpp.core.expression.Var;
import cpp.jsonentity.JsonEntity;
import cpp.lib.ClsQJsonDocument;
import cpp.lib.ClsQJsonObject;
import database.column.Column;

public class MethodGetFromJson extends Method{

	Param pJson;
	JsonEntity entity;
	
	public MethodGetFromJson(JsonEntity entity) {
		super(Public, entity.toSharedPtr(), "get"+entity.getName()+"FromJson");
		pJson = addParam(Types.QByteArray, "jsondata");
		this.entity = entity;
	}

	@Override
	public void addImplementation() {
		Var b1 = _declareMakeShared((Cls)((SharedPtr)returnType).getElementType(), "b1");
		Var jsondoc = _declare(JsonTypes.QJsonDocument, "jsondoc",JsonTypes.QJsonDocument.callStaticMethod(ClsQJsonDocument.fromJson, pJson));
		Var jsonobject = _declare(JsonTypes.QJsonObject, "jsonobject", jsondoc.callMethod(ClsQJsonDocument.object));
		
		for(Column col : entity.getTbl().getAllColumns()) {
			b1.callSetterMethodInstruction(col.getCamelCaseName(), jsonobject.callMethod(ClsQJsonObject.value, QString.fromStringConstant(col.getName())));
		}
		
		_return(b1);
	}

}
