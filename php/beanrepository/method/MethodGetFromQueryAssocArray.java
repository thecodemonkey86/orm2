package php.beanrepository.method;

import java.util.List;

import database.FirebirdDatabase;
import database.column.Column;
import php.bean.BeanCls;
import php.core.Param;
import php.core.PhpFunctions;
import php.core.Types;
import php.core.expression.BoolExpression;
import php.core.expression.Expression;
import php.core.expression.PhpStringLiteral;
import php.core.expression.Var;
import php.core.method.Method;
import php.core.expression.IntExpression;

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
		
		if(	this.beanCls.getName().equals("Beleg")) {
			System.out.println(parent.getName());
		}
		for(Column col:columns) {
			if (!col.isRelationDestColumn() || col.hasOneRelation()) {
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
					
					Expression exprArrayIndex = BeanCls.getTypeMapper().filterFetchAssocArrayKeyExpression(alias).concat(new PhpStringLiteral(BeanCls.getTypeMapper().filterFetchAssocArrayKey("__"+col.getName())));
					
					if(BeanCls.getDatabase() instanceof FirebirdDatabase) {
						exprArrayIndex = PhpFunctions.substr.call(exprArrayIndex,new IntExpression(0),new IntExpression(31));
					}
					
					addInstr(bean.getClassConcreteType().getMethod("set"+col.getUc1stCamelCaseName()+"Internal").call(bean, BeanCls.getTypeMapper().getConvertTypeExpression( array.arrayIndex(exprArrayIndex),col)).asInstruction());
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
