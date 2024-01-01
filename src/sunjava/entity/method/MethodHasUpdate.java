package sunjava.entity.method;

import java.util.ArrayList;

import database.column.Column;
import sunjava.core.Method;
import sunjava.core.Types;
import sunjava.core.expression.Expression;
import sunjava.core.expression.Expressions;
import sunjava.entity.EntityCls;
import sunjava.lib.ClsBaseEntity;

public class MethodHasUpdate extends Method{

	public MethodHasUpdate() {
		super(Public, Types.Bool, ClsBaseEntity.METHOD_NAME_HAS_UPDATE);
		setOverrideAnnotation(true);
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
