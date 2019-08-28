package cpp.core;

import java.util.ArrayList;

import codegen.CodeUtil;
import cpp.CoreTypes;
import cpp.core.instruction.Instruction;

public class QtSlot extends Method{

	public QtSlot(String visibility, String name, Param...params) {
		super(visibility, CoreTypes.Void, name);
		for(Param p : params)
			addParam(p);
	}

	@Override
	public void addImplementation() {
	}

	@Override
	public boolean addInstr(Instruction e) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Method setStatic(boolean isStatic) {
		if(isStatic)
			throw new UnsupportedOperationException("Slots cannot be static");
		return this;
	}
	
	@Override
	public String toString() {
		return "&"+ parent+"::"+name;
	}
	
	@Override
	public String toHeaderString() {
		ArrayList<String> params=new ArrayList<>();
		for(Param p:this.params) {
			params.add(p.toDeclarationString());
		}
		return CodeUtil.sp(visibility,"slots:",returnType.toDeclarationString() ,getName(),CodeUtil.parentheses(CodeUtil.commaSep(params)),(constQualifier?"const":null), ";");
	}
	
	@Override
	public final boolean includeIfEmpty() {
		return true;
	}
}
