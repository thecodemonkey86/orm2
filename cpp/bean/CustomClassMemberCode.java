package cpp.bean;

import cpp.core.Method;
import cpp.core.instruction.Instruction;

public class CustomClassMemberCode extends Method{

	protected String headerCode,implCode;
	
	public CustomClassMemberCode(String headerCode, String implCode) {
		super(null, null, "");
		this.headerCode = headerCode;
		this.implCode = implCode;
	}

	@Override
	public void addImplementation() {
		addInstr(new Instruction() {
			@Override
			public String toString() {
				return implCode;
			}
		});
	}

	@Override
	public String toHeaderString() {
		return this.headerCode;
	}
	
	@Override
	public String toString() {
		return this.instructions.get(0).toString();
	}
}
