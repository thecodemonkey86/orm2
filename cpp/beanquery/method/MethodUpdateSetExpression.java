package cpp.beanquery.method;

import cpp.Types;
import cpp.bean.BeanCls;
import cpp.beanquery.ClsBeanQueryUpdate;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.lib.ClsQVariant;
import cpp.lib.ClsQVector;
import database.column.Column;

public class MethodUpdateSetExpression extends Method{
	BeanCls bean;
	Column col;
	Param pExpression;
	Param pValue;
	
	public MethodUpdateSetExpression(BeanCls bean,Cls parentType,Column col) {
		super(Public, parentType.toRef(), "set"+col.getUc1stCamelCaseName());
		this.bean = bean;
		this.col = col;
		pExpression = addParam(Types.QString.toConstRef(), "expression");
		pValue = addParam(Types.QVariant.toConstRef(), "param");
	}

	@Override
	public void addImplementation() {
		
		addInstr(_this().accessAttr(ClsBeanQueryUpdate.updateFields).binOp("+=", QString.fromStringConstant(col.getEscapedName()+"=").concat(pExpression)).asInstruction());
		_callMethodInstr( _this().accessAttr(ClsBeanQueryUpdate.params),ClsQVector.append,Types.QVariant.callStaticMethod(ClsQVariant.fromValue, pValue));
		
		_return(_this().dereference());
	}

}
