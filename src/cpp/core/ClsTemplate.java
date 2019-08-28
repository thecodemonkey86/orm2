package cpp.core;

import java.util.Arrays;
import java.util.List;

public abstract class ClsTemplate {

	protected List<TplSymbol> symbols;
	protected String name;
	
	public ClsTemplate(String name,TplSymbol...symbols ) {
		this.symbols =  (List<TplSymbol>) Arrays.asList(symbols);
		this.name = name;
	}
	
	public abstract TplCls getConcreteClass(Type...types) ;
	
	public String getName() {
		return name;
	}
}
