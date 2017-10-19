package cpp.cls.bean.method;

import util.StringUtil;
import model.Column;
import cpp.cls.Attr;
import cpp.cls.Method;
import cpp.cls.bean.BeanCls;
import cpp.cls.expression.Expressions;
import cpp.cls.instruction.Instructions;

public class MethodManyDelegateGetter extends Method {
	protected Attr attr;
	protected Column col;
	protected String foreignClsName;
	
	public MethodManyDelegateGetter(Attr attr,Column col,String foreignClsName) {
		super(Method.Public, BeanCls.getDatabaseMapper().getTypeFromDbDataType( col.getDbType(),  col.isNullable()), "get"+StringUtil.ucfirst(attr.getName())+col.getUc1stCamelCaseName());
		this.attr = attr;
		this.col = col;
		this.foreignClsName = foreignClsName;
	}

	@Override
	public void addImplementation() {
		_if(Expressions.not(parent.getAttrByName("loaded"))).thenBlock()._callMethodInstr(_this(), "load");
		addInstr(Instructions._return(attr.callMethod("get"+col.getUc1stCamelCaseName())));
		
	}

}
