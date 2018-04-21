package cpp.core;

import java.util.Arrays;

import util.CodeUtil2;

public class Enum extends Type{
	Cls parentCls;
	EnumConstant[] enumConstants;
	
	public Enum(Cls parentCls, String name, EnumConstant ...enumConstants) {
		super(name);
		this.parentCls = parentCls;
		this.enumConstants = enumConstants;
	}
	
	@Override
	public String toDeclarationString() {
		return CodeUtil2.concat(Arrays.asList(parentCls.getName(),"::",type)) ;
	}
	
	public EnumConstant[] getEnumConstants() {
		return enumConstants;
	}
	
	
	public Cls getParentCls() {
		return parentCls;
	}

}
