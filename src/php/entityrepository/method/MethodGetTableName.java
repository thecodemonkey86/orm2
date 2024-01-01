package php.entityrepository.method;

import php.core.Param;
import php.core.PhpFunctions;
import php.core.Types;
import php.core.expression.Expressions;
import php.core.expression.PhpStringLiteral;
import php.core.instruction.IfBlock;
import php.core.method.Method;
import php.entity.EntityCls;

public class MethodGetTableName extends Method{
	protected EntityCls entity;
	
	public MethodGetTableName(EntityCls entity) {
		super(Method.Public, Types.String, getMethodName(entity));
		addParam(new Param(Types.String, "alias", Expressions.Null));
		setStatic(true);
		this.entity = entity;
	}

	@Override
	public void addImplementation() {
		IfBlock ifAliasIsNotNull = _if(getParam("alias").isNotNull());
		ifAliasIsNotNull.thenBlock().
		_return(PhpFunctions.sprintf.call( new PhpStringLiteral(EntityCls.getDatabase().getEscapedTableName(entity.getTbl())+" %s"),getParam("alias"))); 
		ifAliasIsNotNull.elseBlock()
		._return(new PhpStringLiteral(EntityCls.getDatabase().getEscapedTableName(entity.getTbl())));
	}

	public static String getMethodName(EntityCls entity) {
		return "getTableName"+ entity.getName();
	}
}
