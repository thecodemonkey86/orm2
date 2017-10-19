package sunjava.cls.bean.pk.method;

import java.util.ArrayList;

import model.Column;
import model.PrimaryKey;
import model.Table;
import sunjava.Types;
import sunjava.cls.Method;
import sunjava.cls.Param;
import sunjava.cls.bean.pk.PkMultiColumnType;
import sunjava.cls.expression.BoolExpression;
import sunjava.cls.expression.Expression;
import sunjava.cls.expression.Expressions;
import sunjava.cls.instruction.IfBlock;

public class MethodPkEquals extends Method {
	protected Table tbl;
	
	public MethodPkEquals(Table tbl) {
		super(Method.Public, Types.Bool, "equals");
		this.tbl = tbl;
		addParam(new Param(Types.Object, "other"));
		setOverrideAnnotation(true);
	}

	@Override
	public void addImplementation() {
		Param other = getParam("other");
		PkMultiColumnType parent=(PkMultiColumnType) this.parent;
		IfBlock ifInstanceOf = _if(other._instanceof(parent));
		PrimaryKey pk = tbl.getPrimaryKey();
		ArrayList<Expression> expr=new ArrayList<>();
		for(Column colPk:pk.getColumns()) {
			expr.add(_this().accessAttr(colPk.getCamelCaseName())._equals(  
				
					other.cast(parent).accessAttr(colPk.getCamelCaseName())));
		}
		ifInstanceOf.thenBlock()._return(Expressions.and(expr));
		ifInstanceOf.elseBlock()._return(BoolExpression.FALSE);
		
	}

}
