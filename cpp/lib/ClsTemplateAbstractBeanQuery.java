package cpp.lib;

import cpp.core.Cls;
import cpp.core.ClsTemplate;
import cpp.core.TplCls;
import cpp.core.TplSymbol;
import cpp.core.Type;

public class ClsTemplateAbstractBeanQuery extends ClsTemplate{

	public static final String CLSNAME="BeanQuery";
	
	public ClsTemplateAbstractBeanQuery() {
		super(CLSNAME, new TplSymbol("T"));
	}

	@Override
	public TplCls getConcreteClass(Type... types) {
		return new ClsAbstractBeanQuery(this, (Cls) types[0]);
	}

}
