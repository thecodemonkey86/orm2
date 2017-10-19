package cpp.cls;

import java.util.ArrayList;

import codegen.CodeUtil;
import cpp.cls.instruction.Instruction;

public abstract class NonMemberMethod extends Method {

	protected boolean defaultScope; 
	
	public NonMemberMethod(Type returnType,String name) {
		super(null,returnType,name);
		defaultScope = true;
	}
	
	public void setDefaultScope(boolean defaultScope) {
		this.defaultScope = defaultScope;
	}
	
	public boolean isDefaultScope() {
		return defaultScope;
	}
	
	@Override
	public String toHeaderString() {
		ArrayList<String> params=new ArrayList<>();
		for(Param p:this.params) {
			params.add(p.toSourceString());
		}
		return CodeUtil.sp((inlineQualifier?"inline":null), isStatic?"static":null,getReturnType().toDeclarationString(),getName(),CodeUtil.parentheses(CodeUtil.commaSep(params)),(constQualifier?"const":null), ";");
	}
	
	@Override
	public String toString() {
		ArrayList<String> params =new ArrayList<>();
		for(Param p:this.params) {
			params.add(CodeUtil.sp(p.getType().toUsageString(), p.getName()));
		}
		
		StringBuilder sb=new StringBuilder( CodeUtil.sp((inlineQualifier?"inline":null),getReturnType().toDeclarationString(),getName(),CodeUtil.parentheses(CodeUtil.commaSep(params)),"{\n"));
		for(Instruction i:instructions) {
			CodeUtil.writeLine(sb,i);
		}
		sb.append("}");
		
		
		return sb.toString();
	}

	


}
