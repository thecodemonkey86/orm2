package php.entity.method;

import java.util.ArrayList;

import database.column.Column;
import database.relation.PrimaryKey;
import php.core.Param;
import php.core.Types;
import php.core.expression.BoolExpression;
import php.core.expression.Expression;
import php.core.expression.Expressions;
import php.core.instruction.IfBlock;
import php.core.method.Method;
import php.entity.EntityCls;

public class EntityEqualsMethod extends Method {
	PrimaryKey pk;
	
	public EntityEqualsMethod(EntityCls cls, PrimaryKey pk) {
		super(Public, Types.Bool, "equals");
		this.pk = pk;
		addParam(new Param(cls, "entity"));
	}

	@Override
	public void addImplementation() {
		EntityCls parent=(EntityCls) this.parent;
		Param entity = getParam("entity");
		IfBlock ifInstanceOf = _if(entity._instanceof(parent));
		
		if (!pk.isMultiColumn()) {
			ifInstanceOf.thenBlock()._return(parent.accessThisAttrByColumn(
				  pk.getFirstColumn())._equals( EntityCls.getPkExpression(entity,pk.getFirstColumn()))
			);	
		} else {
			ArrayList<Expression> expr=new ArrayList<>();
			for(Column colPk:pk.getColumns()) {
				expr.add(parent.accessThisAttrByColumn(colPk)._equals(  
					
					EntityCls.getPkExpression(entity,colPk)));
			}
			ifInstanceOf.thenBlock()._return(Expressions.and(expr));
		}
		ifInstanceOf.elseBlock()._return(BoolExpression.FALSE);
	}
	
	
}
