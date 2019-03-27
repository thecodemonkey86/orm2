package cpp.jsonentityrepository.method;

import cpp.JsonTypes;
import cpp.Types;
import cpp.bean.Nullable;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.SharedPtr;
import cpp.core.expression.Expression;
import cpp.core.expression.Var;
import cpp.core.instruction.IfBlock;
import cpp.jsonentity.JsonEntity;
import cpp.lib.ClsQJsonDocument;
import cpp.lib.ClsQJsonObject;
import cpp.lib.ClsQJsonValue;
import cpp.orm.JsonOrmUtil;
import database.column.Column;

public class MethodGetOneFromJson extends Method {

	Param pJson;
	Param pJsonObject;
	JsonEntity entity;
	boolean overloadedQByteArray;

	public MethodGetOneFromJson(JsonEntity entity, boolean overloadedQByteArray) {
		super(Public, entity.toSharedPtr(), getMethodName(entity));
		this.overloadedQByteArray = overloadedQByteArray;
		if (overloadedQByteArray) {
			pJson = addParam(Types.QByteArray.toConstRef(), "jsondata");
		} else {
			pJsonObject = addParam(JsonTypes.QJsonObject.toConstRef(), "jsonobject");
		}
		this.entity = entity;
		setStatic(true);
	}

	@Override
	public void addImplementation() {
		Var b1 = _declareMakeShared((Cls) ((SharedPtr) returnType).getElementType(), "b1");
		if (overloadedQByteArray) {
			Var jsondoc = _declare(JsonTypes.QJsonDocument, "jsondoc",
					JsonTypes.QJsonDocument.callStaticMethod(ClsQJsonDocument.fromJson, pJson));
			_return(parent.callStaticMethod(getMethodName(entity),
					_declare(JsonTypes.QJsonObject, "jsonobject", jsondoc.callMethod(ClsQJsonDocument.object))));
		} else {
			for (Column col : entity.getTbl().getAllColumns()) {
				if (col.isNullable()) {
					IfBlock ifValueIsNull = _ifNot(
							pJsonObject.callMethod(ClsQJsonObject.value, QString.fromStringConstant(col.getName()))
									.callMethod(ClsQJsonValue.isNull));
					// ifValueIsNull.thenBlock().addInstr(
					// b1.callMethodInstruction(MethodColumnAttrSetNull.getMethodName(col)));
					ifValueIsNull.thenBlock().addInstr(b1.callSetterMethodInstruction(col.getCamelCaseName(),
							JsonOrmUtil.jsonConvertMethod(
									pJsonObject.callMethod(ClsQJsonObject.value,
											QString.fromStringConstant(col.getName())),
									((Nullable) (((Cls) b1.getType()).getAttrByName(col.getCamelCaseName())).getType())
											.getElementType())));
				} else {

					addInstr(b1.callSetterMethodInstruction(col.getCamelCaseName(),
							JsonOrmUtil.jsonConvertMethod(
									pJsonObject.callMethod(ClsQJsonObject.value,
											QString.fromStringConstant(col.getName())),
									((Cls) b1.getType()).getAttrByName(col.getCamelCaseName()).getType())));
				}
			}

			_return(b1);
		}

	}

	public static String getMethodName(JsonEntity entity) {
		return "getOne" + entity.getName() + "FromJson";
	}
}
