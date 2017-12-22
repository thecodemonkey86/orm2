package cpp.core;

import java.util.ArrayList;

import codegen.CodeUtil;

public class Destructor extends Method{
	
	
	public Destructor() {
		super(Public, null, null);
		instructions = new ArrayList<>();
	}
	
	
		
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder(CodeUtil.sp(parent.getName()+"::~"+parent.getName(),"()","{\n"));
		for(Object i:instructions) {
			CodeUtil.writeLine(sb,i);
		}
		sb.append("}");
		
		
		return sb.toString();
	}
	
	@Override
	public String toHeaderString() {
		return CodeUtil.sp("public:", (virtualQualifier ? "virtual" : null) ,"~"+ parent.getName(),"()",";");
	}


	@Override
	public void addImplementation() {
		
	}
}
