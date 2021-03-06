package sunjava.bean.method;

import java.util.ArrayList;

import database.column.Column;
import sunjava.bean.BeanCls;
import sunjava.core.Method;
import sunjava.core.Types;
import sunjava.core.expression.Expression;
import sunjava.core.expression.Expressions;
import sunjava.lib.ClsBaseBean;

public class MethodHasUpdate extends Method{

	public MethodHasUpdate() {
		super(Public, Types.Bool, ClsBaseBean.METHOD_NAME_HAS_UPDATE);
		setOverrideAnnotation(true);
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
