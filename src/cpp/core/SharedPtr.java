package cpp.core;

import codegen.CodeUtil;
import util.CodeUtil2;

public class SharedPtr extends TplCls{

	boolean weak;
	
	public boolean isWeak() {
		return weak;
	}
	
	public SharedPtr(Cls element) {
		super("shared_ptr", element);
		setUseNamespace("std");
		weak=false;
	}

	@Override
	public Method getMethod(String name) {
		return ((Cls) element).getMethod(name);
	}
	
	@Override
	public String getName() {
		return element.toDeclarationString();
	}
	
//	@Override
//	public String getQualifiedName() {
//		return type+CodeUtil2.abr(element.toDeclarationString());
//	}
	@Override
	public String toDeclarationString() {
		String type=weak?"weak_ptr":"shared_ptr";
		String str= constness ? CodeUtil.sp("const",type+CodeUtil2.abr(element.toUsageString())) : type+CodeUtil2.abr(element.toUsageString());
		return (useNamespace != null) ? useNamespace+"::"+ str : str;
	}
	
	@Override
	public String toUsageString() {
//		
		String str= constness ? CodeUtil.sp("const",type+CodeUtil2.abr(element.toUsageString())) : type+CodeUtil2.abr(element.toUsageString());
		return (useNamespace != null) ? useNamespace+"::"+ str : str;
	}
	
	@Override
	public Attr getAttrByName(String name) {
		if (!(element instanceof Cls)) {
			throw new RuntimeException("type "+type+" is not a class"); 
		}
		return ((Cls) element).getAttrByName(name);
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
	public boolean isPtr() {
		return true;
	}

	public void setWeak() {
		this.weak=true;
	}
	
	@Override
	public String getForwardDeclaration() {
		return element.getForwardDeclaration();
	}
}
