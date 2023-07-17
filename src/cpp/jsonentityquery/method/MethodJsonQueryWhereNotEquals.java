package cpp.jsonentityquery.method;

import cpp.Types;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.Type;
import cpp.core.expression.CreateObjectExpression;
import cpp.core.expression.Expressions;
import cpp.core.instruction.IfBlock;
import cpp.entity.Nullable;
import cpp.jsonentity.JsonEntity;
import cpp.lib.ClsBaseJsonEntitySelectQuery;
import database.column.Column;

public class MethodJsonQueryWhereNotEquals extends Method {

	Column col;
	Param pValue;
	boolean nullableParam;
	boolean hasTableAlias;
	
	public MethodJsonQueryWhereNotEquals(Cls query,Column col, boolean nullableParam,boolean hasTableAlias ) {
		super(Public, query.toRef(), getMethodName(col));
		this.col = col;
		this.nullableParam = nullableParam;
		Type t=JsonEntity.getDatabaseMapper().columnToType(col,nullableParam);
		this.pValue = addParam(t.isPrimitiveType() ? t : t.toConstRef(), col.getCamelCaseName());
		this.hasTableAlias = hasTableAlias;
	}

	public static String getMethodName(Column col) {
		return "where"+col.getUc1stCamelCaseName() +"NotEquals";
	}

	@Override
	public void addImplementation() {
		String alias=hasTableAlias ? "e1."  : "";
		if(nullableParam) {
			IfBlock ifNotIsNull= _if(Expressions.not(pValue.callMethod(Nullable.isNull)));
			ifNotIsNull.thenBlock()._callMethodInstr(_this(), ClsBaseJsonEntitySelectQuery.where,QString.fromStringConstant(alias+col.getEscapedName()+"!=?") ,new CreateObjectExpression(Types.QVariantList).binOp("<<", new CreateObjectExpression(Types.QVariant,this.pValue.callMethod(Nullable.val))));
			ifNotIsNull.elseBlock()._callMethodInstr(_this(), ClsBaseJsonEntitySelectQuery.where,QString.fromStringConstant(alias+col.getEscapedName()+" is not null")  );
		} else {
			_callMethodInstr(_this(), ClsBaseJsonEntitySelectQuery.where,QString.fromStringConstant(alias+col.getEscapedName()+"!=?") ,new CreateObjectExpression(Types.QVariantList).binOp("<<", new CreateObjectExpression(Types.QVariant,this.pValue)));
		}
		_return(_this().dereference());
	}

}
