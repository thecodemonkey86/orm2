package cpp.bean.method;

import util.StringUtil;
import util.pg.PgCppUtil;
import cpp.Types;
import cpp.bean.BeanCls;
import cpp.bean.Nullable;
import cpp.bean.OneAttr;
import cpp.core.Param;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.CreateObjectExpression;
import cpp.core.expression.Expressions;
import cpp.core.instruction.IfBlock;
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
		Param pRelationBean = getParam(attr.getName());
		for(int i=0;i<r.getColumnCount();i++) {
			Column destCol = r.getColumns(i).getValue2();
			Column srcCol = r.getColumns(i).getValue1();
			
			if(destCol.isNullable() == srcCol.isNullable()) {
				if(destCol.isNullable()) {
					IfBlock ifParamOneRelationIsNull = _if(pRelationBean._equals(Expressions.Nullptr));
					ifParamOneRelationIsNull.thenBlock().addInstr( _this().assignAttr(srcCol.getCamelCaseName(), new CreateObjectExpression(Types.nullable(BeanCls.getDatabaseMapper().columnToType(destCol)))));
					ifParamOneRelationIsNull.elseBlock().addInstr( _this().assignAttr(srcCol.getCamelCaseName(), pRelationBean.callMethod("get"+destCol.getUc1stCamelCaseName())));
					
				} else {
					addInstr( _this().assignAttr(srcCol.getCamelCaseName(), pRelationBean.callMethod("get"+destCol.getUc1stCamelCaseName())));
				}
				
				
			} else if(destCol.isNullable()) {
				
				IfBlock ifBeanNotNull = _if(pRelationBean._equals(Expressions.Nullptr));
					ifBeanNotNull.thenBlock().addInstr( _this().assignAttr(srcCol.getCamelCaseName(), pRelationBean.callMethod("get"+destCol.getUc1stCamelCaseName()).callMethod(Nullable.val)));
			} else {
				IfBlock ifParamOneRelationIsNull = _if(pRelationBean._equals(Expressions.Nullptr));
				ifParamOneRelationIsNull.thenBlock().addInstr( _this().assignAttr(srcCol.getCamelCaseName(), new CreateObjectExpression(BeanCls.getDatabaseMapper().columnToType(srcCol))));
				ifParamOneRelationIsNull.elseBlock().addInstr( _this().assignAttr(srcCol.getCamelCaseName(), new CreateObjectExpression(BeanCls.getDatabaseMapper().columnToType(srcCol), pRelationBean.callMethod("get"+destCol.getUc1stCamelCaseName()))));
			}
			
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
