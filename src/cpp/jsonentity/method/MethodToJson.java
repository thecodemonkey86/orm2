package cpp.jsonentity.method;

import cpp.JsonTypes;
import cpp.core.Method;
import cpp.core.QString;
import cpp.core.expression.CreateObjectExpression;
import cpp.core.expression.Expression;
import cpp.core.expression.Operators;
import cpp.core.expression.Var;
import cpp.core.instruction.IfBlock;
import cpp.entity.Nullable;
import cpp.jsonentity.JsonEntity;
import cpp.lib.ClsBaseJsonEntity;
import cpp.lib.ClsQJsonDocument;
import cpp.lib.ClsQJsonObject;
import cpp.orm.JsonOrmUtil;
import database.column.Column;

public class MethodToJson extends Method {

	JsonEntity entity;

	public MethodToJson(JsonEntity entity) {
		super(Public, JsonTypes.QJsonDocument, getMethodName());
		
		this.entity = entity;
		setConstQualifier();
	}

	@Override
	public void addImplementation() {
		
		JsonEntity parent = (JsonEntity) this.parent;
		Expression _this=_this();
		Var d = _declareInitConstructor(JsonTypes.QJsonDocument, "_d");
		Var o = _declareInitConstructor(JsonTypes.QJsonObject, "_o");
		
		addInstr(o.callMethodInstruction(ClsQJsonObject.insert,QString.fromStringConstant(ClsBaseJsonEntity.insert),_this.accessAttr(ClsBaseJsonEntity.insert)));
		
		IfBlock ifInsert=_if(_this.accessAttr(ClsBaseJsonEntity.insert));
		for(Column pkCol : entity.getTbl().getPrimaryKey()) {
			 addInstr(o.callMethodInstruction(ClsQJsonObject.insert,QString.fromStringConstant(pkCol.getName()), JsonOrmUtil.convertToQJsonValue(
					_this.accessAttr(pkCol.getCamelCaseName()))));
			
			ifInsert.elseBlock()._if(_this.accessAttr(ClsBaseJsonEntity.primaryKeyModified)).thenBlock().addInstr(o.callMethodInstruction(ClsQJsonObject.insert,QString.fromStringConstant(pkCol.getName()+"Previous"), JsonOrmUtil.convertToQJsonValue(
					_this.accessAttr(pkCol.getCamelCaseName()+"Previous"))));
		}
		
		
		for (Column col : entity.getTbl().getColumnsWithoutPrimaryKey()) {
				IfBlock ifFieldModfied= _if(_this.accessAttr(ClsBaseJsonEntity.insert).binOp(Operators.OR, parent.getAttrByName(col.getCamelCaseName()+"Modified")));
					
				if (col.isNullable()) {
					IfBlock ifValueIsNull = ifFieldModfied.thenBlock()._ifNot(
							_this.callAttrGetter(col.getCamelCaseName())
									.callMethod(Nullable.isNull));
					// ifValueIsNull.thenBlock().addInstr(
					// e1.callMethodInstruction(MethodColumnAttrSetNull.getMethodName(col)));
					ifValueIsNull.thenBlock().addInstr(o.callMethodInstruction(ClsQJsonObject.insert,QString.fromStringConstant(col.getName()), JsonOrmUtil.convertToQJsonValue(
							_this.callAttrGetter(col.getCamelCaseName())
							.callMethod(Nullable.val))));
					ifValueIsNull.elseBlock().addInstr(o.callMethodInstruction(ClsQJsonObject.insert,QString.fromStringConstant(col.getName()), new CreateObjectExpression(JsonTypes.QJsonValue)));
				} else {
					 ifFieldModfied.thenBlock().addInstr(o.callMethodInstruction(ClsQJsonObject.insert,QString.fromStringConstant(col.getName()), JsonOrmUtil.convertToQJsonValue(
							_this.callAttrGetter(col.getCamelCaseName()))));
				}
		}
		 
		addInstr(d.callMethodInstruction(ClsQJsonDocument.setObject, o));
		_return(d);
		

	}

	public static String getMethodName() {
		return "toJson";
	}
}
