package cpp.core;

import java.util.ArrayList;

import codegen.CodeUtil;

public class PureVirtualMethod extends Method {

	public PureVirtualMethod(String visibility, Type returnType, String name) {
		super(visibility, returnType, name);
		setVirtualQualifier(true);
	}

	@Override
	public final void addImplementation() {
		//throw new RuntimeException("Pure virtual methods must not define an implemention");
	}
	
	public String toHeaderString() {
		ArrayList<String> params=new ArrayList<>();
		for(Param p:this.params) {
			params.add(p.toDeclarationString());
		}
		return CodeUtil.sp(getVisibility()+":",(inlineQualifier?"inline":null), (isStatic?"static":(virtualQualifier?"virtual":null)),retType() ,getName(),CodeUtil.parentheses(CodeUtil.commaSep(params)),(constQualifier?"const":null), " = 0;");
	}
	
	@Override
	public String toString() {
		return "";
	}
	
	@Override
	public boolean includeIfEmpty() {
		return true;
	}

}
