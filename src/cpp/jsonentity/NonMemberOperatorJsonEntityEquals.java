package cpp.jsonentity;

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

public class NonMemberOperatorJsonEntityEquals extends NonMemberOperator{

	PrimaryKey pk;
	
	public NonMemberOperatorJsonEntityEquals(JsonEntity cls, boolean sp) {
		super("==", Types.Bool, false);
		addParam(new Param(sp? cls.toSharedPtr().toConstRef():cls.toConstRef(), "entity1"));
		addParam(new Param(sp? cls.toSharedPtr().toConstRef():cls.toConstRef(), "entity2"));
		this.pk = cls.getTbl().getPrimaryKey();
		
	}

	@Override
	public void addImplementation() {
		Param entity1 = getParam("entity1");
		Param entity2 = getParam("entity2");
		if (!pk.isMultiColumn()) {
			_return(new BinaryOperatorExpression(
					PgCppUtil.getJsonEntityPkGetterExpression(entity1,pk.getFirstColumn()), 
					this,
					PgCppUtil.getJsonEntityPkGetterExpression(entity2,pk.getFirstColumn()))
			);	
		} else {
			ArrayList<Expression> expr=new ArrayList<>();
			for(Column colPk:pk.getColumns()) {
				expr.add(new BinaryOperatorExpression(
						PgCppUtil.getJsonEntityPkGetterExpression(entity1,colPk), 
					this, 
					PgCppUtil.getJsonEntityPkGetterExpression(entity2,colPk)));
			}
			_return(Expressions.and(expr));
		}
		
	}

}
