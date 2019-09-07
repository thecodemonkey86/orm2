package cpp.entity.method;

import util.StringUtil;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.core.expression.Expressions;
import cpp.core.instruction.Instructions;
import cpp.entity.EntityCls;
import database.column.Column;

public class MethodManyDelegateGetter extends Method {
	protected Attr attr;
	protected Column col;
	protected String foreignClsName;
	
	public MethodManyDelegateGetter(Attr attr,Column col,String foreignClsName) {
		super(Method.Public, EntityCls.getDatabaseMapper().getTypeFromDbDataType( col.getDbType(),  col.isNullable()), "get"+StringUtil.ucfirst(attr.getName())+col.getUc1stCamelCaseName());
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
