package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.Type;
import cpp.core.instruction.IfBlock;
import cpp.entity.EntityCls;
import cpp.entity.Nullable;
import cpp.entityquery.ClsEntityQueryUpdate;
import cpp.lib.ClsQVariant;
import cpp.lib.ClsQList;
import database.column.Column;

public class MethodUpdateSet extends Method{
	EntityCls entity;
	Column col;
	Param pValue;
	
	public MethodUpdateSet(EntityCls entity,Cls parentType,Column col) {
		super(Public, parentType.toRef(), "set"+col.getUc1stCamelCaseName());
		this.entity = entity;
		this.col = col;
		Type t = EntityCls.getDatabaseMapper().columnToType(col);
		pValue = addParam(new Param(t.isPrimitiveType() ? t : t.toConstRef(), "value"));
	}

	@Override
	public void addImplementation() {
		
		
		if(col.isNullable()) {
			IfBlock ifNull = _if(pValue.callMethod(Nullable.isNull));
			ifNull.thenBlock().addInstr(_this().accessAttr(ClsEntityQueryUpdate.updateFields).binOp("+=",  QString.fromStringConstant(col.getEscapedName()+"=NULL")).asInstruction());
			ifNull.elseBlock().addInstr(_this().accessAttr(ClsEntityQueryUpdate.updateFields).binOp("+=",  QString.fromStringConstant(col.getEscapedName()+"=?")).asInstruction());			
			ifNull.elseBlock()._callMethodInstr( _this().accessAttr(ClsEntityQueryUpdate.params),ClsQList.append,Types.QVariant.callStaticMethod(ClsQVariant.fromValue, pValue.callMethod(Nullable.val)));
		} else {
			addInstr(_this().accessAttr(ClsEntityQueryUpdate.updateFields).binOp("+=", QString.fromStringConstant(col.getEscapedName()+"=?")).asInstruction());
			_callMethodInstr( _this().accessAttr(ClsEntityQueryUpdate.params),ClsQList.append,Types.QVariant.callStaticMethod(ClsQVariant.fromValue, pValue) );
		}
		_return(_this().dereference());
	}

}
