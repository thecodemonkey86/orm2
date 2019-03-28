package sunjava.core.instruction;

import sunjava.core.AbstractJavaCls;
import sunjava.core.expression.Var;
import sunjava.lib.ClsException;

public class CatchBlock extends InstructionBlock{
	protected Var var;
	public CatchBlock( Var var) {
		super();
		if (!(var.getType() instanceof ClsException)) {
			throw new RuntimeException("variable must be of type exception");
		}
		this.var = var;
	}
	
	
	public Var getVar() {
		return var;
	}
	
	@Override
	public void collectImports(AbstractJavaCls cls) {
		var.getType().collectImports(cls);
		super.collectImports(cls);
	}
}
