package php.rest.method;

import java.util.Collection;

import php.bean.BeanCls;
import php.bean.method.MethodGetFieldsAsAssocStringArray;
import php.core.PhpFunctions;
import php.core.PhpGlobals;
import php.core.Types;
import php.core.expression.ArrayInitExpression;
import php.core.expression.Expression;
import php.core.expression.PhpStringLiteral;
import php.core.expression.Var;
import php.core.instruction.CaseBlock;
import php.core.instruction.ForeachLoop;
import php.core.instruction.SwitchBlock;
import php.core.method.Method;
import php.lib.ClsBaseBeanQuery;

public class RestMethodGet extends Method {

	Collection<BeanCls> beans;
	
	public RestMethodGet(Collection<BeanCls> beans) {
		super(Public, Types.Void, "get");
		setStatic(true);
		this.beans = beans;
	}

	@Override
	public void addImplementation() {
		SwitchBlock switchEntityType = _switch(PhpGlobals.$_GET.arrayIndex(new PhpStringLiteral("entityType")));
		for(BeanCls bean : beans) {
			CaseBlock caseBeanType = switchEntityType._case(new PhpStringLiteral(bean.getName()));
			
			Expression e = Types.BeanRepository.callStaticMethod("createQuery"+bean.getName())
					.callMethod(ClsBaseBeanQuery.select)
					.callMethod("fetch");
			Var vBeans = caseBeanType._declare(e.getType(),"beans",e );
			Var vResult = caseBeanType._declare(Types.array(Types.String), "result", new ArrayInitExpression());
			ForeachLoop foreachBean = caseBeanType._foreach(new Var(bean, "bean"), vBeans);
			foreachBean.addInstr( vResult.arrayPush(foreachBean.getVar().callMethod(MethodGetFieldsAsAssocStringArray.METHOD_NAME)));
			caseBeanType.addInstr(PhpFunctions.echo(PhpFunctions.json_encode.call(vResult)));
			caseBeanType._break();
		}

	}

}
