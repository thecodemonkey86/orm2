package sunjava.bean;

import sunjava.core.Method;
import sunjava.core.instruction.Instruction;

public class CustomClassMemberCode extends Method{

	protected String implCode;
	
	public CustomClassMemberCode( String implCode) {
		super(null, null, "");
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
	public String toString() {
		return this.instructions.get(0).toString();
	}
}
