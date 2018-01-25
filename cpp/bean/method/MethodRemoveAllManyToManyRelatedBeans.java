package cpp.bean.method;

import util.StringUtil;
import cpp.Types;
import cpp.bean.BeanCls;
import cpp.bean.Beans;
import cpp.bean.ManyAttr;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.Struct;
import cpp.core.expression.Var;
import cpp.core.instruction.ForeachLoop;
import cpp.lib.ClsQVector;
import cpp.orm.OrmUtil;
import database.column.Column;
import database.relation.ManyRelation;

public class MethodRemoveAllManyToManyRelatedBeans extends Method {

	protected ManyRelation rel;
	
	public MethodRemoveAllManyToManyRelatedBeans(ManyRelation r) {
		super(Public, Types.Void, "removeAll"+StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrName(r)));
		rel=r;
	}

	@Override
	public void addImplementation() {
		Attr a=parent.getAttrByName(OrmUtil.getManyRelationDestAttrName(rel));
		BeanCls relationBean = Beans.get( rel.getDestTable());
		
		if (relationBean.getTbl().getPrimaryKey().isMultiColumn()) {
			throw new RuntimeException("not implemented");
				
		} else {
			Var varForeach = new Var(((ManyAttr)a).getElementType().toConstRef(), "_relationBean");
			ForeachLoop foreachRelationBeans = _foreach(varForeach, a);
			
			
			foreachRelationBeans.addInstr(
					parent.getAttrByName(
							a.getName()+"Removed")
							.callMethod("append",
									varForeach.callMethod("get" + relationBean.getTbl().getPrimaryKey().getFirstColumn().getUc1stCamelCaseName())
																	
								).asInstruction());	
		}
		_callMethodInstr(a, ClsQVector.clear);
	}

}
