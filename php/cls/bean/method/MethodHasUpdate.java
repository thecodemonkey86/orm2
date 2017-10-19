package php.cls.bean.method;

import java.util.ArrayList;

import model.Column;
import php.Types;
import php.cls.Method;
import php.cls.bean.BeanCls;
import php.cls.expression.Expression;
import php.cls.expression.Expressions;
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
