package sunjava.beanrepository.method;

import sunjava.bean.BeanCls;
import sunjava.core.JavaString;
import sunjava.core.Method;
import sunjava.core.Param;
import sunjava.core.Types;
import sunjava.lib.ClsJavaString;

public class MethodGetTableNameAlias extends Method{
	protected BeanCls bean;
	
	public MethodGetTableNameAlias(BeanCls bean) {
		super(Method.Public, Types.String, "getTableName" +bean.getName());
		addParam(new Param(Types.String, "alias"));
		setStatic(true);
		this.bean = bean;
	}

	@Override
	public void addImplementation() {
		_return(Types.String.callStaticMethod(ClsJavaString.format, JavaString.stringConstant(BeanCls.getDatabase().getEscapedTableName(bean.getTbl())+" %s"),getParam("alias"))); 
		//_return(JavaString.fromExpression(aTableName).concat(QChar.fromChar(' ')).concat(JavaString.fromExpression(getParam("alias"))));
	}

}
