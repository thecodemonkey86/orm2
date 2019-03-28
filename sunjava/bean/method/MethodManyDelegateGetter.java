package sunjava.bean.method;

import database.column.Column;
import sunjava.bean.BeanCls;
import sunjava.core.Attr;
import sunjava.core.JavaCls;
import sunjava.core.Method;
import sunjava.core.expression.Expressions;
import sunjava.core.instruction.Instructions;
import util.StringUtil;

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
