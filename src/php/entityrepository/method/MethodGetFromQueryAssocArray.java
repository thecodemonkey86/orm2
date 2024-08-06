package php.entityrepository.method;

import java.util.List;

import database.FirebirdDatabase;
import database.column.Column;
import php.core.Param;
import php.core.PhpFunctions;
import php.core.Types;
import php.core.expression.BoolExpression;
import php.core.expression.Expression;
import php.core.expression.Expressions;
import php.core.expression.InlineIfExpression;
import php.core.expression.PhpStringLiteral;
import php.core.expression.Var;
import php.core.method.Method;
import php.entity.EntityCls;
import php.core.expression.IntExpression;

public class MethodGetFromQueryAssocArray extends Method{
	protected List<Column> columns;
	protected EntityCls beanCls;
	
	public static String getMethodName(EntityCls entity) {
		return "get"+entity.getName()+ "FromQueryAssocArray";
	}
	
	public MethodGetFromQueryAssocArray(EntityCls entity) {
		super(Public, entity.toNullable(), getMethodName(entity));
		setStatic(true);
		addParam(new Param(Types.array(Types.String).toRef(), "array"));
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

		Param array = getParam("array");
		Param alias = getParam("alias");
		Expression pkExprArrayIndex = EntityCls.getTypeMapper().filterFetchAssocArrayKeyExpression(alias).concat(new PhpStringLiteral(EntityCls.getTypeMapper().filterFetchAssocArrayKey("__"+beanCls.getTbl().getPrimaryKey().getFirstColumn().getName())));
		
		if(EntityCls.getDatabase() instanceof FirebirdDatabase) {
			pkExprArrayIndex = PhpFunctions.substr.call(pkExprArrayIndex,new IntExpression(0),new IntExpression(31));
		}
		_if(array.arrayIndex(pkExprArrayIndex).isNull()).thenBlock()._return(Expressions.Null); 
		Var entity = _declareNew(returnType, "entity", BoolExpression.FALSE);
		
		for(Column col:columns) {
			if (!col.isRelationDestColumn() || col.hasOneRelation() || col.isPartOfPk()) {
				try{
					/*Expression resultSetValueGetter = BeanCls.getTypeMapper().getResultSetValueGetter(resultSet, col, alias);
					if (col.isNullable() && resultSetValueGetter.getType().isPrimitiveType()) {
						Var value = _declare(resultSetValueGetter.getType(), "value"+col.getUc1stCamelCaseName(),resultSetValueGetter );
						IfBlock ifWasNull = _if(resultSet.callMethod(ClsResultSet.wasNull));
							ifWasNull.thenBlock()
							.addInstr(entity.getClassConcreteType().getMethod("set"+col.getUc1stCamelCaseName()+"Internal").call(entity, Expressions.Null).asInstruction());
							
							ifWasNull.elseBlock()
							.addInstr(entity.getClassConcreteType().getMethod("set"+col.getUc1stCamelCaseName()+"Internal").call(entity, value).asInstruction());
					} else {
						
					}*/
					
					Expression exprArrayIndex = EntityCls.getTypeMapper().filterFetchAssocArrayKeyExpression(alias).concat(new PhpStringLiteral(EntityCls.getTypeMapper().filterFetchAssocArrayKey("__"+col.getName())));
					
					if(EntityCls.getDatabase() instanceof FirebirdDatabase) {
						exprArrayIndex = PhpFunctions.substr.call(exprArrayIndex,new IntExpression(0),new IntExpression(31));
					}
					if(col.getUc1stCamelCaseName().equals("ZSaStartTimeCustomer")) {
						System.out.println();
					}
					Var val = col.isNullable() ?  _declare( Types.Mixed,"_val"+col.getUc1stCamelCaseName(), new InlineIfExpression(exprArrayIndex.isNull(), Expressions.Null,array.arrayIndex(exprArrayIndex))) : _declare(Types.Mixed, "_val"+col.getUc1stCamelCaseName(),array.arrayIndex(exprArrayIndex));
					Expression convertTypeExpression = EntityCls.getTypeMapper().getConvertTypeExpression(val ,col);
					
					addInstr(entity.getClassConcreteType().getMethod("set"+col.getUc1stCamelCaseName()+"Internal").call(entity, convertTypeExpression).asInstruction());
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
