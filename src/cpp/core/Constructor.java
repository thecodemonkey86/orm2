package cpp.core;

import java.util.ArrayList;

import codegen.CodeUtil;
import cpp.core.expression.AccessExpression;
import cpp.core.expression.Expression;
import cpp.core.expression.ThisExpression;
import cpp.core.instruction.AssignInstruction;
import cpp.core.instruction.Instruction;

public abstract class Constructor extends Method {
	
	protected ArrayList<Expression> passToSuperConstructor;
	protected ArrayList<AssignInstruction> initializeInstructions;
	public Constructor() {
		super(Public,null,null);
		params = new ArrayList<>();
		instructions = new ArrayList<>();
		initializeInstructions = new ArrayList<>();
		
	}
	
	@Override
	public void addInstr(Instruction i) {
		if(i instanceof AssignInstruction) {
			AssignInstruction a=(AssignInstruction) i;
			if((a.getAssign() instanceof AccessExpression && ((AccessExpression) a.getAssign()).getAccess() instanceof ThisExpression ) || a.getAssign() instanceof Attr) {
				initializeInstructions.add(a);
			} else {
				instructions.add(i);
			}
		} else {
			super.addInstr(i);	
		}
		
	}
 
	
	@Override
	public void _assign(Expression var, Expression value) {
		addInstr(new AssignInstruction(var, value));
	}
	
	public void addPassToSuperConstructor(Expression p) {
		if (passToSuperConstructor==null)
			passToSuperConstructor=new ArrayList<>();
		this.passToSuperConstructor.add(p);
	}
	
	@Override
	public String toString() {
		ArrayList<String> params=new ArrayList<>();
		for(Param p:this.params) {
			params.add(p.toSourceString());
		}
		
		StringBuilder sb=new StringBuilder(CodeUtil.sp(parent.getName()+"::"+parent.getName(),CodeUtil.parentheses(CodeUtil.commaSep(params))
				));
		ArrayList<String> initExpr=new ArrayList<>();
		if (passToSuperConstructor!=null) {
			initExpr.add(parent.getSuperclass().getName()+CodeUtil.parentheses(CodeUtil.commaSep( passToSuperConstructor)));
		}
		for(AssignInstruction e:initializeInstructions) {
			
			initExpr.add(e.toConstructorInitializeString()+CodeUtil.parentheses(e.getValue().getReadAccessString()));
		
		}
		if(!initExpr.isEmpty()) {
		sb.append(CodeUtil.sp(':',CodeUtil.commaSep(initExpr)));
		}
		sb.append("{\n");
		for(Object i:instructions) {
			CodeUtil.writeLine(sb,i);
		}
		sb.append("}");
		
		
		return sb.toString();
	}
	
	@Override
	public String toHeaderString() {
		ArrayList<String> params=new ArrayList<>();
		for(Param p:this.params) {
			params.add(p.toDeclarationString());
		}
		return CodeUtil.sp("public:", parent.getName(),CodeUtil.parentheses(CodeUtil.commaSep(params)),";");
	}

}
