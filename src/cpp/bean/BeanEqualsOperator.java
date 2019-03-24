package cpp.bean;

import java.util.ArrayList;

import util.pg.PgCppUtil;
import cpp.core.Param;
import cpp.core.expression.BinaryOperatorExpression;
import cpp.core.expression.EqualOperator;
import cpp.core.expression.Expression;
import cpp.core.expression.Expressions;
import database.column.Column;
import database.relation.PrimaryKey;

public class BeanEqualsOperator extends EqualOperator {
	PrimaryKey pk;
	
	public BeanEqualsOperator(BeanCls cls, PrimaryKey pk) {
		super(new Param(cls.toConstRef(), "bean") );
		this.pk = pk;
		
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
