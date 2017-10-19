package sunjava.cls.bean.repo.method;

import sunjava.Types;
import sunjava.cls.JavaString;
import sunjava.cls.Method;
import sunjava.cls.Param;
import sunjava.cls.bean.BeanCls;
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
