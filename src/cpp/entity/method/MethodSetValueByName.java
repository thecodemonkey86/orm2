package cpp.entity.method;


import java.util.List;

import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.expression.CStringLiteral;
import cpp.core.expression.Expression;
import cpp.core.instruction.IfBlock;
import cpp.core.instruction.MethodCallInstruction;
import cpp.core.instruction.ThrowInstruction;
import cpp.entity.EntityCls;
import cpp.lib.ClsQtException;
import database.column.Column;

public class MethodSetValueByName extends Method {
	
	

	/*private static  long qhash(String p) 
	{
		long h = 0;


	  for (int i = 0; i < p.length(); ++i)
		  h = (31 * h + p.charAt(i))%4294967296L;

	  return h;
	}*/
	
	Param pName;
	Param pValue;
	public MethodSetValueByName() {
		super(Public, Types.Void, "setValueByName");
		pName = addParam(Types.QString.toConstRef(),"name");
		pValue = addParam(Types.QVariant.toConstRef(),"value");
	}

	@Override
	public void addImplementation() {
		
		List<Column> columns = ((EntityCls) parent).getTbl().getColumnsWithoutPrimaryKey();
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
		DefaultCaseBlock defaultCaseBlock = switchBlock._default();*/
		IfBlock ifblock = null;
		int counter=0;
		for (Column c : columns) {
			if(!c.isFileImportEnabled()) {
				MethodCallInstruction setterMethodInstruction = _this().callSetterMethodInstruction(c.getCamelCaseName(),pValue.callMethod(EntityCls.getDatabaseMapper().getQVariantConvertMethod(c)));
				Expression cond = pName._equals(new CStringLiteral(c.getName()));
				if (ifblock == null ||counter==100) {
					counter=0;
					ifblock = _if(cond);
					
					ifblock.thenBlock().addInstr(setterMethodInstruction);
				} else {
					ifblock.addElseIf(cond, setterMethodInstruction);
				}
				++counter;
			}
		}
		// ifblock.elseBlock().addInstr(new ThrowInstruction(new ClsQtException()));
		//defaultCaseBlock.addInstr(new ThrowInstruction(new ClsQtException()));
		ifblock.addElseInstr(new ThrowInstruction(new ClsQtException()));
	}

}
