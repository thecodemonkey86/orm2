package php.core;

import java.util.ArrayList;

import php.core.method.Method;

public abstract class TplMethod extends Method{
	protected ArrayList<TplSymbol> tplTypes;
	
	public void addTplType(TplSymbol e) {
		tplTypes.add(e);
	}

	public TplMethod(String visibility, Type returnType, String name) {
		super(visibility, returnType, name);
		tplTypes = new ArrayList<>();
	}
	

}
