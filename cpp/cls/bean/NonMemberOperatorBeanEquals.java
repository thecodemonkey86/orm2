package cpp.cls.bean;

import java.util.ArrayList;

import cpp.Types;
import cpp.cls.Operator;
import cpp.cls.Param;
import cpp.cls.expression.Expression;
import cpp.cls.expression.Expressions;
import cpp.cls.expression.BinaryOperatorExpression;
import model.Column;
import model.PrimaryKey;
import pg.PgCppUtil;

public class NonMemberOperatorBeanEquals extends Operator{

	PrimaryKey pk;
	
	public NonMemberOperatorBeanEquals(BeanCls cls, PrimaryKey pk) {
		super("==", Types.Bool, false);
		addParam(new Param(cls.toSharedPtr().toConstRef(), "bean1"));
		addParam(new Param(cls.toSharedPtr().toConstRef(), "bean2"));
		this.pk = pk;
		
	}

	@Override
	public void addImplementation() {
		Param bean1 = getParam("bean1");
		Param bean2 = getParam("bean2");
		if (!pk.isMultiColumn()) {
			_return(new BinaryOperatorExpression(
					PgCppUtil.getPkGetterExpression(bean1,pk.getFirstColumn()), 
					this,
					PgCppUtil.getPkGetterExpression(bean2,pk.getFirstColumn()))
			);	
		} else {
			ArrayList<Expression> expr=new ArrayList<>();
			for(Column colPk:pk.getColumns()) {
				expr.add(new BinaryOperatorExpression(
						PgCppUtil.getPkGetterExpression(bean1,colPk), 
					this, 
					PgCppUtil.getPkGetterExpression(bean2,colPk)));
			}
			_return(Expressions.and(expr));
		}
		
	}

}
