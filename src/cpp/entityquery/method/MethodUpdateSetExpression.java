package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.entity.EntityCls;
import cpp.entityquery.ClsEntityQueryUpdate;
import cpp.lib.ClsQVariant;
import cpp.lib.ClsQList;
import database.column.Column;

public class MethodUpdateSetExpression extends Method{
	EntityCls bean;
	Column col;
	Param pExpression;
	Param pValue;
	
	public MethodUpdateSetExpression(EntityCls bean,Cls parentType,Column col) {
		super(Public, parentType.toRef(), "set"+col.getUc1stCamelCaseName());
		this.bean = bean;
		this.col = col;
		pExpression = addParam(Types.QString.toConstRef(), "expression");
		pValue = addParam(Types.QVariant.toConstRef(), "param");
	}

	@Override
	public void addImplementation() {
		
		addInstr(_this().accessAttr(ClsEntityQueryUpdate.updateFields).binOp("+=", QString.fromStringConstant(col.getEscapedName()+"=").concat(pExpression)).asInstruction());
		_callMethodInstr( _this().accessAttr(ClsEntityQueryUpdate.params),ClsQList.append,Types.QVariant.callStaticMethod(ClsQVariant.fromValue, pValue));
		
		_return(_this().dereference());
	}

}
