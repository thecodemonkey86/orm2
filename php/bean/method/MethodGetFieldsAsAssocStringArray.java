package php.bean.method;

import database.column.Column;
import php.bean.BeanCls;
import php.core.Param;
import php.core.Types;
import php.core.expression.ArrayInitExpression;
import php.core.expression.AssocArrayInitExpression;
import php.core.expression.Expression;
import php.core.expression.Expressions;
import php.core.expression.MethodCall;
import php.core.expression.PhpStringLiteral;
import php.core.expression.Var;
import php.core.instruction.CaseBlock;
import php.core.instruction.DefaultCaseBlock;
import php.core.instruction.ForeachLoop;
import php.core.instruction.IfBlock;
import php.core.instruction.SwitchBlock;
import php.core.method.Method;
import util.Pair;

public class MethodGetFieldsAsAssocStringArray extends Method {

	Param pDateFormat;
	Param pDateTimeFormat;
	Param pSpecificColumns;
	
	public MethodGetFieldsAsAssocStringArray() {
		super(Public, Types.array(Types.String), "getFieldsAsAssocStringArray");
		pDateTimeFormat = addParam(new Param(Types.String, "dateTimeFormat",new PhpStringLiteral("Y-m-d H:i:s")));
		pDateFormat = addParam(new Param(Types.String, "dateFormat",new PhpStringLiteral("Y-m-d")));
		pSpecificColumns = addParam(new Param(Types.array(Types.String), "columns",Expressions.Null));
		
	}

	@Override
	public void addImplementation() {
		BeanCls bean = (BeanCls) parent;
		IfBlock ifAllColumns = _if(pSpecificColumns.isNull());
		AssocArrayInitExpression expr = new AssocArrayInitExpression();
		for(Column col : bean.getTbl().getAllColumns()) {
			if(!col.hasRelation())
				expr.addElement(new Pair<String, Expression>(col.getName(), BeanCls.getTypeMapper().getConvertFieldToStringExpression(_this().callMethod("get"+col.getUc1stCamelCaseName()), col,pDateTimeFormat,pDateFormat)));
		}
		ifAllColumns.thenBlock()._return(expr);
		Var result = ifAllColumns.elseBlock()._declare(Types.array(Types.String), "result", new ArrayInitExpression());
		ForeachLoop foreachCols = ifAllColumns.elseBlock()._foreach(new Var(Types.String, "column"), pSpecificColumns);
		SwitchBlock switchBlock = foreachCols._switch(foreachCols.getVar());
		for(Column col : bean.getTbl().getAllColumns()) {
			if(!col.hasRelation()) {
					MethodCall getter = _this().callMethod("get"+col.getUc1stCamelCaseName());
					Expression convertExpr = BeanCls.getTypeMapper().getConvertFieldToStringExpression(getter, col,pDateTimeFormat,pDateFormat);
					CaseBlock caseBlock = switchBlock._case(new PhpStringLiteral(col.getName()));
					caseBlock.addInstr(result.arrayIndexSet(new PhpStringLiteral(col.getName()), convertExpr));
					caseBlock._break();

			}
		}
		switchBlock.setDefaultCase(new DefaultCaseBlock());
		ifAllColumns.elseBlock()._return(result);
	}

}
