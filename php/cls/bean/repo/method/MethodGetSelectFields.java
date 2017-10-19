package php.cls.bean.repo.method;

import java.util.ArrayList;

import model.Column;
import php.PhpFunctions;
import php.Types;
import php.cls.Method;
import php.cls.Param;
import php.cls.bean.BeanCls;

import php.cls.expression.PhpStringLiteral;

public class MethodGetSelectFields extends Method  {

	protected BeanCls bean;
	
	public MethodGetSelectFields(BeanCls bean) {
		super(Public, Types.String, getMethodName(bean));
		setStatic(true);
		addParam(new Param(Types.String, "alias", new PhpStringLiteral("b1")));
		this.bean = bean;
	}

	@Override
	public void addImplementation() {
		ArrayList<Column> cols = bean.getTbl().getAllColumns(); 
		String sprintfTmpl = "%1$s." + cols.get(0).getEscapedName() + " as %1$s__" + cols.get(0).getName();

		for(int i=1;i<cols.size();i++) {
			sprintfTmpl = sprintfTmpl + "," + "%1$s." + cols.get(i).getEscapedName() + " as %1$s __" + cols.get(i).getName();
		}
		
		_return (PhpFunctions.sprintf.call(new PhpStringLiteral(sprintfTmpl),getParam("alias")));
	}

	public static String getMethodName(BeanCls bean) {
		return "getSelectFields"+ bean.getName();
	}
}
