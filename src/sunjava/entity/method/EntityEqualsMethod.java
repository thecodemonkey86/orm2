package sunjava.entity.method;

import java.util.ArrayList;

import database.column.Column;
import database.relation.PrimaryKey;
import sunjava.core.Method;
import sunjava.core.Param;
import sunjava.core.Types;
import sunjava.core.expression.BoolExpression;
import sunjava.core.expression.Expression;
import sunjava.core.expression.Expressions;
import sunjava.core.instruction.IfBlock;
import sunjava.entity.EntityCls;

public class EntityEqualsMethod extends Method {
	PrimaryKey pk;
	
	public EntityEqualsMethod(EntityCls cls, PrimaryKey pk) {
		super(Public, Types.Bool, "equals");
		this.pk = pk;
		addParam(new Param(Types.Object, "entity"));
	}

	@Override
	public void addImplementation() {
		EntityCls parent=(EntityCls) this.parent;
		Param entity = getParam("entity");
		IfBlock ifInstanceOf = _if(entity._instanceof(parent));
		
		if (!pk.isMultiColumn()) {
			ifInstanceOf.thenBlock()._return(parent.accessThisAttrByColumn(
				  pk.getFirstColumn())._equals( EntityCls.getPkExpression(entity.cast(parent),pk.getFirstColumn()))
			);	
		} else {
			ArrayList<Expression> expr=new ArrayList<>();
			for(Column colPk:pk.getColumns()) {
				expr.add(parent.accessThisAttrByColumn(colPk)._equals(  
					
					EntityCls.getPkExpression(entity.cast(parent),colPk)));
			}
			ifInstanceOf.thenBlock()._return(Expressions.and(expr));
		}
		ifInstanceOf.elseBlock()._return(BoolExpression.FALSE);
	}
	
	
}
