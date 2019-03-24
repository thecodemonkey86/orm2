package php.core.instruction;

import php.core.expression.Var;
import php.lib.ClsException;

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
	
}
