package cpp.entity.method;

import java.util.List;

import util.pg.PgCppUtil;
import cpp.Types;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.core.Optional;
import cpp.core.QString;
import cpp.core.expression.CreateObjectExpression;
import cpp.core.expression.Expression;
import cpp.core.expression.InlineIfExpression;
import cpp.core.expression.Var;
import cpp.core.instruction.ForeachLoop;
import cpp.entity.EntityCls;
import cpp.lib.ClsQString;
import cpp.lib.ClsQVariant;
import cpp.lib.ClsQList;
import database.column.Column;

public class MethodGetInsertParams extends Method {

	protected List<Column> cols;
	
	public MethodGetInsertParams(List<Column> cols) {
		super(Method.Public,Types.QVariantList, "getInsertParams");
		this.cols = cols;
		setConstQualifier();
	}

	@Override
	public void addImplementation() {
		EntityCls parent = (EntityCls) this.parent;
		Var params = _declare(Types.QVariantList, "params");
		 
		for(Column col: cols) {
			if(col.hasOneRelation()){
				addInstr(params.callMethodInstruction(ClsQList.append,parent.getAttrByName(PgCppUtil.getOneRelationDestAttrName(col.getOneRelation())).callMethod("get"+col.getOneRelationMappedColumn().getUc1stCamelCaseName()) )); 
			}else{
				if(col.isRawValueEnabled()) {
					Attr attrRawExpressionParams = parent.getAttrByName("insertParamsForRawExpression"+col.getUc1stCamelCaseName());
					ForeachLoop foreachAttrRawExpressionParams = _foreach(new Var(Types.QVariant, name), attrRawExpressionParams);
					foreachAttrRawExpressionParams.addInstr(params.callMethodInstruction(ClsQList.append,foreachAttrRawExpressionParams.getVar()));
				} else if(col.isFileImportEnabled()) {
					Expression colAttr = parent.getAttrByName(col.getCamelCaseName()+"FilePath");
					addInstr(params.callMethodInstruction(ClsQList.append, ClsQVariant.fromValue(colAttr)));
				} else {
					Expression colAttr = parent.getAttrByName(col.getCamelCaseName());
					addInstr(params.callMethodInstruction(ClsQList.append,col.isNullable() ? new InlineIfExpression(_not(colAttr.callMethod(Optional.has_value)), new CreateObjectExpression(Types.QVariant), ClsQVariant.fromValue(colAttr.callMethod(Optional.value)))   : ClsQVariant.fromValue(colAttr.getType().equals(Types.QString) ? new InlineIfExpression(colAttr.callMethod(ClsQString.isNull), QString.fromStringConstant(""), colAttr) : colAttr)));
				}
			}
	}
		_return(params);
	}

}
