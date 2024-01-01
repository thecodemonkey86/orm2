package php.entityrepository.method;

import java.util.ArrayList;

import database.column.Column;
import php.core.Param;
import php.core.PhpFunctions;
import php.core.Types;
import php.core.expression.PhpStringLiteral;
import php.core.method.Method;
import php.entity.EntityCls;

public class MethodGetSelectFields extends Method  {

	protected EntityCls entity;
	
	public MethodGetSelectFields(EntityCls entity) {
		super(Public, Types.String, getMethodName(entity));
		setStatic(true);
		addParam(new Param(Types.String, "alias", new PhpStringLiteral("e1")));
		this.entity = entity;
	}

	@Override
	public void addImplementation() {
		ArrayList<Column> cols = entity.getTbl().getAllColumns(); 
		String sprintfTmpl = "%1$s." + cols.get(0).getEscapedName() + " as %1$s__" + cols.get(0).getName();

		for(int i=1;i<cols.size();i++) {
			sprintfTmpl = sprintfTmpl + "," + "%1$s." + cols.get(i).getEscapedName() + " as %1$s __" + cols.get(i).getName();
		}
		
		_return (PhpFunctions.sprintf.call(new PhpStringLiteral(sprintfTmpl),getParam("alias")));
	}

	public static String getMethodName(EntityCls entity) {
		return "getSelectFields"+ entity.getName();
	}
}
