package php.rest.method;

import java.util.Collection;

import php.bean.BeanCls;
import php.bean.method.MethodGetFieldsAsAssocArray;
import php.core.PhpConstants;
import php.core.PhpFunctions;
import php.core.PhpGlobals;
import php.core.Types;
import php.core.expression.Expression;
import php.core.expression.PhpStringLiteral;
import php.core.expression.Var;
import php.core.instruction.CaseBlock;
import php.core.instruction.SwitchBlock;
import php.core.method.Method;
import php.lib.ClsBaseBeanQuery;

public class RestMethodGetOne extends Method {

	Collection<BeanCls> beans;
	
	public RestMethodGetOne(Collection<BeanCls> beans) {
		super(Public, Types.Void, "getOne");
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
					.callMethod("fetchOne");
			Var vBean = caseBeanType._declare(e.getType(),"entities",e );
			caseBeanType.addInstr(PhpFunctions.echo(PhpFunctions.json_encode.call(vBean.callMethod(MethodGetFieldsAsAssocArray.METHOD_NAME), PhpConstants.JSON_UNESCAPED_UNICODE)));
			caseBeanType._break();
		}

	}

}
