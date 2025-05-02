package cpp.core.method;

import cpp.core.Attr;
import cpp.core.Optional;
import cpp.core.Param;

public class MethodOptionalAttributeEmplaceSetter extends MethodAttributeSetter{
	public MethodOptionalAttributeEmplaceSetter(Attr a) {
		super(a);
		params.clear();
		addParam(new Param( ((Optional) a.getType()).getElementType().toConstRef() , a.getName()));
		setnoexcept();
	}

	
	@Override
	public void addImplementation() {
		//_return(_this().accessAttr(a));
		addInstr(_this().accessAttr(attr).callMethodInstruction(Optional.emplace,getParam(attr.getName())));
	}
	
	 

}
