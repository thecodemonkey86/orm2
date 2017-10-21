package sunjava.core;

import java.util.ArrayList;

import codegen.CodeUtil;
import sunjava.core.expression.Expression;
import sunjava.core.instruction.Instruction;
import sunjava.lib.ClsException;

public abstract class Constructor extends Method {
	
	protected ArrayList<Expression> passToSuperConstructor;
	
	public Constructor() {
		super(Public,null,null);
		params = new ArrayList<>();
		instructions = new ArrayList<>();
		
	}
	
	@Override
	public void collectImports(AbstractJavaCls cls) {
		for(Param p : params) {
			p.getType().collectImports(cls);
		}
		if (throwsExceptions != null) {
			for(ClsException e : throwsExceptions) {
			  e.collectImports(cls);
			}
		}
		for(Instruction i : instructions) {
			i.collectImports(cls);
		}
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
			params.add(p.toDeclarationString());
		}
		
		StringBuilder sb=new StringBuilder(CodeUtil.sp(visibility  ,parent.getName())+CodeUtil.parentheses(CodeUtil.commaSep(params))
				);
		
		
			
			sb.append("{\n");
			
if (passToSuperConstructor!=null) {
				
	CodeUtil.writeLine(sb,"super" + CodeUtil.parentheses(CodeUtil.commaSep(passToSuperConstructor))+";");
			}
		for(Object i:instructions) {
			
			CodeUtil.writeLine(sb,i);
		}
		sb.append("}");
		
		
		return sb.toString();
	}
	

}
