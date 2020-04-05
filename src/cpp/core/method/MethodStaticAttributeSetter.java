package cpp.core.method;

import cpp.core.Attr;
import cpp.core.Cls;
import cpp.core.RawPtr;
import cpp.core.SharedPtr;
import cpp.core.instruction.AssignInstruction;

public class MethodStaticAttributeSetter extends MethodAttributeSetter {

	public MethodStaticAttributeSetter(Attr a) {
		super(a);
		setStatic(true);
	}
	
	@Override
	public void addImplementation() {
		Cls t = (Cls)parent;
		
		if(t instanceof RawPtr) {
			addInstr(new AssignInstruction( ((Cls) ((RawPtr)t).getElementType()).accessStaticAttr(attr.getName()), getParam(attr.getName())));
		} else if(t instanceof SharedPtr) {
			addInstr(new AssignInstruction( ((Cls) ((SharedPtr)t).getElementType()).accessStaticAttr(attr.getName()), getParam(attr.getName())));;
		} else {
			addInstr(new AssignInstruction( t.accessStaticAttr(attr.getName()), getParam(attr.getName())));;
		}
		
		
		
	}

}
