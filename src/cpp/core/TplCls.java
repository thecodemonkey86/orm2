package cpp.core;

import codegen.CodeUtil;

public class TplCls extends Cls {
	
	protected Type element;
	
	public TplCls(String type, Type element) {
		super(type);
		this.element = element;
	}
	
//	@Override
//	public String getQualifiedName() {
//		return type + CodeUtil.abr(element.getQualifiedName());
//	}
	
	@Override
	public String toUsageString() {
		String str= constness ? CodeUtil.sp("const",type,isPtr()?"*":"") : isPtr()?type+ CodeUtil.abr(element.toUsageString())+"*":type+ CodeUtil.abr(element.toUsageString());
		return (useNamespace != null) ? useNamespace+"::"+ str : str;
		
	}
	
	@Override
	public String toDeclarationString() {
		return toUsageString();
	}
	
	@Override
	public String getConstructorName() {
		return super.getConstructorName()+ CodeUtil.abr(element.toUsageString());
	}
	
	@Override
	public String toString() {
		return type + CodeUtil.abr(element.toString());
	}
	

	public Type getElementType() {
		return element;
	}
	
	@Override
	public String getForwardDeclaration() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof TplCls)
			return super.equals(obj) && element.equals( ((TplCls)obj).element);
		return false;
	}
	
	@Override
	public void collectIncludes(Cls cls,boolean inSourceFile) {
		super.collectIncludes(cls,inSourceFile);
		if(element instanceof Cls) {
			((Cls)element).collectIncludes(cls,inSourceFile);
		}
	}
}
