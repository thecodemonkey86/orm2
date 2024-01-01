package php.entity.method;

import java.util.ArrayList;

import database.column.Column;
import php.core.Types;
import php.core.expression.Expression;
import php.core.expression.Expressions;
import php.core.method.Method;
import php.entity.EntityCls;
import php.lib.ClsBaseEntity;

public class MethodHasUpdate extends Method{

	public MethodHasUpdate() {
		super(Public, Types.Bool, ClsBaseEntity.METHOD_NAME_HAS_UPDATE);
	}

	@Override
	public void addImplementation() {
		EntityCls entity = (EntityCls) parent;
		
		ArrayList<Expression> conditions = new ArrayList<>();
		conditions.add(_this().callMethod("isPrimaryKeyModified"));
		for(Column col : entity.getTbl().getColumns(false)) {
			if (!col.isPartOfPk()) {
				conditions.add(EntityCls.accessIsColumnAttrOrEntityModified(_this(), col));
				;
			}
		}
//		if(!conditions.isEmpty())
			_return(Expressions.or(conditions));
//		else
//			_return(BoolExpression.FALSE);
	}

}
