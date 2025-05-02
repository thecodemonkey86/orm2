package cpp.core;

import java.util.ArrayList;

import codegen.CodeUtil;

public class CopyAssignOperator extends Operator {
	boolean delete;
	public CopyAssignOperator(Cls cls, boolean delete) {
		super("=",cls.toRef(),false);
		addParam(cls.toConstRef(),"");
		this.delete=delete;
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
		return CodeUtil.sp(
				returnType,
				"operator",
				symbol,
				CodeUtil.parentheses(CodeUtil.commaSep(params)),
				(constQualifier?"const":""),
				"=",(delete?"delete":"default"))+";";
	}
}
