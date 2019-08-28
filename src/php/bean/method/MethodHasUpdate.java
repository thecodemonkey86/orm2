package php.bean.method;

import java.util.ArrayList;

import database.column.Column;
import php.bean.BeanCls;
import php.core.Types;
import php.core.expression.Expression;
import php.core.expression.Expressions;
import php.core.method.Method;
import php.lib.ClsBaseBean;

public class MethodHasUpdate extends Method{

	public MethodHasUpdate() {
		super(Public, Types.Bool, ClsBaseBean.METHOD_NAME_HAS_UPDATE);
	}

	@Override
	public void addImplementation() {
		BeanCls bean = (BeanCls) parent;
		
		ArrayList<Expression> conditions = new ArrayList<>();
		conditions.add(_this().callMethod("isPrimaryKeyModified"));
		for(Column col : bean.getTbl().getColumns(false)) {
			if (!col.isPartOfPk()) {
				conditions.add(BeanCls.accessIsColumnAttrOrEntityModified(_this(), col));
				;
			}
		}
//		if(!conditions.isEmpty())
			_return(Expressions.or(conditions));
//		else
//			_return(BoolExpression.FALSE);
	}

}
