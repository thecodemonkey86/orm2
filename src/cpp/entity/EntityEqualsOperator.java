package cpp.entity;

import java.util.ArrayList;

import util.pg.PgCppUtil;
import cpp.core.Param;
import cpp.core.expression.BinaryOperatorExpression;
import cpp.core.expression.EqualOperator;
import cpp.core.expression.Expression;
import cpp.core.expression.Expressions;
import database.column.Column;
import database.relation.PrimaryKey;

public class EntityEqualsOperator extends EqualOperator {
	PrimaryKey pk;
	
	public EntityEqualsOperator(EntityCls cls, PrimaryKey pk) {
		super(new Param(cls.toConstRef(), "entity") );
		this.pk = pk;
		
	}

	@Override
	public void addImplementation() {
		EntityCls parent=(EntityCls) this.parent;
		Param entity = getParam("entity");
		if (!pk.isMultiColumn()) {
			_return(new BinaryOperatorExpression(parent.accessThisAttrGetterByColumn(pk.getFirstColumn()), 
					this,
					PgCppUtil.getPkExpression(entity,pk.getFirstColumn()))
			);	
		} else {
			ArrayList<Expression> expr=new ArrayList<>();
			for(Column colPk:pk.getColumns()) {
				expr.add(new BinaryOperatorExpression(
						parent.accessThisAttrGetterByColumn(colPk), 
					this, 
					PgCppUtil.getPkExpression(entity,colPk)));
			}
			_return(Expressions.and(expr));
		}
		
	}
	
	
}
