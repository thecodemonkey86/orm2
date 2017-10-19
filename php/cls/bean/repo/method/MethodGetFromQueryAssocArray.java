package php.cls.bean.repo.method;

import java.util.List;

import model.Column;
import php.PhpFunctions;
import php.Types;
import php.cls.Method;
import php.cls.Param;
import php.cls.bean.BeanCls;
import php.cls.expression.BoolExpression;
import php.cls.expression.Expression;
import php.cls.expression.PhpStringLiteral;
import php.cls.expression.Var;
import php.cls.instruction.IfBlock;

public class MethodGetFromQueryAssocArray extends Method{
	protected List<Column> columns;
	protected BeanCls beanCls;
	
	public static String getMethodName(BeanCls bean) {
		return "get"+bean.getName()+ "FromQueryAssocArray";
	}
	
	public MethodGetFromQueryAssocArray(BeanCls bean) {
		super(Public, bean, getMethodName(bean));
		setStatic(true);
		addParam(new Param(Types.array(Types.String).toRef(), "array"));
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
		Var bean = _declareNew(returnType, "bean", BoolExpression.FALSE);
		Param array = getParam("array");
		Param alias = getParam("alias");
		for(Column col:columns) {
			if (!col.hasRelation()) {
				try{
					/*Expression resultSetValueGetter = BeanCls.getTypeMapper().getResultSetValueGetter(resultSet, col, alias);
					if (col.isNullable() && resultSetValueGetter.getType().isPrimitiveType()) {
						Var value = _declare(resultSetValueGetter.getType(), "value"+col.getUc1stCamelCaseName(),resultSetValueGetter );
						IfBlock ifWasNull = _if(resultSet.callMethod(ClsResultSet.wasNull));
							ifWasNull.thenBlock()
							.addInstr(bean.getClassConcreteType().getMethod("set"+col.getUc1stCamelCaseName()+"Internal").call(bean, Expressions.Null).asInstruction());
							
							ifWasNull.elseBlock()
							.addInstr(bean.getClassConcreteType().getMethod("set"+col.getUc1stCamelCaseName()+"Internal").call(bean, value).asInstruction());
					} else {
						
					}*/
					
					addInstr(bean.getClassConcreteType().getMethod("set"+col.getUc1stCamelCaseName()+"Internal").call(bean, array.arrayIndex( alias.concat(new PhpStringLiteral("__"+col.getName())))).asInstruction());
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
