package sunjava.cls.bean.repo.method;

import java.util.List;

import model.Column;
import sunjava.Types;
import sunjava.cls.Method;
import sunjava.cls.Param;
import sunjava.cls.bean.BeanCls;
import sunjava.cls.expression.BoolExpression;
import sunjava.cls.expression.Expression;
import sunjava.cls.expression.Expressions;
import sunjava.cls.expression.Var;
import sunjava.cls.instruction.IfBlock;
import sunjava.lib.ClsResultSet;

public class MethodGetFromResultSet extends Method {
	protected List<Column> columns;
	protected BeanCls beanCls;
	
	public static String getMethodName(BeanCls bean) {
		return "get"+bean.getName()+ "FromResultSet";
	}
	
	public MethodGetFromResultSet(BeanCls bean) {
		super(Public, bean, getMethodName(bean));
		setStatic(true);
		addParam(new Param(Types.ResultSet, "resultSet"));
		addParam(new Param(Types.String, "alias"));
		this.columns = bean.getTbl().getColumns(true);
		this.beanCls = bean;
	}

	@Override
	public boolean includeIfEmpty() {
		return true;
	}
	
	@Override
	public void addImplementation() {
		addThrowsException(Types.SqlException);
		Var bean = _declareNew(returnType, "bean", BoolExpression.FALSE);
		Param resultSet = getParam("resultSet");
		Param alias = getParam("alias");
		for(Column col:columns) {
			if (!col.hasRelation()) {
				try{
					Expression resultSetValueGetter = BeanCls.getTypeMapper().getResultSetValueGetter(resultSet, col, alias);
					if (col.isNullable() && resultSetValueGetter.getType().isPrimitiveType()) {
						Var value = _declare(resultSetValueGetter.getType(), "value"+col.getUc1stCamelCaseName(),resultSetValueGetter );
						IfBlock ifWasNull = _if(resultSet.callMethod(ClsResultSet.wasNull));
							ifWasNull.thenBlock()
							.addInstr(bean.getClassConcreteType().getMethod("set"+col.getUc1stCamelCaseName()+"Internal").call(bean, Expressions.Null).asInstruction());
							
							ifWasNull.elseBlock()
							.addInstr(bean.getClassConcreteType().getMethod("set"+col.getUc1stCamelCaseName()+"Internal").call(bean, value).asInstruction());
					} else {
						addInstr(bean.getClassConcreteType().getMethod("set"+col.getUc1stCamelCaseName()+"Internal").call(bean, resultSetValueGetter).asInstruction());
					}
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(parent);
				}
			}
		}
		//addInstr(bean.assignAttr("insert",BoolExpression.FALSE));
		_return(bean);
	}

}
