package php.cls.bean.repo.method;

import php.PhpFunctions;
import php.Types;
import php.cls.Method;
import php.cls.Param;
import php.cls.bean.BeanCls;
import php.cls.expression.Expressions;
import php.cls.expression.PhpStringLiteral;
import php.cls.instruction.IfBlock;

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
