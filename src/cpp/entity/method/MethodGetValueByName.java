package cpp.entity.method;


import java.util.ArrayList;

import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.expression.CStringLiteral;
import cpp.core.expression.CreateObjectExpression;
import cpp.core.expression.Expression;
import cpp.core.instruction.IfBlock;
import cpp.core.instruction.ReturnInstruction;
import cpp.core.instruction.ThrowInstruction;
import cpp.entity.EntityCls;
import cpp.core.Optional;
import cpp.lib.ClsQVariant;
import cpp.lib.ClsQtException;
import database.column.Column;
import cpp.core.expression.InlineIfExpression;

public class MethodGetValueByName extends Method {
	
	

	/*private static  long qhash(String p) 
	{
		long h = 0;


	  for (int i = 0; i < p.length(); ++i)
		  h = (31 * h + p.charAt(i))%4294967296L;

	  return h;
	}*/
	
	Param pName;
	public MethodGetValueByName() {
		super(Public, Types.QVariant, "getValueByName");
		pName = addParam(Types.QString.toConstRef(),"name");
	}

	@Override
	public void addImplementation() {
		
		ArrayList<Column> columns = ((EntityCls) parent).getTbl().getAllColumns();
		if (columns.isEmpty()) {
			throw new RuntimeException();
		}
//		ArrayList<Column> hashCollisionColumns = new ArrayList<>();
//		HashSet<Long> hashCollisions = new HashSet<>();

		/*SwitchBlock switchBlock = new SwitchBlock(new Expression() {

			@Override
			public String toString() {
				return "qHash" + CodeUtil2.parentheses(pName);
			}

			@Override
			public Type getType() {
				return Types.SizeT;
			}
		});*/

		/*for (Column c1 : columns) {
			//if (!c1.hasRelation()) {
				for (Column c2 : columns) {
					//if (!c2.hasRelation()) {
						long qhash = qhash(c1.getName());
						if (!c1.getName().equals(c2.getName()) && qhash == qhash(c2.getName())) {
							hashCollisions.add(qhash);
						}
					//}
				}
			//}
		}*/

		/*IfBlock ifblockFallback = null;
		for (Column c : columns) {
			//if (!c.hasRelation()) {
				//
				Expression ret = null;
				
				if(c.isNullable()) {
					ret = new InlineIfExpression( _this().callAttrGetter(c.getCamelCaseName()).callMethod(Nullable.isNull), new CreateObjectExpression(Types.QVariant), _this().callAttrGetter(c.getCamelCaseName()).callMethod(Optional.value)); 
				} else {
					ret = ClsQVariant.fromValue(_this().callAttrGetter(c.getCamelCaseName()));
				}
				
//				ReturnInstruction ret =new ReturnInstruction(ClsQVariant.fromValue(_this().callAttrGetter(c.getCamelCaseName()))); 
				long hash = qhash(c.getName());
				if (!hashCollisions.contains(hash)) {
					switchBlock._case(new UIntExpression(hash))._return(ret);
				} else {
					hashCollisionColumns.add(c);
				}

			//}
		}
		DefaultCaseBlock defaultCaseBlock = switchBlock._default();*/
		IfBlock ifblock = null;
		int counter=0;
		for (Column c : columns) {
			if(!c.isFileImportEnabled()) {
				Expression ret = c.isNullable() 
						? new InlineIfExpression(_this().callAttrGetter(c.getCamelCaseName()).callMethod(Optional.has_value), ClsQVariant.fromValue(_this().callAttrGetter(c.getCamelCaseName()).callMethod(Optional.value)),new CreateObjectExpression(Types.QVariant)) : ClsQVariant.fromValue(_this().callAttrGetter(c.getCamelCaseName()));
				Expression cond = pName._equals(new CStringLiteral(c.getName()));
				if (ifblock == null ||counter==100) {
					counter=0;
					ifblock = _if(cond);
					ifblock.thenBlock()._return(ret);
				} else {
					ifblock.addElseIf(cond, new ReturnInstruction(ret));
				}
				++counter;
			}
		}
		// ifblock.elseBlock().addInstr(new ThrowInstruction(new ClsQtException()));
		//defaultCaseBlock.addInstr(new ThrowInstruction(new ClsQtException()));
		ifblock.addElseInstr(new ThrowInstruction(new ClsQtException()));
	}

}
