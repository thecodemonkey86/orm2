package php.entity.method;

import database.column.Column;
import php.core.Param;
import php.core.Types;
import php.core.expression.ArrayInitExpression;
import php.core.expression.AssocArrayInitExpression;
import php.core.expression.Expression;
import php.core.expression.Expressions;
import php.core.expression.InlineIfExpression;
import php.core.expression.PhpStringLiteral;
import php.core.expression.Var;
import php.core.instruction.ForeachLoop;
import php.core.instruction.IfBlock;
import php.core.method.Method;
import php.entity.EntityCls;
import util.Pair;

public class MethodGetFieldsAsAssocArray extends Method {

	public static final String METHOD_NAME = "getFieldsAsAssocArray";
	Param pDateFormat;
	Param pDateTimeFormat;
	Param pSpecificColumns;
	
	public MethodGetFieldsAsAssocArray(EntityCls entity) {
		super(Public, Types.array(Types.String), METHOD_NAME);
		pSpecificColumns = addParam(new Param(Types.array(Types.String), "columns",Expressions.Null));
		for(Column col : entity.getTbl().getAllColumns()) {
			if(!col.isRelationDestColumn() || col.isPartOfPk()) {
				if(EntityCls.getTypeMapper().columnToType(col).equals(Types.DateTime)) {
					pDateTimeFormat = addParam(new Param(Types.String, "dateTimeFormat",new PhpStringLiteral("Y-m-d H:i:s")));
					pDateFormat = addParam(new Param(Types.String, "dateFormat",new PhpStringLiteral("Y-m-d")));
					break;
				}
			}
		}
		
		
		
	}

	@Override
	public void addImplementation() {
		EntityCls entity = (EntityCls) parent;
		IfBlock ifAllColumns = _if(pSpecificColumns.isNull());
		AssocArrayInitExpression expr = new AssocArrayInitExpression();
		for(Column col : entity.getTbl().getAllColumns()) {
			if(!col.isRelationDestColumn() || col.isPartOfPk()) {
				Expression e = _this().callMethod("get"+col.getUc1stCamelCaseName());
				expr.addElement(new Pair<String, Expression>(col.getName(),
						col.isNullable() ? new InlineIfExpression(e.isNull(), Expressions.Null, (e.getType().equals(Types.DateTime)
							? EntityCls.getTypeMapper().getConvertFieldToStringExpression(e, col,pDateTimeFormat,pDateFormat)
							:e)) : (e.getType().equals(Types.DateTime)
							? EntityCls.getTypeMapper().getConvertFieldToStringExpression(e, col,pDateTimeFormat,pDateFormat)
							:e) ));
				
			}
		}
		ifAllColumns.thenBlock()._return(expr);
		/*Var result = ifAllColumns.elseBlock()._declare(Types.array(Types.String), "result", new ArrayInitExpression());
		ForeachLoop foreachCols = ifAllColumns.elseBlock()._foreach(new Var(Types.String, "column"), pSpecificColumns);
		SwitchBlock switchBlock = foreachCols._switch(foreachCols.getVar());
		for(Column col : entity.getTbl().getAllColumns()) {
			if(!col.hasRelation()) {
					MethodCall getter = _this().callMethod("get"+col.getUc1stCamelCaseName());
					Expression convertExpr = BeanCls.getTypeMapper().getConvertFieldToStringExpression(getter, col,pDateTimeFormat,pDateFormat);
					CaseBlock caseBlock = switchBlock._case(new PhpStringLiteral(col.getName()));
					caseBlock.addInstr(result.arrayIndexSet(new PhpStringLiteral(col.getName()), convertExpr));
					caseBlock._break();

			}
		}
		switchBlock.setStandardDefaultCase();
		ifAllColumns.elseBlock()._return(result);*/
		
		Var result = ifAllColumns.elseBlock()._declare(Types.array(Types.String), "result", new ArrayInitExpression());
		Var allcols = ifAllColumns.elseBlock()._declare(Types.array(Types.String), "allcols", expr);
		ForeachLoop foreachCols = ifAllColumns.elseBlock()._foreach(new Var(Types.String, "column"), pSpecificColumns);
		foreachCols.addInstr(result.arrayIndexSet(foreachCols.getVar(), allcols.arrayIndex(foreachCols.getVar())));
		ifAllColumns.elseBlock()._return(result);
		
		/*Var result = ifAllColumns.elseBlock()._declare(Types.array(Types.String), "result", expr);
		ForeachLoop foreachCols = ifAllColumns.elseBlock()._foreach(new Var(Types.String, "column"), pSpecificColumns);
		foreachCols.addInstr(result.arrayUnset(arg))*/
	}

}
