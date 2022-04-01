package cpp.entity.method;


import java.util.ArrayList;
import java.util.HashSet;

import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.Type;
import cpp.core.expression.CreateObjectExpression;
import cpp.core.expression.Expression;
import cpp.core.expression.InlineIfExpression;
import cpp.core.expression.UIntExpression;
import cpp.core.instruction.DefaultCaseBlock;
import cpp.core.instruction.IfBlock;
import cpp.core.instruction.ReturnInstruction;
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
		if (columns.isEmpty()) {
			throw new RuntimeException();
		}
		ArrayList<Column> hashCollisionColumns = new ArrayList<>();
		HashSet<Long> hashCollisions = new HashSet<>();

		SwitchBlock switchBlock = new SwitchBlock(new Expression() {

			@Override
			public String toString() {
				return "qHash" + CodeUtil2.parentheses(pName);
			}

			@Override
			public Type getType() {
				return Types.SizeT;
			}
		});

		for (Column c1 : columns) {
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
		}

		IfBlock ifblockFallback = null;
		for (Column c : columns) {
			//if (!c.hasRelation()) {
				//
				Expression ret = null;
				
				if(c.isNullable()) {
					ret = new InlineIfExpression( _this().callAttrGetter(c.getCamelCaseName()).callMethod(Nullable.isNull), new CreateObjectExpression(Types.QVariant), _this().callAttrGetter(c.getCamelCaseName()).callMethod(Nullable.val)); 
				} else {
					ret = Types.QVariant.callStaticMethod(ClsQVariant.fromValue, _this().callAttrGetter(c.getCamelCaseName()));
				}
				
//				ReturnInstruction ret =new ReturnInstruction(Types.QVariant.callStaticMethod(ClsQVariant.fromValue, _this().callAttrGetter(c.getCamelCaseName()))); 
				long hash = qhash(c.getName());
				if (!hashCollisions.contains(hash)) {
					switchBlock._case(new UIntExpression(hash))._return(ret);
				} else {
					hashCollisionColumns.add(c);
				}

			//}
		}
		DefaultCaseBlock defaultCaseBlock = switchBlock._default();
		for (Column c : hashCollisionColumns) {
			Expression ret = Types.QVariant.callStaticMethod(ClsQVariant.fromValue,_this().callAttrGetter(c.getCamelCaseName()));
			Expression cond = pName._equals(parent.callStaticMethod(MethodGetFieldName.getMethodName(c)));
			if (ifblockFallback == null) {
				ifblockFallback = defaultCaseBlock._if(cond);
				ifblockFallback.thenBlock()._return(ret);
			} else {
				ifblockFallback.addElseIf(cond, new ReturnInstruction(ret));
			}
		}
		// ifblock.elseBlock().addInstr(new ThrowInstruction(new ClsQtException()));
		defaultCaseBlock.addInstr(new ThrowInstruction(new ClsQtException()));
		addInstr(switchBlock);
	}

}
