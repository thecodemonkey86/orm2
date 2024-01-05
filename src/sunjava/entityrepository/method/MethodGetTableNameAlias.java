package sunjava.entityrepository.method;

import sunjava.core.JavaString;
import sunjava.core.Method;
import sunjava.core.Param;
import sunjava.core.Types;
import sunjava.entity.EntityCls;
import sunjava.lib.ClsJavaString;

public class MethodGetTableNameAlias extends Method{
	protected EntityCls entity;
	
	public MethodGetTableNameAlias(EntityCls entity) {
		super(Method.Public, Types.String, "getTableName" +entity.getName());
		addParam(new Param(Types.String, "alias"));
		setStatic(true);
		this.entity = entity;
	}

	@Override
	public void addImplementation() {
		_return(Types.String.callStaticMethod(ClsJavaString.format, JavaString.stringConstant(EntityCls.getDatabase().getEscapedTableName(entity.getTbl())+" %s"),getParam("alias"))); 
		//_return(JavaString.fromExpression(aTableName).concat(QChar.fromChar(' ')).concat(JavaString.fromExpression(getParam("alias"))));
	}

}
