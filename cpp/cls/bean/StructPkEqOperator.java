package cpp.cls.bean;

import java.util.ArrayList;

import cpp.Struct;
import cpp.Types;
import cpp.cls.Attr;
import cpp.cls.Operator;
import cpp.cls.Param;
import cpp.cls.expression.Expression;
import cpp.cls.expression.Expressions;
import cpp.cls.expression.BinaryOperatorExpression;
import cpp.cls.instruction.ReturnInstruction;

public class StructPkEqOperator extends Operator {
	Struct structPk;
	public StructPkEqOperator(Struct structPk) {
		super("==", Types.Bool, false);
		this.structPk = structPk;
		Param s1 = new Param(structPk.toConstRef(), "s1");
		addParam(s1);
		Param s2 = new Param(structPk.toConstRef(), "s2");
		addParam(s2);
		
	}

	@Override
	public void addImplementation() {
		Param s1=getParam("s1");
		Param s2=getParam("s2");
		ArrayList<Expression> l=new ArrayList<>();
		for(Attr a:structPk.getAttrs()) {
			l.add(new BinaryOperatorExpression(s1.accessAttr(a.getName()), this, s2.accessAttr(a.getName())));
		}
		addInstr(new ReturnInstruction(Expressions.and(l)));
		
	}

	
}
