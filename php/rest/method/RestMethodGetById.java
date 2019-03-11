package php.rest.method;

import java.util.Collection;

import database.column.Column;
import database.relation.PrimaryKey;
import php.bean.BeanCls;
import php.bean.method.MethodGetFieldsAsAssocStringArray;
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

public class RestMethodGetById extends Method {

	Collection<BeanCls> beans;
	
	public RestMethodGetById(Collection<BeanCls> beans) {
		super(Public, Types.Void, "getById");
		setStatic(true);
		this.beans = beans;
	}

	@Override
	public void addImplementation() {
		SwitchBlock switchEntityType = _switch(PhpGlobals.$_GET.arrayIndex(new PhpStringLiteral("entityType")));
		for(BeanCls bean : beans) {
			CaseBlock caseBeanType = switchEntityType._case(new PhpStringLiteral(bean.getName()));
			
			PrimaryKey pk= bean.getTbl().getPrimaryKey();
			
			Expression e = Types.BeanRepository.callStaticMethod("createQuery"+bean.getName())
					.callMethod(ClsBaseBeanQuery.select);
				
			for(Column pkCol : pk) {
				e = e.callMethod(ClsBaseBeanQuery.where, new PhpStringLiteral(pkCol.getEscapedName()+ "=?"), PhpGlobals.$_GET.arrayIndex(new PhpStringLiteral(pkCol.getName() )));
			}
			
			e = e.callMethod("fetchOne");
			Var vBean = caseBeanType._declare(e.getType(),"bean",e );
			caseBeanType.addInstr(PhpFunctions.echo(PhpFunctions.json_encode.call(vBean.callMethod(MethodGetFieldsAsAssocStringArray.METHOD_NAME))));
			caseBeanType._break();
		}


	}

}
