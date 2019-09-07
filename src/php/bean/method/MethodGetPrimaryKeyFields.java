package php.bean.method;

import database.column.Column;
import php.bean.BeanCls;
import php.core.Types;
import php.core.expression.ArrayInitExpression;
import php.core.expression.PhpStringLiteral;
import php.core.method.Method;

public class MethodGetPrimaryKeyFields extends Method {

	public MethodGetPrimaryKeyFields(BeanCls bean) {
		super(Public, bean.getTbl().getPrimaryKey().isMultiColumn() ? Types.array(Types.String) : Types.String, "getPrimaryKeyField"+(bean.getTbl().getPrimaryKey().isMultiColumn() ?"s":""));
		setStatic(true);
	}

	@Override
	public void addImplementation() {
		
		BeanCls bean = (BeanCls) parent;
		
		if(bean.getTbl().getPrimaryKey().isMultiColumn()) {
			ArrayInitExpression pkFieldNames = new ArrayInitExpression();
			for(Column col: bean.getTbl().getPrimaryKey().getColumns()) {
				pkFieldNames.addElement(new PhpStringLiteral(col.getName()));
			
			}
			_return(pkFieldNames); 
		} else {
			_return(new PhpStringLiteral(bean.getTbl().getPrimaryKey().getFirstColumn().getName())); 
		}
		

	}

}
