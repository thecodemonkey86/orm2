package cpp.core.expression;

import cpp.core.ConstRef;
import cpp.core.RawPtr;
import cpp.core.SharedPtr;
import cpp.core.Type;
import cpp.core.UniquePtr;

public class DereferenceExpression extends Expression{
	Expression e;
	
	public DereferenceExpression(Expression e) {
		this.e=e;
	}
	
	@Override
	public Type getType() {
		if (e.getType() instanceof RawPtr) {
			return ((RawPtr)e.getType()).getElementType();
		} else if (e.getType() instanceof SharedPtr) {
			return ((SharedPtr)e.getType()).getElementType();
		} else if (e.getType() instanceof UniquePtr) {
			return ((UniquePtr)e.getType()).getElementType();
		} else if (e.getType() instanceof ConstRef) {
			ConstRef cr=(ConstRef) e.getType();
			return ((SharedPtr)cr.getBase()).getElementType();
		}
		throw new RuntimeException("not implemented");
		//return null;
	}

	@Override
	public String toString() {
		return "*"+e.toString();
	}

}
