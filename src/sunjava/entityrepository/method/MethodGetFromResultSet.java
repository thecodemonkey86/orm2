package sunjava.entityrepository.method;

import java.util.List;

import database.column.Column;
import sunjava.core.Method;
import sunjava.core.Param;
import sunjava.core.Types;
import sunjava.core.expression.BoolExpression;
import sunjava.core.expression.Expression;
import sunjava.core.expression.Expressions;
import sunjava.core.expression.Var;
import sunjava.core.instruction.IfBlock;
import sunjava.entity.EntityCls;
import sunjava.lib.ClsResultSet;

public class MethodGetFromResultSet extends Method {
	protected List<Column> columns;
	protected EntityCls beanCls;
	
	public static String getMethodName(EntityCls entity) {
		return "get"+entity.getName()+ "FromResultSet";
	}
	
	public MethodGetFromResultSet(EntityCls entity) {
		super(Public, entity, getMethodName(entity));
		setStatic(true);
		addParam(new Param(Types.ResultSet, "resultSet"));
		addParam(new Param(Types.String, "alias"));
		this.columns = entity.getTbl().getColumns(true);
		this.beanCls = entity;
	}

	@Override
	public boolean includeIfEmpty() {
		return true;
	}
	
	@Override
	public void addImplementation() {
		addThrowsException(Types.SqlException);
		Var entity = _declareNew(returnType, "entity", BoolExpression.FALSE);
		Param resultSet = getParam("resultSet");
		Param alias = getParam("alias");
		for(Column col:columns) {
			if (!col.hasRelation()) {
				try{
					Expression resultSetValueGetter = EntityCls.getTypeMapper().getResultSetValueGetter(resultSet, col, alias);
					if (col.isNullable() && resultSetValueGetter.getType().isPrimitiveType()) {
						Var value = _declare(resultSetValueGetter.getType(), "value"+col.getUc1stCamelCaseName(),resultSetValueGetter );
						IfBlock ifWasNull = _if(resultSet.callMethod(ClsResultSet.wasNull));
							ifWasNull.thenBlock()
							.addInstr(entity.getClassConcreteType().getMethod("set"+col.getUc1stCamelCaseName()+"Internal").call(entity, Expressions.Null).asInstruction());
							
							ifWasNull.elseBlock()
							.addInstr(entity.getClassConcreteType().getMethod("set"+col.getUc1stCamelCaseName()+"Internal").call(entity, value).asInstruction());
					} else {
						addInstr(entity.getClassConcreteType().getMethod("set"+col.getUc1stCamelCaseName()+"Internal").call(entity, resultSetValueGetter).asInstruction());
					}
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(parent);
				}
			}
		}
		//addInstr(entity.assignAttr("insert",BoolExpression.FALSE));
		_return(entity);
	}

}
