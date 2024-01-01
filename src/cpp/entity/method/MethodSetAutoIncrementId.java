package cpp.entity.method;

import cpp.Types;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.instruction.Instruction;
import cpp.core.instruction.ThrowInstruction;
import cpp.entity.EntityCls;
import cpp.lib.ClsQtException;
import util.CodeUtil2;

public class MethodSetAutoIncrementId extends Method {

	Param pId;
	
	public MethodSetAutoIncrementId(boolean isAutoIncrement) {
		super(Public, Types.Void, "setAutoIncrementId");
		
		pId = addParam(new Param(Types.Int64, "id"));
		if(!isAutoIncrement)
			setNoreturnQualifier(true);
	}

	@Override
	public void addImplementation() {
		EntityCls entity = (EntityCls) parent;
		if( entity.getTbl().getPrimaryKey().isAutoIncrement()) {
			Attr attrAutoIncrement = entity.getAttrByName( entity.getTbl().getPrimaryKey().getAutoIncrementColumn().getCamelCaseName());
			addInstr( _this().accessAttr(attrAutoIncrement).assign( getParam("id")));
		} else {
			addInstr(new Instruction() {
				@Override
				public String toString() {
					return "Q_UNUSED"+CodeUtil2.parentheses(pId);
				}
			});
			addInstr(new ThrowInstruction(new ClsQtException(), QString.fromStringConstant("unsupported operation")));
		}
		
	}

}
