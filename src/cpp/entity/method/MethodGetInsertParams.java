package cpp.entity.method;

import java.util.List;

import util.pg.PgCppUtil;
import cpp.Types;
import cpp.core.Attr;
import cpp.core.Method;
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
		/*for(Column col: cols) {
			if (!col.hasOneRelation()) {
				Attr colAttr = parent.getAttrByName(col.getCamelCaseName());
				_callMethodInstr(params,Types.QVariantList.getMethod("append"),
						col.isNullable() 
						? _inlineIf(colAttr.callMethod("isNull"), 
							new CreateObjectExpression(Types.QVariant), 
							colAttr.callMethod("val"))
						: colAttr);
									
			} else {
//				if (Beans.get(col.getRelation().getSourceTable()).equals(parent)) {
//					
//				} else {
					Attr colAttr = parent.getAttrByName(col.getCamelCaseName());
					
					Expression  e= col.hasOneRelation() 
					? colAttr.callMethod("get"+ col.getRelation().getDestTable().getUc1stCamelCaseName()).callMethod("get"+col.getOneRelationMappedColumn().getUc1stCamelCaseName())  
							:  colAttr.callMethod("get"+ col.getUc1stCamelCaseName());
					_callMethodInstr(params,Types.QVariantList.getMethod("append"),e);
//					for(Column pkCol:pkForeign.getColumns()) {
//						_callMethodInstr(params,Types.QVariantList.getMethod("append"),colAttr.callMethod("get"+pkCol.getUc1stCamelCaseName()));	
//					}
//				}
				
			}
		}*/
		for(Column col: cols) {
//			Expression colAttr = parent.getPkExpression(col);
			
//			if (col.hasOneRelation()) {
//				addInstr(params.callMethodInstruction("append", colAttr));
//			} else {
//				addInstr(params.callMethodInstruction("append", colAttr));
//			
//			}
			if(col.hasOneRelation()){
				//colPk.getRelation().getDestTable().getCamelCaseName()
				addInstr(params.callMethodInstruction(ClsQList.append,parent.getAttrByName(PgCppUtil.getOneRelationDestAttrName(col.getOneRelation())).callMethod("get"+col.getOneRelationMappedColumn().getUc1stCamelCaseName()) )); 
			}else{
				if(col.isRawValueEnabled()) {
					Attr attrRawExpressionParams = parent.getAttrByName("insertParamsForRawExpression"+col.getUc1stCamelCaseName());
					ForeachLoop foreachAttrRawExpressionParams = _foreach(new Var(Types.QVariant, name), attrRawExpressionParams);
					foreachAttrRawExpressionParams.addInstr(params.callMethodInstruction(ClsQList.append,foreachAttrRawExpressionParams.getVar()));
				} else if(col.isFileImportEnabled()) {
					Expression colAttr = parent.getAttrByName(col.getCamelCaseName()+"FilePath");
					addInstr(params.callMethodInstruction(ClsQList.append, Types.QVariant.callStaticMethod(ClsQVariant.fromValue,colAttr)));
				} else {
					Expression colAttr = parent.getAttrByName(col.getCamelCaseName());
					addInstr(params.callMethodInstruction(ClsQList.append,col.isNullable() ? new InlineIfExpression(colAttr.callMethod(ClsQString.isNull), new CreateObjectExpression(Types.QVariant), Types.QVariant.callStaticMethod(ClsQVariant.fromValue, colAttr.callMethod("val")))   : Types.QVariant.callStaticMethod(ClsQVariant.fromValue, colAttr.getType().equals(Types.QString) ? new InlineIfExpression(colAttr.callMethod(ClsQString.isNull), QString.fromStringConstant(""), colAttr) : colAttr)));
				}
			}
	}
		_return(params);
	}

}
