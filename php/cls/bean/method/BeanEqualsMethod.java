package php.cls.bean.method;

import java.util.ArrayList;

import model.Column;
import model.PrimaryKey;
import php.Types;
import php.cls.Method;
import php.cls.Param;
import php.cls.bean.BeanCls;
import php.cls.expression.BoolExpression;
import php.cls.expression.Expression;
import php.cls.expression.Expressions;
import php.cls.instruction.IfBlock;

public class BeanEqualsMethod extends Method {
	PrimaryKey pk;
	
	public BeanEqualsMethod(BeanCls cls, PrimaryKey pk) {
		super(Public, Types.Bool, "equals");
		this.pk = pk;
		addParam(new Param(cls, "bean"));
	}

	@Override
	public void addImplementation() {
		BeanCls parent=(BeanCls) this.parent;
		Param bean = getParam("bean");
		IfBlock ifInstanceOf = _if(bean._instanceof(parent));
		
		if (!pk.isMultiColumn()) {
			ifInstanceOf.thenBlock()._return(parent.accessThisAttrByColumn(
				  pk.getFirstColumn())._equals( BeanCls.getPkExpression(bean,pk.getFirstColumn()))
			);	
		} else {
			ArrayList<Expression> expr=new ArrayList<>();
			for(Column colPk:pk.getColumns()) {
				expr.add(parent.accessThisAttrByColumn(colPk)._equals(  
					
					BeanCls.getPkExpression(bean,colPk)));
			}
			ifInstanceOf.thenBlock()._return(Expressions.and(expr));
		}
		ifInstanceOf.elseBlock()._return(BoolExpression.FALSE);
	}
	
	
}
