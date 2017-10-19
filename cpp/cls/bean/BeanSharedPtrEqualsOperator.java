package cpp.cls.bean;

import java.util.ArrayList;

import pg.PgCppUtil;
import model.Column;
import model.PrimaryKey;
import cpp.cls.Param;
import cpp.cls.expression.EqualOperator;
import cpp.cls.expression.Expression;
import cpp.cls.expression.Expressions;
import cpp.cls.expression.BinaryOperatorExpression;

public class BeanSharedPtrEqualsOperator extends EqualOperator {
	PrimaryKey pk;
	
	public BeanSharedPtrEqualsOperator(BeanCls cls, PrimaryKey pk) {
		super(new Param(cls.toSharedPtr(), "bean") );
		this.pk = pk;
		setConstQualifier(true);
		
	}

	@Override
	public void addImplementation() {
		BeanCls parent=(BeanCls) this.parent;
		Param bean = getParam("bean");
		if (!pk.isMultiColumn()) {
			_return(new BinaryOperatorExpression(parent.accessThisAttrGetterByColumn(pk.getFirstColumn()), 
					this,
					PgCppUtil.getPkExpression(bean,pk.getFirstColumn()))
			);	
		} else {
			ArrayList<Expression> expr=new ArrayList<>();
			for(Column colPk:pk.getColumns()) {
				expr.add(new BinaryOperatorExpression(
						parent.accessThisAttrGetterByColumn(colPk), 
					this, 
					PgCppUtil.getPkExpression(bean,colPk)));
			}
			_return(Expressions.and(expr));
		}
		
	}
	
	
}
