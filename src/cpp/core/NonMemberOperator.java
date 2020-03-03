package cpp.core;

import java.util.ArrayList;

import codegen.CodeUtil;

public abstract class NonMemberOperator extends Operator {

	String exportMacro;
	
	public NonMemberOperator(String symbol, Type returnType, boolean constQualifier) {
		super(symbol, returnType, constQualifier);
	}
	
	public void setExportMacro(String exportMacro) {
		this.exportMacro = exportMacro;
	}

	@Override
	public String toHeaderString() {
//		if (getParent()==null) return toString();
		ArrayList<String> params=new ArrayList<>();
		for(Param p:this.params) {
			params.add(p.toSourceString());
		}
		return CodeUtil.sp(exportMacro,
				returnType,
				"operator",
				symbol,
				CodeUtil.parentheses(CodeUtil.commaSep(params)),
				(constQualifier?"const":""),
				";");
	}

}
