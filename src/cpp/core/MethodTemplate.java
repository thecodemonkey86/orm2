package cpp.core;

import java.util.ArrayList;

import cpp.core.method.TplMethod;

public abstract class MethodTemplate {
	protected ArrayList<TplSymbol> tplTypes;
	protected String visibility;
	protected Type returnType;
	protected String name;
	
	public void addTplType(TplSymbol e) {
		tplTypes.add(e);
	}

	public MethodTemplate(String visibility, Type returnType, String name) {
		tplTypes = new ArrayList<>();
		this.returnType = returnType;
		this.name = name;
		this.visibility = visibility;
	}
	
	public abstract TplMethod getConcreteMethod(Type...types) ;

	public String getName() {
		return name;
	}
	
	public ArrayList<TplSymbol> getTplTypes() {
		return tplTypes;
	}
}
