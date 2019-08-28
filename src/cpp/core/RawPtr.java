package cpp.core;

public class RawPtr extends TplCls{

	public RawPtr(Type element) {
		super("rawptr", element);
	}

	@Override
	public Method getMethod(String name) {
		if (element instanceof Cls)
			return ((Cls) element).getMethod(name);
		throw new RuntimeException("type is not a class");
	}
	
	@Override
	public String getName() {
		return element.getName();
	}
//	
//	@Override
//	public String getQualifiedName() {
//		return element.getQualifiedName()+"*";
//	}
	
	@Override
	public Attr getAttrByName(String name) {
		if (!(element instanceof IAttributeContainer)) {
			throw new RuntimeException("type "+type+" is not a class"); 
		}
		return ((IAttributeContainer) element).getAttrByName(name);
	}
	
	@Override
	public Attr getAttr(Attr prototype) {
		if (element instanceof IAttributeContainer) {
			IAttributeContainer c=(IAttributeContainer) element;
			return c.getAttr(prototype);
		} 
		throw new RuntimeException("type is not a class or a struct: "+type);
	}
	
	@Override
	public String toUsageString() {
		return (constness? "const " : "") +element.toUsageString()+"*";
	}
	
	@Override
	public String toDeclarationString() {
		return (constness? "const " : "") +element.toUsageString()+"*";
	}
	
	@Override
	public String toString() {
		return element.toUsageString()+"*";
	}
	
	@Override
	public String getConstructorName() {
		return element.toUsageString();
	}
	
	@Override
	public boolean isPtr() {
		return true;
	}
	
	@Override
	public Type toConstRef() {
		return this;
	}
}
