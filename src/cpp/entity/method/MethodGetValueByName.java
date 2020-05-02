package cpp.entity.method;


import java.util.ArrayList;

import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.Type;
import cpp.core.expression.CreateObjectExpression;
import cpp.core.expression.Expression;
import cpp.core.expression.UIntExpression;
import cpp.core.instruction.IfBlock;
import cpp.core.instruction.SwitchBlock;
import cpp.core.instruction.ThrowInstruction;
import cpp.entity.EntityCls;
import cpp.entity.Nullable;
import cpp.lib.ClsQVariant;
import cpp.lib.ClsQtException;
import database.column.Column;
import util.CodeUtil2;

public class MethodGetValueByName extends Method {
	
	private static  long qhash(String p) 
	{
		long h = 0;


	  for (int i = 0; i < p.length(); ++i)
		  h = (31 * h + p.charAt(i))%4294967296L;

	  return h;
	}
	
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
		
		SwitchBlock switchBlock = new SwitchBlock(new Expression() {
			
			@Override
			public String toString() {
				return "qHash"+CodeUtil2.parentheses(pName);
			}
			
			@Override
			public Type getType() {
				return Types.Uint;
			}
		});
		//IfBlock ifblock = null;
		for(Column c:columns) {
			if(!c.hasRelation()) {
				//Expression cond = pName._equals(parent.callStaticMethod(MethodGetFieldName.getMethodName(c)));
//				ReturnInstruction ret =new ReturnInstruction(Types.QVariant.callStaticMethod(ClsQVariant.fromValue, _this().callAttrGetter(c.getCamelCaseName()))); 
				/*if(ifblock==null) {
					ifblock =  _if(cond);
					ifblock.thenBlock().addInstr(ret);
				} else {
					 ifblock.addElseIf(cond, ret);
				}*/
				if(c.isNullable())	{
					IfBlock ifNull = switchBlock._case(new UIntExpression(qhash(c.getName())))._if(_this().callAttrGetter(c.getCamelCaseName()).callMethod(Nullable.isNull));
					ifNull.thenBlock()._return(new CreateObjectExpression(Types.QVariant));
					ifNull.elseBlock()._return(Types.QVariant.callStaticMethod(ClsQVariant.fromValue, _this().callAttrGetter(c.getCamelCaseName()).callMethod(Nullable.val)));
				} else {
					switchBlock._case(new UIntExpression(qhash(c.getName())))._return(Types.QVariant.callStaticMethod(ClsQVariant.fromValue, _this().callAttrGetter(c.getCamelCaseName())));
				}
				
			}
		}
		//ifblock.elseBlock().addInstr(new ThrowInstruction(new ClsQtException()));
		switchBlock._default().addInstr(new ThrowInstruction(new ClsQtException()));
		addInstr(switchBlock);
	}

}
