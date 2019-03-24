package cpp.jsonentityrepository.method;

import cpp.JsonTypes;
import cpp.Types;
import cpp.bean.Nullable;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.SharedPtr;
import cpp.core.expression.Var;
import cpp.core.instruction.IfBlock;
import cpp.jsonentity.JsonEntity;
import cpp.lib.ClsQJsonDocument;
import cpp.lib.ClsQJsonObject;
import cpp.lib.ClsQJsonValue;
import cpp.orm.JsonOrmUtil;
import database.column.Column;

public class MethodGetFromJson extends Method{

	Param pJson;
	JsonEntity entity;
	
	public MethodGetFromJson(JsonEntity entity) {
		super(Public, entity.toSharedPtr(), getMethodName(entity));
		pJson = addParam(Types.QByteArray.toConstRef(), "jsondata");
		this.entity = entity;
		setStatic(true);
	}

	@Override
	public void addImplementation() {
		Var b1 = _declareMakeShared((Cls)((SharedPtr)returnType).getElementType(), "b1");
		Var jsondoc = _declare(JsonTypes.QJsonDocument, "jsondoc",JsonTypes.QJsonDocument.callStaticMethod(ClsQJsonDocument.fromJson, pJson));
		Var jsonobject = _declare(JsonTypes.QJsonObject, "jsonobject", jsondoc.callMethod(ClsQJsonDocument.object));
		
		for(Column col : entity.getTbl().getAllColumns()) {
			if(col.isNullable()) {
				IfBlock ifValueIsNull = _ifNot(jsonobject.callMethod(ClsQJsonObject.value, QString.fromStringConstant(col.getName())).callMethod(ClsQJsonValue.isNull));
				//ifValueIsNull.thenBlock().addInstr( b1.callMethodInstruction(MethodColumnAttrSetNull.getMethodName(col)));
				ifValueIsNull.thenBlock().addInstr( b1.callSetterMethodInstruction(col.getCamelCaseName(), JsonOrmUtil.jsonConvertMethod(jsonobject.callMethod(ClsQJsonObject.value, QString.fromStringConstant(col.getName())), ((Nullable)( ((Cls)b1.getType()).getAttrByName(col.getCamelCaseName())).getType()).getElementType())));
			} else {
			
				addInstr( b1.callSetterMethodInstruction(col.getCamelCaseName(), JsonOrmUtil.jsonConvertMethod(jsonobject.callMethod(ClsQJsonObject.value, QString.fromStringConstant(col.getName())), ((Cls)b1.getType()).getAttrByName(col.getCamelCaseName()).getType())));
			}
		}
		
		_return(b1);
	}

	public static String getMethodName(JsonEntity entity) {
		return "get"+entity.getName()+"FromJson";
	}
}
