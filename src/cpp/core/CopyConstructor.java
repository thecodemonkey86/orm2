package cpp.core;

import java.util.ArrayList;

import codegen.CodeUtil;

public class CopyConstructor extends Constructor {

	public CopyConstructor(Cls cls) {
		addParam(cls.toConstRef(),"");
	}
	
	@Override
	public void addImplementation() {
	}
	
	
	@Override
	public boolean hasOutputSourceCode() {
		return false;
	}
	
	@Override
	public String toHeaderString() {
		ArrayList<String> params=new ArrayList<>();
		for(Param p:this.params) {
			params.add(CodeUtil.sp("const",p.getType().getName(),"&",name));
		}
		return CodeUtil.sp("public:", parent.getName(),CodeUtil.parentheses(CodeUtil.commaSep(params)),"= default;");
	}
}
