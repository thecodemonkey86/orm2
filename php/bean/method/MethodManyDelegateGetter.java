package php.bean.method;

import database.column.Column;
import php.bean.BeanCls;
import php.core.Attr;
import php.core.PhpCls;
import php.core.expression.Expressions;
import php.core.instruction.Instructions;
import php.core.method.Method;
import util.StringUtil;

public class MethodManyDelegateGetter extends Method {
	protected Attr attr;
	protected Column col;
	protected String foreignClsName;
	
	public MethodManyDelegateGetter(Attr attr,Column col,String foreignClsName) {
		super(Method.Public, BeanCls.getTypeMapper().getTypeFromDbDataType( col), "get"+StringUtil.ucfirst(attr.getName())+col.getUc1stCamelCaseName());
		this.attr = attr;
		this.col = col;
		this.foreignClsName = foreignClsName;
	}

	@Override
	public void addImplementation() {
		PhpCls parent = (PhpCls) this.parent;
		_if(Expressions.not(parent.getAttrByName("loaded"))).thenBlock()._callMethodInstr(_this(), "load");
		addInstr(Instructions._return(attr.callMethod("get"+col.getUc1stCamelCaseName())));
		
	}

}
