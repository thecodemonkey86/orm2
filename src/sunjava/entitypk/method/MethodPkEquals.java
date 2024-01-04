package sunjava.entitypk.method;

import java.util.ArrayList;

import database.column.Column;
import database.relation.PrimaryKey;
import database.table.Table;
import sunjava.core.Method;
import sunjava.core.Param;
import sunjava.core.Types;
import sunjava.core.expression.BoolExpression;
import sunjava.core.expression.Expression;
import sunjava.core.expression.Expressions;
import sunjava.core.instruction.IfBlock;
import sunjava.entitypk.PkMultiColumnType;

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
