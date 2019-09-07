package sunjava.core;

import java.util.ArrayList;

import codegen.CodeUtil;
import sunjava.core.instruction.Instruction;

public abstract class Operator extends Method{
	String symbol;
	
	public Operator(String symbol, Type returnType, boolean constQualifier) {
		super(Public, returnType, null);
		this.symbol = symbol;
		this.returnType = returnType;
	} 
	
	public String getSymbol() {
		return symbol;
	}
	
	@Override
	public String toString() {
		ArrayList<String> params=new ArrayList<>();
		for(Param p:this.params) {
			params.add(p.toSourceString());
		}
		StringBuilder sb=new StringBuilder(CodeUtil.sp(returnType,(getParent()!=null?getParent().getName()+"::operator":"operator"),symbol,CodeUtil.parentheses(CodeUtil.commaSep(params)), "{\n"));
		for(Instruction i:instructions) {
			CodeUtil.writeLine(sb,i);
		}
		sb.append("}");
		
		
		return sb.toString();
	}


	
}
