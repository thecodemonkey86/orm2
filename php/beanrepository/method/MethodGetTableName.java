package php.beanrepository.method;

import php.bean.BeanCls;
import php.core.Param;
import php.core.PhpFunctions;
import php.core.Types;
import php.core.expression.Expressions;
import php.core.expression.PhpStringLiteral;
import php.core.instruction.IfBlock;
import php.core.method.Method;

public class MethodGetTableName extends Method{
	protected BeanCls bean;
	
	public MethodGetTableName(BeanCls bean) {
		super(Method.Public, Types.String, getMethodName(bean));
		addParam(new Param(Types.String, "alias", Expressions.Null));
		setStatic(true);
		this.bean = bean;
	}

	@Override
	public void addImplementation() {
		IfBlock ifAliasIsNotNull = _if(getParam("alias").isNotNull());
		ifAliasIsNotNull.thenBlock().
		_return(PhpFunctions.sprintf.call( new PhpStringLiteral(BeanCls.getDatabase().getEscapedTableName(bean.getTbl())+" %s"),getParam("alias"))); 
		ifAliasIsNotNull.elseBlock()
		._return(new PhpStringLiteral(BeanCls.getDatabase().getEscapedTableName(bean.getTbl())));
	}

	public static String getMethodName(BeanCls bean) {
		return "getTableName"+ bean.getName();
	}
}
