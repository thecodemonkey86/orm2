package cpp.entity;

import java.util.ArrayList;

import cpp.Types;
import cpp.core.NonMemberOperator;
import cpp.core.Param;
import cpp.core.expression.BinaryOperatorExpression;
import cpp.core.expression.Expression;
import cpp.core.expression.Expressions;
import database.column.Column;
import database.relation.PrimaryKey;
import util.pg.PgCppUtil;

public class NonMemberOperatorEntityEquals extends NonMemberOperator{

	PrimaryKey pk;
	
	public NonMemberOperatorEntityEquals(EntityCls cls, PrimaryKey pk) {
		super("==", Types.Bool, false);
		addParam(new Param(cls.toSharedPtr().toConstRef(), "entity1"));
		addParam(new Param(cls.toSharedPtr().toConstRef(), "entity2"));
		this.pk = pk;
		
	}

	@Override
	public void addImplementation() {
		Param bean1 = getParam("entity1");
		Param bean2 = getParam("entity2");
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
