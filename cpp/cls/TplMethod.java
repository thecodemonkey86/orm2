package cpp.cls;

import java.util.ArrayList;

import codegen.CodeUtil;

public abstract class TplMethod extends Method{
	protected ArrayList<TplSymbol> tplTypes;
	
	public void addTplType(TplSymbol e) {
		tplTypes.add(e);
	}

	public TplMethod(String visibility, Type returnType, String name) {
		super(visibility, returnType, name);
		tplTypes = new ArrayList<>();
	}
	
	@Override
	public String toHeaderString() {
		ArrayList<String> params=new ArrayList<>();
		for(Param p:this.params) {
			params.add(p.toSourceString());
		}
		return CodeUtil.sp(getVisibility()+":","template"+CodeUtil.abr(CodeUtil.commaSep(tplTypes, "typename ")))+ CodeUtil.sp((inlineQualifier?"inline":null), getReturnType().toDeclarationString(),getName(),CodeUtil.parentheses(CodeUtil.commaSep(params)),(constQualifier?"const":null), ";");
	}

}
