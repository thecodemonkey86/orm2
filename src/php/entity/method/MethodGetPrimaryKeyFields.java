package php.entity.method;

import database.column.Column;
import php.core.Types;
import php.core.expression.ArrayInitExpression;
import php.core.expression.PhpStringLiteral;
import php.core.method.Method;
import php.entity.EntityCls;

public class MethodGetPrimaryKeyFields extends Method {

	public MethodGetPrimaryKeyFields(EntityCls entity) {
		super(Public, entity.getTbl().getPrimaryKey().isMultiColumn() ? Types.array(Types.String) : Types.String, "getPrimaryKeyField"+(entity.getTbl().getPrimaryKey().isMultiColumn() ?"s":""));
		setStatic(true);
	}

	@Override
	public void addImplementation() {
		
		EntityCls entity = (EntityCls) parent;
		
		if(entity.getTbl().getPrimaryKey().isMultiColumn()) {
			ArrayInitExpression pkFieldNames = new ArrayInitExpression();
			for(Column col: entity.getTbl().getPrimaryKey().getColumns()) {
				pkFieldNames.addElement(new PhpStringLiteral(col.getName()));
			
			}
			_return(pkFieldNames); 
		} else {
			_return(new PhpStringLiteral(entity.getTbl().getPrimaryKey().getFirstColumn().getName())); 
		}
		

	}

}
