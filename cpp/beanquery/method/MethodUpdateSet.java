package cpp.beanquery.method;

import cpp.Types;
import cpp.bean.BeanCls;
import cpp.bean.Nullable;
import cpp.beanquery.ClsBeanQueryUpdate;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.Type;
import cpp.core.instruction.IfBlock;
import cpp.lib.ClsQVariant;
import cpp.lib.ClsQVector;
import database.column.Column;

public class MethodUpdateSet extends Method{
	BeanCls bean;
	Column col;
	Param pValue;
	
	public MethodUpdateSet(BeanCls bean,Cls parentType,Column col) {
		super(Public, parentType.toRef(), "set"+col.getUc1stCamelCaseName());
		this.bean = bean;
		this.col = col;
		Type t = BeanCls.getDatabaseMapper().columnToType(col);
		pValue = addParam(new Param(t.isPrimitiveType() ? t : t.toConstRef(), "value"));
	}

	@Override
	public void addImplementation() {
		
		
		if(col.isNullable()) {
			IfBlock ifNull = _if(pValue.callMethod(Nullable.isNull));
			ifNull.thenBlock().addInstr(_this().accessAttr(ClsBeanQueryUpdate.updateFields).binOp("+=",  QString.fromStringConstant(col.getEscapedName()+"=NULL")).asInstruction());
			ifNull.elseBlock().addInstr(_this().accessAttr(ClsBeanQueryUpdate.updateFields).binOp("+=",  QString.fromStringConstant(col.getEscapedName()+"=?")).asInstruction());			
			ifNull.elseBlock()._callMethodInstr( _this().accessAttr(ClsBeanQueryUpdate.params),ClsQVector.append,Types.QVariant.callStaticMethod(ClsQVariant.fromValue, pValue.callMethod(Nullable.val)));
		} else {
			addInstr(_this().accessAttr(ClsBeanQueryUpdate.updateFields).binOp("+=", QString.fromStringConstant(col.getEscapedName()+"=?")).asInstruction());
			_callMethodInstr( _this().accessAttr(ClsBeanQueryUpdate.params),ClsQVector.append,Types.QVariant.callStaticMethod(ClsQVariant.fromValue, pValue) );
		}
		_return(_this().dereference());
	}

}
