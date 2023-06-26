package cpp.jsonentity;

import java.util.ArrayList;

import util.pg.PgCppUtil;
import cpp.core.Param;
import cpp.core.expression.BinaryOperatorExpression;
import cpp.core.expression.EqualOperator;
import cpp.core.expression.Expression;
import cpp.core.expression.Expressions;
import database.column.Column;
import database.relation.PrimaryKey;

public class JsonEntityEqualsOperator extends EqualOperator {
	JsonEntity cls;
	
	public JsonEntityEqualsOperator(JsonEntity cls,boolean sp) {
		super(new Param(sp? cls.toSharedPtr().toConstRef(): cls.toConstRef(), "entity") );
		this.cls=cls;
		setParent(cls);
	}

	@Override
	public void addImplementation() {
		PrimaryKey pk = cls.getTbl().getPrimaryKey();
		Param entity = getParam("entity");
		if (!pk.isMultiColumn()) {
			_return(new BinaryOperatorExpression(cls.accessThisAttrGetterByColumn(pk.getFirstColumn()), 
					this,
					PgCppUtil.getJsonEntityPkExpression(entity,pk.getFirstColumn()))
			);	
		} else {
			ArrayList<Expression> expr=new ArrayList<>();
			for(Column colPk:pk.getColumns()) {
				expr.add(new BinaryOperatorExpression(
						cls.accessThisAttrGetterByColumn(colPk), 
					this, 
					PgCppUtil.getJsonEntityPkExpression(entity,colPk)));
			}
			_return(Expressions.and(expr));
		}
		
	}
	
	
}
