package php.cls;

public class PhpPseudoGenericClass extends PhpCls {
	
	protected Type element;
	
	public PhpPseudoGenericClass(String type, Type element, String namespace) {
		super(type,namespace);
		this.element = element;
	}
	
//	@Override
//	public String getQualifiedName() {
//		return type + CodeUtil.abr(element.getQualifiedName());
//	}
	
	
	
	@Override
	public String getConstructorName() {
		return type ;
	}
	

	public Type getElementType() {
		return element;
	}
	
}
