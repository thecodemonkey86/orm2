package sunjava.cls.bean.method;

import sunjava.cls.Attr;
import sunjava.cls.JavaCls;
import sunjava.cls.Method;
import sunjava.cls.bean.BeanCls;
import sunjava.cls.expression.Expressions;
import sunjava.cls.instruction.Instructions;
import util.StringUtil;
import model.Column;

public class MethodManyDelegateGetter extends Method {
	protected Attr attr;
	protected Column col;
	protected String foreignClsName;
	
	public MethodManyDelegateGetter(Attr attr,Column col,String foreignClsName) {
		super(Method.Public, BeanCls.getTypeMapper().getTypeFromDbDataType( col.getDbType(),  col.isNullable()), "get"+StringUtil.ucfirst(attr.getName())+col.getUc1stCamelCaseName());
		this.attr = attr;
		this.col = col;
		this.foreignClsName = foreignClsName;
	}

	@Override
	public void addImplementation() {
		JavaCls parent = (JavaCls) this.parent;
		_if(Expressions.not(parent.getAttrByName("loaded"))).thenBlock()._callMethodInstr(_this(), "load");
		addInstr(Instructions._return(attr.callMethod("get"+col.getUc1stCamelCaseName())));
		
	}

}
