package sunjava.bean.method;

import java.util.ArrayList;

import database.column.Column;
import database.relation.PrimaryKey;
import sunjava.bean.BeanCls;
import sunjava.core.Method;
import sunjava.core.Param;
import sunjava.core.Types;
import sunjava.core.expression.BoolExpression;
import sunjava.core.expression.Expression;
import sunjava.core.expression.Expressions;
import sunjava.core.instruction.IfBlock;

public class BeanEqualsMethod extends Method {
	PrimaryKey pk;
	
	public BeanEqualsMethod(BeanCls cls, PrimaryKey pk) {
		super(Public, Types.Bool, "equals");
		this.pk = pk;
		addParam(new Param(Types.Object, "bean"));
	}

	@Override
	public void addImplementation() {
		BeanCls parent=(BeanCls) this.parent;
		Param bean = getParam("bean");
		IfBlock ifInstanceOf = _if(bean._instanceof(parent));
		
		if (!pk.isMultiColumn()) {
			ifInstanceOf.thenBlock()._return(parent.accessThisAttrByColumn(
				  pk.getFirstColumn())._equals( BeanCls.getPkExpression(bean.cast(parent),pk.getFirstColumn()))
			);	
		} else {
			ArrayList<Expression> expr=new ArrayList<>();
			for(Column colPk:pk.getColumns()) {
				expr.add(parent.accessThisAttrByColumn(colPk)._equals(  
					
					BeanCls.getPkExpression(bean.cast(parent),colPk)));
			}
			ifInstanceOf.thenBlock()._return(Expressions.and(expr));
		}
		ifInstanceOf.elseBlock()._return(BoolExpression.FALSE);
	}
	
	
}
