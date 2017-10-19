package php.cls.bean.method;

import php.cls.Attr;
import php.cls.PhpCls;
import php.cls.Method;
import php.cls.bean.BeanCls;
import php.cls.expression.Expressions;
import php.cls.instruction.Instructions;
import util.StringUtil;
import model.Column;

public class MethodManyDelegateGetter extends Method {
	protected Attr attr;
	protected Column col;
	protected String foreignClsName;
	
	public MethodManyDelegateGetter(Attr attr,Column col,String foreignClsName) {
		super(Method.Public, BeanCls.getTypeMapper().getTypeFromDbDataType( col.getDbType()), "get"+StringUtil.ucfirst(attr.getName())+col.getUc1stCamelCaseName());
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
