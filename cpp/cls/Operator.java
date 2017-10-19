package cpp.cls;

import java.util.ArrayList;

import codegen.CodeUtil;
import cpp.cls.instruction.Instruction;

public abstract class Operator extends Method{
	String symbol;
	
	public Operator(String symbol, Type returnType, boolean constQualifier) {
		super(Public, returnType, null);
		this.constQualifier = constQualifier;
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
		StringBuilder sb=new StringBuilder(CodeUtil.sp(returnType,(getParent()!=null?getParent().getName()+"::operator":"operator"),symbol,CodeUtil.parentheses(CodeUtil.commaSep(params)),(constQualifier?"const":""),"{\n"));
		for(Instruction i:instructions) {
			CodeUtil.writeLine(sb,i);
		}
		sb.append("}");
		
		
		return sb.toString();
	}


	@Override
	public String toHeaderString() {
//		if (getParent()==null) return toString();
		ArrayList<String> params=new ArrayList<>();
		for(Param p:this.params) {
			params.add(p.toSourceString());
		}
		return CodeUtil.sp(
				returnType,
				"operator",
				symbol,
				CodeUtil.parentheses(CodeUtil.commaSep(params)),
				(constQualifier?"const":""),
				";");
	}
	
}
