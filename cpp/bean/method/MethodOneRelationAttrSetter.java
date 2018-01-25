package cpp.bean.method;

import util.StringUtil;
import util.pg.PgCppUtil;
import cpp.bean.BeanCls;
import cpp.bean.OneAttr;
import cpp.core.expression.BoolExpression;
import cpp.core.method.MethodAttributeSetter;
import database.column.Column;
import database.relation.OneRelation;


public class MethodOneRelationAttrSetter extends MethodAttributeSetter {

	protected boolean internal; 
	
	public MethodOneRelationAttrSetter(BeanCls bean, OneRelation r, boolean internal) {
		super(bean.getAttrByName(PgCppUtil.getOneRelationDestAttrName(r)));
		this.internal = internal;
		if (internal ) {
			this.name = this.name + "Internal";
		}
	}
	
	public static String getMethodName(OneRelation r,boolean internal) {
		return "set"+StringUtil.ucfirst(PgCppUtil.getOneRelationDestAttrName(r)) +(internal ? "Internal":"");
	}

	@Override
	public void addImplementation() {
		super.addImplementation();
		OneRelation r = ((OneAttr) attr).getRelation();
		for(int i=0;i<r.getColumnCount();i++) {
			Column destCol = r.getColumns(i).getValue2();
			Column srcCol = r.getColumns(i).getValue1();
			addInstr( _this().assignAttr(srcCol.getCamelCaseName(), getParam(attr.getName()).callMethod("get"+destCol.getUc1stCamelCaseName())));
			if (!this.internal) {
				if (!srcCol.isPartOfPk())
					addInstr(_this().assignAttr(attr.getName()+"Modified",BoolExpression.TRUE));
				else
					addInstr(_this().assignAttr("primaryKeyModified",BoolExpression.TRUE));
			} else {
				this.returnType = getParent().toRawPointer();
				_return(_this());
			}
		}
	}
}
