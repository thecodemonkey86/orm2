package cpp.entity.method;


import java.util.ArrayList;

import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.expression.Expression;
import cpp.core.instruction.IfBlock;
import cpp.core.instruction.ReturnInstruction;
import cpp.core.instruction.ThrowInstruction;
import cpp.entity.EntityCls;
import cpp.lib.ClsQVariant;
import cpp.lib.ClsQtException;
import database.column.Column;

public class MethodGetValueByName extends Method {
	Param pName;
	public MethodGetValueByName() {
		super(Public, Types.QVariant, "getValueByName");
		pName = addParam(Types.QString.toConstRef(),"name");
	}

	@Override
	public void addImplementation() {
		ArrayList<Column> columns = ((EntityCls) parent).getTbl().getAllColumns();
		if(columns.isEmpty()) {
			throw new RuntimeException();
		}
		IfBlock ifblock = null;
		for(Column c:columns) {
			if(!c.hasRelation()) {
				Expression cond = pName._equals(parent.callStaticMethod(MethodGetFieldName.getMethodName(c)));
				ReturnInstruction ret =new ReturnInstruction(Types.QVariant.callStaticMethod(ClsQVariant.fromValue, _this().callAttrGetter(c.getCamelCaseName()))); 
				if(ifblock==null) {
					ifblock =  _if(cond);
					ifblock.thenBlock().addInstr(ret);
				} else {
					 ifblock.addElseIf(cond, ret);
				}
			}
		}
		ifblock.elseBlock().addInstr(new ThrowInstruction(new ClsQtException()));

	}

}
