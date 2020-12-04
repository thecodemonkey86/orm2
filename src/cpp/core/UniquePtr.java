package cpp.core;

import codegen.CodeUtil;
import cpp.Namespaces;
import util.CodeUtil2;

public class UniquePtr extends TplCls{

	public UniquePtr(Cls element) {
		super("unique_ptr", element);
		setUseNamespace(Namespaces.std);
	}

	@Override
	public Method getMethod(String name) {
		return ((Cls) element).getMethod(name);
	}
	
	@Override
	public String getName() {
		return element.getName();
	}
	
	@Override
	public Attr getAttrByName(String name) {
		if (!(element instanceof Cls)) {
			throw new RuntimeException("type "+type+" is not a class"); 
		}
		return ((Cls) element).getAttrByName(name);
	}
	
	@Override
	public String toUsageString() {
		String str= constness ? CodeUtil.sp("const",type+CodeUtil2.abr(element.toUsageString())) : type+CodeUtil2.abr(element.toUsageString());
		return (useNamespace != null) ? useNamespace+"::"+ str : str;
	}
	
	@Override
	public boolean isPtr() {
		return true;
	}
}
