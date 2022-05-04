package php.entityrepository.query.method;

import database.column.Column;
import php.core.Param;
import php.core.PhpFunctions;
import php.core.Type;
import php.core.Types;
import php.core.expression.Expression;
import php.core.expression.InlineIfExpression;
import php.core.expression.ParenthesesExpression;
import php.core.expression.PhpStringLiteral;
import php.core.method.Method;
import php.entity.EntityCls;
import php.entityrepository.query.ClsEntityQuery;
import php.lib.ClsBaseEntityQuery;
import php.lib.ClsSqlQuery;
import php.lib.ClsSqlUtil;

public class MethodEntityQueryWhereIn extends Method {
	EntityCls bean;
	Param pValue ;
	Column c;
	public MethodEntityQueryWhereIn(ClsEntityQuery query, EntityCls bean,Column c) {
		super(Public, query, "where"+c.getUc1stCamelCaseName()+"In");
		this.bean=bean;
		Type t = EntityCls.getTypeMapper().columnToType(c);
		
		if(t == null) {
			System.out.println(EntityCls.getTypeMapper().columnToType(c));
		}
		
		pValue = addParam(new Param(Types.array(t), "value"));
		this.c = c;
	}

	@Override
	public void addImplementation() {
		Expression aSqlQuery = _this().accessAttr(ClsBaseEntityQuery.sqlQuery);
		
		_return( _this().callMethod(ClsBaseEntityQuery.where, new ParenthesesExpression( new InlineIfExpression(aSqlQuery.callMethod(ClsSqlQuery.getMode)._equals(Types.SqlQuery.accessConstant(ClsSqlQuery.MODE_SELECT)),new PhpStringLiteral( "e1." + c.getEscapedName()+" in "),new PhpStringLiteral(c.getEscapedName()+" in "))).concat(Types.SqlUtil.callStaticMethod(ClsSqlUtil.getPlaceholders, PhpFunctions.count.call(pValue))), pValue  ));
		
	}
	@Override
	public boolean includeIfEmpty() {
		return true;
	}
}
