package cpp.bean.method;

import java.util.List;

import util.pg.PgCppUtil;
import cpp.CoreTypes;
import cpp.bean.BeanCls;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.core.expression.CreateObjectExpression;
import cpp.core.expression.InlineIfExpression;
import cpp.core.expression.Var;
import database.column.Column;

public class MethodGetInsertParams extends Method {

	protected List<Column> cols;
	
	public MethodGetInsertParams(List<Column> cols) {
		super(Method.Public,CoreTypes.QVariantList, "getInsertParams");
		this.cols = cols;
	}

	@Override
	public void addImplementation() {
		BeanCls parent = (BeanCls) this.parent;
		Var params = _declare(CoreTypes.QVariantList, "params");
		/*for(Column col: cols) {
			if (!col.hasOneRelation()) {
				Attr colAttr = parent.getAttrByName(col.getCamelCaseName());
				_callMethodInstr(params,CoreTypes.QVariantList.getMethod("append"),
						col.isNullable() 
						? _inlineIf(colAttr.callMethod("isNull"), 
							new CreateObjectExpression(CoreTypes.QVariant), 
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
					_callMethodInstr(params,CoreTypes.QVariantList.getMethod("append"),e);
//					for(Column pkCol:pkForeign.getColumns()) {
//						_callMethodInstr(params,CoreTypes.QVariantList.getMethod("append"),colAttr.callMethod("get"+pkCol.getUc1stCamelCaseName()));	
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
				addInstr(params.callMethodInstruction("append",parent.getAttrByName(PgCppUtil.getOneRelationDestAttrName(col.getOneRelation())).callMethod("get"+col.getOneRelationMappedColumn().getUc1stCamelCaseName()) )); 
			}else{
				Attr colAttr = parent.getAttrByName(col.getCamelCaseName());
				addInstr(params.callMethodInstruction("append",col.isNullable() ? new InlineIfExpression(colAttr.callMethod("isNull"), new CreateObjectExpression(CoreTypes.QVariant), colAttr.callMethod("val"))   : colAttr));	
			}
	}
		_return(params);
	}

}
