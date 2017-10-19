package cpp.lib;

import cpp.cls.Cls;
import cpp.cls.ClsTemplate;
import cpp.cls.TplCls;
import cpp.cls.TplSymbol;
import cpp.cls.Type;

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
