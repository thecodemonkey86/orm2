package sunjava.cls;

import java.util.ArrayList;

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
