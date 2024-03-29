package cpp.core;

import java.util.ArrayList;

import codegen.CodeUtil;
import util.CodeUtil2;

public class Enum extends Type{
	protected Cls parentCls;
	protected ArrayList<EnumConstant> enumConstants;
	
	public Enum(Cls parentCls, String name,EnumConstant...constants) {
		super(name);
		this.parentCls = parentCls;
		this.enumConstants = new ArrayList<>();
		for(EnumConstant c:constants) {
			addEnumConstant(c);
		}
	}
	
	@Override
	public String toUsageString() {
		return toDeclarationString();
	}
	
	@Override
	public String toDeclarationString() {
		return (parentCls  != null ? parentCls.toDeclarationString()+"::": null)+type ;
	}
	
	public String toDefinitionString() {
		StringBuilder sb = new StringBuilder( CodeUtil2.sp(Method.Public+":", "enum",type,'{'));
		ArrayList<String> constants = new ArrayList<>();
		for(EnumConstant c : enumConstants) {
			constants.add(c.getName());
		}
		sb.append(CodeUtil2.commaSep(constants)).append("};");
		return sb.toString();
	}
	
	public String toHeaderString() {
		StringBuilder sb = new StringBuilder();
		String includeGuardMacro = type.toUpperCase()+"_H";
		CodeUtil2.writeLine(sb,CodeUtil.sp("#ifndef",includeGuardMacro));
		CodeUtil2.writeLine(sb,CodeUtil.sp("#define",includeGuardMacro));
		CodeUtil2.writeLine(sb,toDefinitionString());
		CodeUtil2.writeLine(sb,"#endif");
		return sb.toString();
	}
	
	public void addEnumConstant(EnumConstant c) {
		c.setParent(this);
		this.enumConstants.add(c);
	}
	
	public ArrayList<EnumConstant> getEnumConstants() {
		return enumConstants;
	}
	
	public EnumConstant constant(String name) {
		for(EnumConstant e : enumConstants) {
			if(e.getName().equals(name)) {
				return e;
			}
		}
		throw new RuntimeException();
	}
	
	
	public Cls getParentCls() {
		return parentCls;
	}

}
